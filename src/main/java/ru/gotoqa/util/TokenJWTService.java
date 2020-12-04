package ru.gotoqa.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.junit.jupiter.api.Assertions;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Service for preparing JWT token
 * JSON Web Tokens, standard RFC 7519
 */
@Service
public class TokenJWTService {

    private static final String PUBLIC_KEY_PATH = "src/main/resources/keys/public.txt";
    private static final String PRIVATE_KEY_PATH = "src/main/resources/keys/privateKcs8_key";

    private static final String KEY_ID = Utils.nextId(5);
    private static final String JWT_ID = Utils.nextId(5);
    private static final String SUBJECT = Utils.nextId(15);
    private static final String ISSUER = "http://gotoqa.ru/";
    private static final String CLAIM_TYP = "JWT Project";
    private static final String CLAIM_SESSION_STATE = Utils.nextId(15);

    /**
     * JWT token creation method
     * Pseudocode:
     * JWT Token = encodeBase64Url(header) + '.' + encodeBase64Url(payload) + '.' + encodeBase64Url(signature)
     *
     * @param tokenPayload - Set variables in Payload
     * @return - String header+payload+signature
     */
    public String getTokenJWT(String tokenPayload) {

        // Loading keys from file
        RSAPublicKey publicKey = null;
        RSAPrivateKey privateKey = null;
        try {
            publicKey = TokenJWT.readPublicKeyFromFile(PUBLIC_KEY_PATH);
            privateKey = TokenJWT.readPrivateKeyFromFile(PRIVATE_KEY_PATH);
        } catch (Exception ex) {
            Assertions.assertNull(ex.getMessage(), "Keys file not found");
        }

        // Create and sign a JWT token
        String token = null;
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);

            token = JWT.create()
                    .withKeyId(KEY_ID)
                    .withJWTId(JWT_ID)
                    .withExpiresAt(Utils.getFlexibleDay(1))
                    .withIssuedAt(Utils.getFlexibleDay(-1))
                    .withSubject(SUBJECT)
                    .withIssuer(ISSUER)
                    .withClaim("typ", CLAIM_TYP)
                    .withClaim("session_state", CLAIM_SESSION_STATE)
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
            Assertions.assertNull(exception.getMessage(),
                    "Incorrect signature configuration / Claims parameters were not converted");
        }

        return token;
    }
}
