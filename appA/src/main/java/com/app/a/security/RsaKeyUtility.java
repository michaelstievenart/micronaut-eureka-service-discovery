package com.app.a.security;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RsaKeyUtility {
    private static final Pattern PEM_PATTERN = Pattern.compile("-----BEGIN (.*)-----(.*)-----END (.*)-----", Pattern.DOTALL);
    private static final String RSA_CONSTANT = "RSA";
    private static final String PUBLIC_KEY_CONSTANT = "PUBLIC KEY";

    private RsaKeyUtility() {
    }

    public static Optional<RSAPublicKey> parsePublicKey(final String pemData) {
        return Optional.of((RSAPublicKey) extractPK(pemData)
                        .orElseThrow(() -> new IllegalArgumentException("PEM Data does not contain a public key")));
    }

    static Optional<Key> extractPK(final String pemData) {
        final Matcher m = PEM_PATTERN.matcher(pemData.trim());
        if (!m.matches()) {
            throw new IllegalArgumentException("String is not PEM encoded data");
        }

        final String type = m.group(1);
        final byte[] bytes = utf8Encode(m.group(2));
        final byte[] content = Base64.getMimeDecoder().decode(bytes);

        PublicKey publicKey = null;
        try {
            final KeyFactory fact = KeyFactory.getInstance(RSA_CONSTANT);
            if (PUBLIC_KEY_CONSTANT.equals(type)) {
                KeySpec keySpec = new X509EncodedKeySpec(content);
                publicKey = fact.generatePublic(keySpec);
            }

            return Optional.ofNullable(publicKey);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private static byte[] utf8Encode(CharSequence string) {
        try {
            ByteBuffer bytes = StandardCharsets.UTF_8.newEncoder().encode(CharBuffer.wrap(string));
            byte[] bytesCopy = new byte[bytes.limit()];
            System.arraycopy(bytes.array(), 0, bytesCopy, 0, bytes.limit());
            return bytesCopy;
        } catch (CharacterCodingException e) {
            throw new RuntimeException(e);
        }
    }
}

