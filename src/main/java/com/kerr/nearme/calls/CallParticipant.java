package com.kerr.nearme.calls;

import com.twilio.sdk.verbs.Verb;

/**
 * Created by allankerr on 2017-01-09.
 */
abstract class CallParticipant {

    /**
     * The participants identifier which may be the user ID
     * or phone number associated with the participant.
     */
    private String id;

    public CallParticipant(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass() + "{\n");
        builder.append("\tEndpoint = " + getEndpoint() + "\n");
        builder.append("\tCallerId = " + getCallerId() + "\n");
        builder.append("}");
        return builder.toString();
    }

    public abstract boolean isBillable();

    public abstract Verb getEndpoint();

    public abstract String getCallerId();
}
