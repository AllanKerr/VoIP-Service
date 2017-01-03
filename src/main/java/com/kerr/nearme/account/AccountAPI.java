package com.kerr.nearme.events;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.kerr.nearme.FirebaseAuthenticator;

import java.util.List;

@Api(name = "events", version = "v1",
        title = "Account API",
        description = "API for handling event creation, fetching and queries.")
public class AccountAPI {


    @ApiMethod(name = "test", path = "test")
    public List<String> listCityEvents() {
        return null;
    }
}
