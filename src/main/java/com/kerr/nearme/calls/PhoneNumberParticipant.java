package com.kerr.nearme.calls;

import com.kerr.nearme.account.PhoneNumber;
import com.twilio.sdk.verbs.Number;
import com.twilio.sdk.verbs.Verb;

/**
 * Created by allankerr on 2017-01-09.
 */
class PhoneNumberParticipant extends CallParticipant {

    private PhoneNumber number;

    PhoneNumberParticipant(PhoneNumber number) {
        if (number == null) {
            throw new NullPointerException();
        }
        this.number = number;
    }

    @Override
    public Verb getEndpoint() {
        return new Number(number.getPhoneNumber());
    }

    @Override
    public String getCallerId() {
        return number.getPhoneNumber();
    }
}
