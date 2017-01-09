package com.kerr.nearme.account;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.kerr.nearme.objectify.Dao;

/**
 * Created by allankerr on 2017-01-08.
 */
public class AccountDao extends Dao<Account> {

    static {
        ObjectifyService.register(Account.class);
        ObjectifyService.register(PhoneNumber.class);
    }

    public AccountDao() {
        super(Account.class);
    }

    @Override
    public Account load(String id) {
        Key<Account> key = loadKey(id);
        return ObjectifyService.ofy().load().key(key).now();
    }

    public Account load(PhoneNumber phoneNumber) {
        Key<Account> key = loadKey(phoneNumber);
        return ObjectifyService.ofy().load().key(key).now();
    }

    public Key<Account> loadKey(String id) {
        Key<Account> key = super.loadKey(id);
        if (key == null) {
            Account account = new Account(id);
            ObjectifyService.ofy().save().entity(account).now();
            key = Key.create(account);
        }
        return key;
    }

    public Key<Account> loadKey(PhoneNumber phoneNumber) {
        // TODO Update based on whether the phone number is currently attached to the account.
        return ObjectifyService.ofy().load().type(Account.class).filter("phoneNumbers =", phoneNumber).keys().first().now();
    }
}
