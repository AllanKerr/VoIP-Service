package com.kerr.nearme.account;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.kerr.nearme.APIKeys;
import com.kerr.nearme.FirebaseAuthenticator;
import com.kerr.twilio.incomingphonenumbers.BuyPhoneNumberRequest;
import com.kerr.twilio.incomingphonenumbers.IncomingPhoneNumber;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.VoiceGrant;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.SmsFactory;

import java.io.IOException;
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
        SmsFactory factory = client.getAccount().getSmsFactory();

        HashMap<String, String> content = new HashMap<String, String>();
        content.put("To", message.getPhoneNumber());
        content.put("From", "+13067007600");
        content.put("Body", message.getBody());
        factory.create(content);
    }

    @ApiMethod(name = "buyPhoneNumber", path = "buyPhoneNumber", authenticators = {FirebaseAuthenticator.class})
    public void buyPhoneNumber(final User user, PhoneNumber number) throws UnauthorizedException, IOException {
        if (user == null) {
            throw new UnauthorizedException("");
        }
        final IncomingPhoneNumber newNumber = new BuyPhoneNumberRequest(APIKeys.TWILIO_ACCOUNT_SID, APIKeys.TWILIO_AUTH_TOKEN, number.getPhoneNumber())
                .setVoiceApplicationSid(APIKeys.TWILIO_APPLICATION_SID)
                .setSmsApplicationSid(APIKeys.TWILIO_APPLICATION_SID)
                .fetchResponse();

        ObjectifyService.ofy().transact(new VoidWork() {
            public void vrun() {
                AccountClient client = new AccountClient();
                Account account = client.getAccount(user.getUserId());
                PhoneNumber phoneNumber = new PhoneNumber(newNumber.getPhoneNumber());
                account.addPhoneNumber(phoneNumber);
                phoneNumber.save();
                account.save();
            }
        });
    }

    @ApiMethod(name = "accessToken", path = "accessToken", authenticators = {FirebaseAuthenticator.class})
    public TwilioAccessToken accessToken(User user) throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("");
        }
        VoiceGrant grant = new VoiceGrant();
        grant.setOutgoingApplicationSid("APf952467cd44bb6c6da4edd7b7a0cced2");
        grant.setPushCredentialSid("CR63be6699797688527c43fdcf30babd68");

        AccessToken token = new AccessToken.Builder(
                APIKeys.TWILIO_ACCOUNT_SID,
                APIKeys.TWILIO_API_SID,
                APIKeys.TWILIO_API_SECRET
        ).identity(user.getUserId()).grant(grant).build();
        return new TwilioAccessToken(token);
    }
}
