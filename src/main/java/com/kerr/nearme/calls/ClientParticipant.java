package com.kerr.nearme.calls;

import com.kerr.nearme.account.Account;
import com.twilio.sdk.verbs.Client;
import com.twilio.sdk.verbs.Verb;

import java.util.logging.Logger;

/**
 * Created by allankerr on 2017-01-09.
 */
class ClientParticipant extends CallParticipant {

    private static final Logger logger = Logger.getLogger(ClientParticipant.class.getName());

    private Account account;

    ClientParticipant(Account account) {
        if (account == null) {
            throw new NullPointerException();
        }
        this.account = account;
    }

    @Override
    public Verb getEndpoint() {
        logger.info("Account: " + account);
        logger.info(account.getUserId());
        logger.info(new Client(account.getUserId()).toString());

        return new Client(account.getUserId());
    }

    @Override
    public String getCallerId() {
        return account.getOutgoingNumber().getPhoneNumber();
    }
}
