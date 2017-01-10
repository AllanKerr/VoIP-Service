package com.kerr.nearme;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Authenticator;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Logger;

/**
 * Created by allankerr on 2016-12-23.
 */
public class FirebaseAuthenticator implements Authenticator {

    private static final Logger log = Logger.getLogger(FirebaseAuthenticator.class.getName());

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public User authenticate(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        try {
            FirebaseTokenVerifier verifier = new FirebaseTokenVerifier();
            FirebaseToken decodedToken = verifier.verify(token);
            return new User(decodedToken.getUid(), decodedToken.getEmail());
        } catch (GeneralSecurityException e) {

        } catch (IOException e) {

        } catch (MalformedJwtException e) {

        } catch (SignatureException e) {

        }
        return null;
    }
}
