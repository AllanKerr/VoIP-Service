package com.kerr.nearme.billing;

import com.googlecode.objectify.annotation.Subclass;
import com.kerr.nearme.APIKeys;
import com.kerr.nearme.account.Account;
import com.kerr.twilio.messages.Message;
import com.kerr.twilio.messages.MessageRequest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by allankerr on 2017-01-08.
 */
@Subclass
public class SmsBillable extends Billable {

    private static final Logger logger = Logger.getLogger(SmsBillable.class.getName());

    public SmsBillable(Account account, String smsSid) {
        super(account, smsSid);
    }

    @Override
    public void recordTransaction() {
        logger.fine("Attempting to record transaction");
        if (!isPending) {
            // Ensure billables are only recorded once.
            logger.warning("Attempted to record transaction that has already recorded.");
            return;
        }
        try {
            Message message = new MessageRequest(APIKeys.TWILIO_ACCOUNT_SID, APIKeys.TWILIO_AUTH_TOKEN, billableSid).fetchResponse();
            Double price = Double.parseDouble(message.getPrice());
            recordingSucceeded(price);
        } catch (IOException e) {
            recordingFailed();

            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }
}
