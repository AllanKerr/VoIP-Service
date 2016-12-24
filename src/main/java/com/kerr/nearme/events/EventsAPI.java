package com.kerr.nearme.events;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.users.User;
import com.kerr.nearme.FirebaseAuthenticator;

import java.util.List;

@Api(name = "events", version = "v1",
        title = "Events API",
        description = "API for handling event creation, fetching and queries.")
public class EventsAPI {

    @ApiMethod(name = "cities.list", authenticators = {FirebaseAuthenticator.class})
    public List<String> citiesList(User user) {
        //EventController controller = new EventController();
        return null;//controller.fetchEventsByUserID(userID);
    }

    @ApiMethod(name = "myevents.list")
    public List<String> myEventsList(@Named("userID") String userID) {
        //EventController controller = new EventController();
        return null;//controller.fetchEventsByUserID(userID);
    }

    @ApiMethod(name = "myevents.insert")
    public List<String> myEventsInsert(@Named("userID") String userID) {
        //EventController controller = new EventController();
        return null;//controller.fetchEventsByUserID(userID);
    }
}
