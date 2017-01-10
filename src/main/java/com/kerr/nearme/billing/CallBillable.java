package com.kerr.nearme.billing;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Subclass;
import com.kerr.nearme.ApiKeys;
import com.kerr.nearme.account.Account;
import com.kerr.twilio.calls.Call;
import com.kerr.twilio.calls.CallRequest;
import com.kerr.twilio.calls.CallsRequest;

import java.io.IOException;
import java.util.LinkedList;
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
    protected List<String> getBillableStatuses() {
        List<String> statues = new LinkedList<String>();
        statues.add("completed");
        return statues;
    }

    @Override
    protected List<String> getNonBillableStatuses() {
        List<String> statues = new LinkedList<String>();
        statues.add("no-answer");
        statues.add("busy");
        statues.add("failed");
        statues.add("canceled");
        return statues;
    }

    @Override
    public void process() {
        logger.info("Attempting to record transaction");
        try {
            Call parentCall = new CallRequest(ApiKeys.TWILIO_ACCOUNT_SID, ApiKeys.TWILIO_AUTH_TOKEN, billableSid).fetchResponse();
            List<Call> childCalls = new CallsRequest(ApiKeys.TWILIO_ACCOUNT_SID, ApiKeys.TWILIO_AUTH_TOKEN)
                    .setParentCallSid(billableSid)
                    .fetchResponse();

            Double price = 0.00;
            if (isBillable(parentCall.getStatus())) {
                price += Double.parseDouble(parentCall.getPrice());
            } else {
                logger.info("Processed non-billable parent call " + parentCall.getSid());
            }
            for (Call call : childCalls) {
                if (isBillable(call.getStatus())) {
                    price += Double.parseDouble(call.getPrice());
                } else {
                    logger.info("Processed non-billable child call " + call.getSid());
                }
            }
            if (Math.abs(price) > 0) {
                finishedProcessing(price);
            }
        } catch (IOException e) {
            // Handled silently with a runtime exception causing the queue to retry processing.
            throw new RuntimeException(e);
        }
    }
}
