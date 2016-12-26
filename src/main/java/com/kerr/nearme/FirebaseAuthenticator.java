package com.kerr.nearme;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Authenticator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import com.google.firebase.tasks.Task;
import com.google.firebase.tasks.Tasks;

/**
 * Created by allankerr on 2016-12-23.
 */
public class FirebaseAuthenticator implements Authenticator {

    private static final Logger log = Logger.getLogger(FirebaseAuthenticator.class.getName());

    private static final String AUTHORIZATION_HEADER = "Authorization";

    static {
        log.info("Initializing Firebase Admin SDK");
        ClassLoader classLoader = FirebaseAuthenticator.class.getClassLoader();
        InputStream keyFileStream = classLoader.getResourceAsStream(Constants.FIREBASE_SERVICE_ACCOUNT_KEY);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(keyFileStream)
                .setDatabaseUrl(Constants.FIREBASE_DATABASE_URL)
                .build();

        FirebaseApp app = FirebaseApp.initializeApp(options);
        log.info("Firebase succeeded for app: " + app.getName());
    }

    @Override
    public User authenticate(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        Task<FirebaseToken> authTask = FirebaseAuth.getInstance().verifyIdToken(token);
        try {
            Tasks.await(authTask);
            FirebaseToken decodedToken = authTask.getResult();
            return new User(decodedToken.getUid());
        } catch (ExecutionException e) {
            log.info("Verification failed due to null or malformed token.");
            e.printStackTrace();
        } catch (InterruptedException e) {
            log.info("Verification interrupted.");
            e.printStackTrace();
        }
        // TODO Update for deployment. Temporary UID due to lack of third-party authentication in the Google API Explorer.
        return new User("Sy2KZvijbug8GkzKHwflttMQf5v2");
    }
}
