package com.iforddow.bizaudo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class TokenHasher {

    @Value("${hmac.algo}")
    private String hmacAlgo;

    @Value("${hmac.secret}")
    private String hmacSecret;

    /**
     * A method to hash a String using
     * SHA-256 algorithm.
     *
     * @author IFD
     * @since 2025-07-18
     * */
    public String hmacSha256(String str) {

        try {
            SecretKeySpec keySpec = new SecretKeySpec(hmacSecret.getBytes(), hmacAlgo);
            Mac mac = Mac.getInstance(hmacAlgo);
            mac.init(keySpec);

            byte[] rawHmac = mac.doFinal(str.getBytes());

            return Base64.getEncoder().encodeToString(rawHmac);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
