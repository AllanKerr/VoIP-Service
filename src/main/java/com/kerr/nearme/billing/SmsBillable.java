package com.kerr.nearme.billing;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Subclass;
import com.kerr.nearme.ApiKeys;
import com.kerr.nearme.account.Account;
import com.kerr.twilio.messages.Message;
import com.kerr.twilio.messages.MessageRequest;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by allankerr on 2017-01-08.
 */
@Subclass(index=true)
public class SmsBillable extends Billable {

    private static final Logger logger = Logger.getLogger(SmsBillable.class.getName());

    public SmsBillable(Ref<Account> account, String smsSid) {
        super(account, smsSid);
    }

    public SmsBillable(Ref<Account> account, String smsSid, Double price) {
        super(account, smsSid, price);
    }

    @Override
    public void process() {
        logger.fine("Attempting to record transaction");
        if (!isPending()) {
            // Ensure billables are only recorded once.
            logger.warning("Attempted to record transaction that has already recorded.");
            return;
        }
        Double price;
        if (hasPrice()) {
            price = getPrice();
        } else {
            try {
                Message message = new MessageRequest(ApiKeys.TWILIO_ACCOUNT_SID, ApiKeys.TWILIO_AUTH_TOKEN, billableSid).fetchResponse();
                price = Double.parseDouble(message.getPrice());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        finishedProcessing(price);
    }
}
