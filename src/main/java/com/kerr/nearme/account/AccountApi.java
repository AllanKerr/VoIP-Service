package com.kerr.nearme.account;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.kerr.nearme.ApiKeys;
import com.kerr.nearme.FirebaseAuthenticator;
import com.kerr.nearme.billing.BillableQueue;
import com.kerr.nearme.billing.SmsBillable;
import com.kerr.nearme.objectify.Dao;
import com.kerr.twilio.incomingphonenumbers.BuyPhoneNumberRequest;
import com.kerr.twilio.incomingphonenumbers.IncomingPhoneNumber;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.VoiceGrant;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Api(name = "account", version = "v1",
        title = "Account API",
        description = "API for handling event creation, fetching and queries.")
public class AccountApi {


    @ApiMethod(name = "send.sms", path = "send/sms")
    public void sendSmsMessage(User user, Message message) throws UnauthorizedException, TwilioRestException {
        if (user == null) {
            user = new User("work.allankerr@gmail.com", "", "ZYh1BJU0QyWbVw1vDgQshDKelCP2");
            //throw new UnauthorizedException("");
        }
        TwilioRestClient client = new TwilioRestClient(ApiKeys.TWILIO_ACCOUNT_SID, ApiKeys.TWILIO_AUTH_TOKEN);
        MessageFactory factory = client.getAccount().getMessageFactory();

        AccountDao accountDao = new AccountDao();
        Account account = accountDao.load(user.getUserId());
        List<NameValuePair> content = new LinkedList<NameValuePair>();
        content.add(new BasicNameValuePair("To", message.getPhoneNumber()));
        content.add(new BasicNameValuePair("From", account.getOutgoingNumber().getPhoneNumber()));
        content.add(new BasicNameValuePair("Body", message.getBody()));
        com.twilio.sdk.resource.instance.Message sent = factory.create(content);

        Key<Account> accountKey = Key.create(Account.class, user.getUserId());
        BillableQueue.push(new SmsBillable(accountKey, sent.getSid()));
    }

    @ApiMethod(name = "buyPhoneNumber", path = "buyPhoneNumber", authenticators = {FirebaseAuthenticator.class})
    public void buyPhoneNumber(final User user, PhoneNumber number) throws UnauthorizedException, IOException {
        if (user == null) {
            throw new UnauthorizedException("");
        }
        final IncomingPhoneNumber newNumber = new BuyPhoneNumberRequest(ApiKeys.TWILIO_ACCOUNT_SID, ApiKeys.TWILIO_AUTH_TOKEN, number.getPhoneNumber())
                .setVoiceApplicationSid(ApiKeys.TWILIO_APPLICATION_SID)
                .setSmsApplicationSid(ApiKeys.TWILIO_APPLICATION_SID)
                .fetchResponse();

        ObjectifyService.ofy().transact(new VoidWork() {
            public void vrun() {
                AccountDao accountDao = new AccountDao();
                Account account = accountDao.load(user.getUserId());
                PhoneNumber phoneNumber = new PhoneNumber(newNumber.getPhoneNumber());
                account.addPhoneNumber(phoneNumber);
                new Dao<PhoneNumber>(PhoneNumber.class).save(phoneNumber);
                accountDao.save(account);
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
                ApiKeys.TWILIO_ACCOUNT_SID,
                ApiKeys.TWILIO_API_SID,
                ApiKeys.TWILIO_API_SECRET
        ).identity(user.getUserId()).grant(grant).build();
        return new TwilioAccessToken(token);
    }
}
