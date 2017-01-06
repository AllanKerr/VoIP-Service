package com.kerr.nearme;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * Created on 2017-01-06.
 * See http://stackoverflow.com/questions/39222072/firebase-verify-id-tokens-using-a-third-party-jwt-library.
 */
public class FirebaseTokenVerifier {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FirebaseTokenVerifier.class.getName());

    private static final String PUBLIC_KEYS_URI = "https://www.googleapis.com/robot/v1/metadata/x509/securetoken@system.gserviceaccount.com";

    private static final String EMAIL_CLAIM = "email";
    /**
     *
     * @param token
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public FirebaseToken verify(String token) throws GeneralSecurityException, IOException {
        if (token == null || token.isEmpty()) {
            throw new SignatureException("");
        }
        // get public keys
        JsonObject publicKeys = getPublicKeysJson();

        // get json object as map
        // loop map of keys finding one that verifies
        for (Map.Entry<String, JsonElement> entry : publicKeys.entrySet()) {
            try {
                // get public key
                logger.info(entry.getKey());
                PublicKey publicKey = getPublicKey(entry);

                // validate claim set
                Jws<Claims> jws = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
                String uuid = jws.getBody().getSubject();
                String email = (String)jws.getBody().get(EMAIL_CLAIM);
                return new FirebaseToken(uuid, email);
            } catch (SignatureException e) {
                // If the key doesn't match the next key should be tried
            }
        }
        throw new SignatureException("");
    }

    /**
     *
     * @param entry
     * @return
     * @throws GeneralSecurityException
     */
    private PublicKey getPublicKey(Map.Entry<String, JsonElement> entry) throws GeneralSecurityException, IOException {
        String publicKeyPem = entry.getValue().getAsString()
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)----", "")
                .replaceAll("\r\n", "")
                .replaceAll("\n", "")
                .trim();

        logger.info(publicKeyPem);

        // generate x509 cert
        InputStream inputStream = new ByteArrayInputStream(entry.getValue().getAsString().getBytes("UTF-8"));
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate)cf.generateCertificate(inputStream);

        return cert.getPublicKey();
    }

    /**
     *
     * @return
     * @throws IOException
     */
    private JsonObject getPublicKeysJson() throws IOException {
        // get public keys
        URI uri = URI.create(PUBLIC_KEYS_URI);
        GenericUrl url = new GenericUrl(uri);
        HttpTransport http = new NetHttpTransport();
        HttpResponse response = http.createRequestFactory().buildGetRequest(url).execute();

        // store json from request
        String json = response.parseAsString();
        // disconnect
        response.disconnect();

        // parse json to object
        return new JsonParser().parse(json).getAsJsonObject();
    }
}
