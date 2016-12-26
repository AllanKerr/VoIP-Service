package com.kerr.nearme.events;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.kerr.nearme.FirebaseAuthenticator;

import java.util.List;

@Api(name = "events", version = "v1",
        title = "Events API",
        description = "API for handling event creation, fetching and queries.")
public class EventsAPI {

    @ApiMethod(name = "cities.list")
    public List<String> listCityEvents() {
        return null;
    }

    @ApiMethod(name = "myevents.list", authenticators = {FirebaseAuthenticator.class})
    public List<String> listMyEvents(User user) throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("");
        }
        return null;
    }

    @ApiMethod(name = "myevents.add", authenticators = {FirebaseAuthenticator.class})
    public List<String> addEvent(User user) throws UnauthorizedException  {
        if (user == null) {
            throw new UnauthorizedException("");
        }
        return null;
    }
}
