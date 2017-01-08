package com.kerr.nearme.account;

import com.googlecode.objectify.ObjectifyService;
import com.kerr.nearme.objectify.Dao;

/**
 * Created by allankerr on 2017-01-08.
 */
public class AccoutDao extends Dao<Account> {

    static {
        ObjectifyService.register(Account.class);
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
}
