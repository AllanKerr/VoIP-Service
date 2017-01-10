package com.kerr.nearme.messages;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.kerr.nearme.ApiKeys;
import com.kerr.nearme.FirebaseAuthenticator;
import com.kerr.nearme.account.Account;
import com.kerr.nearme.account.AccountDao;
import com.kerr.nearme.account.PhoneNumber;
import com.kerr.nearme.billing.BillableQueue;
import com.kerr.nearme.billing.SmsBillable;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by allankerr on 2017-01-10.
 */
@Api(name = "sms", version = "v1",
        title = "SMS API",
        description = "API for handling the sending of SMS messages.")
public class SmsApi {

    @ApiMethod(name = "send.sms", path = "send/sms", authenticators = {FirebaseAuthenticator.class})
    public void sendSmsMessage(User user, SmsMessage message) throws UnauthorizedException, TwilioRestException {
        if (user == null) {
            throw new UnauthorizedException("Sending SMS messages requires authorization.");
        }
        TwilioRestClient client = new TwilioRestClient(ApiKeys.TWILIO_ACCOUNT_SID, ApiKeys.TWILIO_AUTH_TOKEN);
        MessageFactory factory = client.getAccount().getMessageFactory();

        AccountDao accountDao = new AccountDao();
        Account account = accountDao.load(user.getUserId());
        PhoneNumber outgoingNumber = account.getOutgoingNumber();

        List<NameValuePair> content = new LinkedList<NameValuePair>();
        content.add(new BasicNameValuePair(SmsParameters.TO, message.getPhoneNumber()));
        content.add(new BasicNameValuePair(SmsParameters.FROM, outgoingNumber.getPhoneNumber()));
        content.add(new BasicNameValuePair(SmsParameters.BODY, message.getBody()));
        Message sent = factory.create(content);

        Key<Account> accountKey = Key.create(Account.class, user.getUserId());
        BillableQueue.push(new SmsBillable(accountKey, sent.getSid()));
    }
}
