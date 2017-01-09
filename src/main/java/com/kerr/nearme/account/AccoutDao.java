package com.kerr.nearme.account;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.kerr.nearme.objectify.Dao;

/**
 * Created by allankerr on 2017-01-08.
 */
public class AccoutDao extends Dao<Account> {

    static {
        ObjectifyService.register(Account.class);
        ObjectifyService.register(PhoneNumber.class);
    }

    public AccoutDao() {
        super(Account.class);
    }

    @Override
    public Account load(String id) {
        Account account = super.load(id);
        if (account == null) {
            account = new Account(id);
            ObjectifyService.ofy().save().entity(account).now();
        }
        return account;
    }

    public Account load(PhoneNumber phoneNumber) {
        Key<Account> key = loadKey(phoneNumber);
        return ObjectifyService.ofy().load().key(key).now();
    }

    public Key<Account> loadKey(PhoneNumber phoneNumber) {
        // TODO Update based on whether the phone number is currently attached to the account.
        return ObjectifyService.ofy().load().type(Account.class).filter("phoneNumbers =", phoneNumber).keys().first().now();
    }
}
