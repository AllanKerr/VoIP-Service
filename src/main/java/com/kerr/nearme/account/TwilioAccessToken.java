package com.kerr.nearme.account;

import com.kerr.nearme.APIKeys;
import com.twilio.jwt.accesstoken.AccessToken;

/**
 * Created by allankerr on 2017-01-03.
 */
public class TwilioAccessToken {

    private String rawAccessToken;

    public String getRawAccessToken() {
        return rawAccessToken;
    }

    public TwilioAccessToken(AccessToken accessToken) {
        this.rawAccessToken = accessToken.toJwt();
    }
}
