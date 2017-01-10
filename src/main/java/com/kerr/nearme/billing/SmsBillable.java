package com.kerr.nearme.billing;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Subclass;
import com.kerr.nearme.ApiKeys;
import com.kerr.nearme.account.Account;
import com.kerr.twilio.messages.Message;
import com.kerr.twilio.messages.MessageRequest;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by allankerr on 2017-01-08.
 */
@Subclass(index=true)
public class SmsBillable extends Billable {

    private static final Logger logger = Logger.getLogger(SmsBillable.class.getName());

    public SmsBillable(Key<Account> account, String smsSid) {
        super(account, smsSid);
    }

    @Override
    protected List<String> getBillableStatuses() {
        List<String> statues = new LinkedList<String>();
        statues.add("sent");
        statues.add("delivered");
        statues.add("undelivered");
        statues.add("received");
        return statues;
    }

    @Override
    protected List<String> getNonBillableStatuses() {
        List<String> statues = new LinkedList<String>();
        statues.add("failed");
        return statues;
    }

    @Override
    public void process() {
        logger.fine("Attempting to record transaction");
        try {
            Message message = new MessageRequest(ApiKeys.TWILIO_ACCOUNT_SID, ApiKeys.TWILIO_AUTH_TOKEN, billableSid).fetchResponse();
            if (isBillable(message.getStatus())) {
                Double price = Double.parseDouble(message.getPrice());
                finishedProcessing(price);
            } else {
                logger.info("Processed non-billable SMS " + message.getSid());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
