package com.kerr.nearme.calls;

import com.twilio.sdk.verbs.Verb;

/**
 * Created by allankerr on 2017-01-09.
 */
abstract class CallParticipant {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass() + "{\n");
        builder.append("\tEndpoint = " + getEndpoint() + "\n");
        builder.append("\tCallerId = " + getCallerId() + "\n");
        builder.append("}");
        return builder.toString();
    }

    public abstract Verb getEndpoint();

    public abstract String getCallerId();
}
