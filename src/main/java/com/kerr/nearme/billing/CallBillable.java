package com.kerr.nearme.billing;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Subclass;
import com.kerr.nearme.ApiKeys;
import com.kerr.nearme.account.Account;
import com.kerr.twilio.calls.Call;
import com.kerr.twilio.calls.CallRequest;
import com.kerr.twilio.calls.CallsRequest;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by allankerr on 2017-01-09.
 */
@Subclass(index = true)
public class CallBillable extends Billable {

    private static final Logger logger = Logger.getLogger(CallBillable.class.getName());

    private static final String COMPLETED_STATUS = "completed";

    public CallBillable(Key<Account> accountRef, String callSid) {
        super(accountRef, callSid);
    }

    @Override
    public void process() {
        logger.fine("Attempting to record transaction");
        if (!isPending()) {
            logger.warning("Attempted to record transaction that has already recorded.");
            return;
        }
        try {
            Call parentCall = new CallRequest(ApiKeys.TWILIO_ACCOUNT_SID, ApiKeys.TWILIO_AUTH_TOKEN, billableSid).fetchResponse();
            String billableStatus = parentCall.getStatus();
            if (!billableStatus.equals(COMPLETED_STATUS)) {
                throw new IllegalStateException("Unable to process billable due to unexpected status: " + billableStatus);
            }
            List<Call> childCalls = new CallsRequest(ApiKeys.TWILIO_ACCOUNT_SID, ApiKeys.TWILIO_AUTH_TOKEN)
                    .setParentCallSid(billableSid)
                    .fetchResponse();
            Double price = Double.parseDouble(parentCall.getPrice());
            for (Call call : childCalls) {
                // Child calls may not have a price if there was no answer.
                if (call.getPrice() != null) {
                    price += Double.parseDouble(call.getPrice());
                }
            }
            finishedProcessing(price);
        } catch (IOException e) {
            // Handled silently with a runtime exception causing the queue to retry processing.
            throw new RuntimeException(e);
        }
    }
}
