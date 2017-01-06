package com.kerr.nearme;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.availablephonenumbercountry.Local;

import java.util.LinkedList;

/**
 * Created by allankerr on 2017-01-06.
 */
@Api(name = "availablephonenumbers", version = "v1",
        title = "Available Phone Numbers API",
        description = "API for querying and purchasing of phone numbers.")
public class AvailablePhoneNumbersApi {

    @ApiMethod(name = "test", path = "availablephonenumbers")
    public LinkedList<String> availablePhone(@Named("Contains") String contains) {
        Twilio.init(APIKeys.TWILIO_ACCOUNT_SID, APIKeys.TWILIO_AUTH_TOKEN);

        LinkedList<String> list = new LinkedList<String>();
        ResourceSet<Local> numbers = Local.reader("CA").setContains(contains).read();
        for (Local l : numbers) {
            list.add(l.getFriendlyName().toString());
        }
        return list;
    }
}
