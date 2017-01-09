package com.kerr.nearme.account;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by allankerr on 2017-01-05.
 */
@Entity
public class PhoneNumber {

    private static final String UKNOWN_PHONE_NUMBER = "+266696687";
    @Id
    private String phoneNumber;

    private PhoneNumber() {}

    public PhoneNumber(String phoneNumber) {
        assert phoneNumber != null;
        this.phoneNumber = phoneNumber;
    }

    public boolean isUnknown() {
        return UKNOWN_PHONE_NUMBER.equals(phoneNumber);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
