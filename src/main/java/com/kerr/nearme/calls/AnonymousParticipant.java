package com.kerr.nearme.calls;

import com.twilio.sdk.verbs.Verb;

/**
 * Created by allankerr on 2017-01-09.
 */
class AnonymousParticipant extends CallParticipant {

    AnonymousParticipant() {
        super(null);
    }

    @Override
    public boolean isBillable() {
        return false;
    }

    @Override
    public Verb getEndpoint() {
        return null;
    }

    @Override
    public String getCallerId() {
        return null;
    }
}
