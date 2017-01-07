package com.kerr.nearme.account;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.kerr.nearme.Savable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by allankerr on 2017-01-05.
 */
@Entity
public class Account implements Savable {

    @Id
    private String userId;

    private Ref<PhoneNumber> outgoingNumber;

    @Index
    private List<Ref<PhoneNumber>> phoneNumbers = new ArrayList<Ref<PhoneNumber>>();

    public String getUserId() {
        return userId;
    }

    public PhoneNumber getOutgoingNumber() {
        return outgoingNumber.get();
    }

    public Account(String userId) {
        this.userId = userId;
    }

    private Account() {}

    public void addPhoneNumber(PhoneNumber number) {
        Ref<PhoneNumber> numberRef = Ref.create(number);
        if (outgoingNumber == null) {
            outgoingNumber = numberRef;
        }
        phoneNumbers.add(numberRef);
    }

    @Override
    public void save() {
        ObjectifyService.ofy().save().entity(this).now();
    }
}
