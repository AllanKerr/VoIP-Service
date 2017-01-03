package com.kerr.nearme.account;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.kerr.nearme.APIKeys;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Account;

import java.util.HashMap;

@Api(name = "account", version = "v1",
        title = "Account API",
        description = "API for handling event creation, fetching and queries.")
public class AccountAPI {


    @ApiMethod(name = "send.sms", path = "send/sms")
    public void sendSmsMessage(User user, Message message) throws UnauthorizedException, TwilioRestException {
        if (user == null) {
        //    throw new UnauthorizedException("");
        }
        TwilioRestClient client = new TwilioRestClient(APIKeys.TWILIO_ACCOUNT_SID, APIKeys.TWILIO_AUTH_TOKEN);
        Account account = client.getAccount();

        SmsFactory factory = account.getSmsFactory();

        HashMap<String, String> content = new HashMap<String,String>();
        content.put("To", message.getPhoneNumber());
        content.put("From", "+13067007600");
        content.put("Body", message.getBody());
        factory.create(content);
    }
}
