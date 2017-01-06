package com.kerr.nearme.account;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by allankerr on 2017-01-05.
 */
@Entity
public class Account {

    @Id
    private String userId;

    private Ref<PhoneNumber> outgoingNumber;

    @Index
    private List<Ref<PhoneNumber>> phoneNumbers = new ArrayList<Ref<PhoneNumber>>();

    public Account(String userId) {
        this.userId = userId;
    }
}
