package com.kerr.nearme.account;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by allankerr on 2017-01-05.
 */
@Entity
public class PhoneNumber {

    @Id
    private String phoneNumber;

    public PhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
