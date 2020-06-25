package com.app.a.security;

import javax.inject.Named;
import javax.inject.Singleton;

import java.util.List;
import java.util.Map;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.oauth2.endpoint.token.response.OauthUserDetailsMapper;
import io.micronaut.security.oauth2.endpoint.token.response.TokenResponse;
import io.micronaut.security.token.validator.TokenValidator;
import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.app.a.config.ClientConfiguration.UAA_SERVICE;

@Singleton
@Named(UAA_SERVICE)
@RequiredArgsConstructor
public class JwtAuthenticationMapper implements OauthUserDetailsMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationMapper.class);
    private final TokenValidator tokenValidator;

    @Override
    public Publisher<UserDetails> createUserDetails(final TokenResponse tokenResponse) {
        LOGGER.trace("parsing TokenResponse");
        return Flowable
                .fromPublisher(tokenValidator.validateToken(tokenResponse.getAccessToken()))
                .map(AuthenticationMapper::getSimpleUserDetails)
                .map(userDetails -> {
                    userDetails.setAttributes(Map.of(
                            ACCESS_TOKEN_KEY, tokenResponse.getAccessToken(),
                            REFRESH_TOKEN_KEY, refreshToken(tokenResponse),
                            PROVIDER_KEY, "uaa"
                    ));
                    return userDetails;
                });
    }

    private String refreshToken(final TokenResponse tokenResponse) {
        return tokenResponse.getRefreshToken() == null ? "" : tokenResponse.getRefreshToken();
    }

    private static class AuthenticationMapper {

        public static UserDetails getSimpleUserDetails(final Authentication authentication) {
            final String username = (String) authentication.getAttributes().get("user_name");
            final List<String> authorities = (List<String>) authentication.getAttributes().get("authorities");
            return new UserDetails(username, authorities);
        }
    }
}

