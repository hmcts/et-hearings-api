package uk.gov.hmcts.reform.et.service;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.factories.DefaultJWSVerifierFactory;
import com.nimbusds.jose.jwk.AsymmetricJWK;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.SecretJWK;
import com.nimbusds.jose.proc.JWSVerifierFactory;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.authorisation.exceptions.InvalidTokenException;

import java.net.URL;
import java.security.Key;

@Slf4j
@Service
public class VerifyTokenService {

    private final JWSVerifierFactory jwsVerifierFactory;

    @Value("${idam.api.jwksUrl}")
    private String idamJwkUrl;

    public VerifyTokenService() {
        this.jwsVerifierFactory = new DefaultJWSVerifierFactory();
    }

    public boolean verifyTokenSignature(String token) {
        try {
            var tokenTocheck = StringUtils.replace(token, "Bearer ", "");
            var signedJwt = SignedJWT.parse(tokenTocheck);
            JWKSet jsonWebKeySet = loadJsonWebKeySet(idamJwkUrl);

            var jwsHeader = signedJwt.getHeader();

            var key = findKeyById(jsonWebKeySet, jwsHeader.getKeyID());

            var jwsVerifier = jwsVerifierFactory.createJWSVerifier(jwsHeader, key);

            return signedJwt.verify(jwsVerifier);
        } catch (Exception e) {
            log.error("Token validation error:", e);
            return false;
        }
    }

    private JWKSet loadJsonWebKeySet(String jwksUrl) {
        try {
            return JWKSet.load(new URL(jwksUrl));
        } catch (Exception e) {
            log.error("JWKS key loading error", e);
            throw new InvalidTokenException("JWKS error", e);
        }
    }

    private Key findKeyById(JWKSet jsonWebKeySet, String keyId) {
        try {
            JWK jsonWebKey = jsonWebKeySet.getKeyByKeyId(keyId);
            if (jsonWebKey == null) {
                throw new InvalidTokenException("JWK does not exist in the key set");
            }
            if (jsonWebKey instanceof SecretJWK) {
                return ((SecretJWK) jsonWebKey).toSecretKey();
            }
            if (jsonWebKey instanceof AsymmetricJWK) {
                return ((AsymmetricJWK) jsonWebKey).toPublicKey();
            }
            throw new InvalidTokenException("Unsupported JWK " + jsonWebKey.getClass().getName());
        } catch (JOSEException e) {
            log.error("Invalid JWK key", e);
            throw new InvalidTokenException("Invalid JWK", e);
        }
    }

}
