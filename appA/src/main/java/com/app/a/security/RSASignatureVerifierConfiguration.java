package com.app.a.security;

import javax.inject.Named;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Context;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.inject.qualifiers.Qualifiers;
import io.micronaut.security.token.jwt.signature.rsa.RSASignatureConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.app.a.config.ClientConfiguration.UAA_SERVICE;

@Context
@Named(UAA_SERVICE)
public class RSASignatureVerifierConfiguration implements RSASignatureConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(RSASignatureVerifierConfiguration.class);
    private RSAPublicKey rsaPublicKey;
    private final BeanContext beanContext;
    private static final String TOKEN_ENDPOINT = "/oauth/token_key";

    public RSASignatureVerifierConfiguration(final BeanContext beanContext) {
        this.beanContext = beanContext;
        fetchPublicKey();
    }

    @Override
    public RSAPublicKey getPublicKey() {
        return this.rsaPublicKey;
    }

    private void fetchPublicKey() {
        LOG.debug("fetching public key");
        final var tokenKey = makeRequest();
        final var optionalRSAPublicKey = RsaKeyUtility.parsePublicKey(tokenKey);
        if (optionalRSAPublicKey.isPresent()) {
            LOG.debug("public key added to verifier configuration");
            this.rsaPublicKey = optionalRSAPublicKey.get();
        }
    }

    private String makeRequest() {
        final var request = HttpRequest
                .GET(TOKEN_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON_TYPE);
        return (String) getClient()
                .retrieve(request, Map.class)
                .map(map -> map.get("value"))
                .blockingFirst();
    }

    private RxHttpClient getClient() {
        return beanContext
                .findBean(RxHttpClient.class, Qualifiers.byName(UAA_SERVICE))
                .orElseThrow(() -> new RuntimeException(String.format("Missing Client %s", UAA_SERVICE)));
    }
}

