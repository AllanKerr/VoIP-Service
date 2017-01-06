package com.kerr.nearme.account;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

/**
 * Created by allankerr on 2017-01-05.
 */
public class AccountClient {

    static {
        ObjectifyService.register(Account.class);
        ObjectifyService.register(PhoneNumber.class);
    }

    public Account getAccount(String userId) {
        Key<Account> key = Key.create(Account.class, userId);
        Account account = ObjectifyService.ofy().load().key(key).now();
        if (account == null) {
            account = new Account(userId);
            ObjectifyService.ofy().save().entity(account).now();
        }
        return account;
    }

    public Account getAccount(PhoneNumber phoneNumber) {
        return ObjectifyService.ofy().load().type(Account.class).filter("phoneNumbers =", phoneNumber).first().now();
    }
}
