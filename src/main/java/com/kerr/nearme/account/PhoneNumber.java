package com.kerr.nearme.account;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.kerr.nearme.Savable;

/**
 * Created by allankerr on 2017-01-05.
 */
@Entity
public class PhoneNumber implements Savable {

    @Id
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    private PhoneNumber() {}

    public PhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void save() {
        ObjectifyService.ofy().save().entity(this).now();
    }
}
