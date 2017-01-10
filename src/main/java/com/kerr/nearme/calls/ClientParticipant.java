package com.kerr.nearme.calls;

import com.kerr.nearme.account.Account;
import com.twilio.sdk.verbs.Client;
import com.twilio.sdk.verbs.Verb;

/**
 * Created by allankerr on 2017-01-09.
 */
class ClientParticipant extends CallParticipant {

    private Account account;

    ClientParticipant(Account account) {
        super(account.getUserId());
        this.account = account;
    }

    @Override
    public boolean isBillable() {
        return true;
    }

    @Override
    public Verb getEndpoint() {
        return new Client(account.getUserId());
    }

    @Override
    public String getCallerId() {
        return account.getOutgoingNumber().getPhoneNumber();
    }
}
