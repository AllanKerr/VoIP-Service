package com.kerr.nearme.billing;

import com.googlecode.objectify.ObjectifyService;
import com.kerr.nearme.objectify.Dao;

/**
 * Created by allankerr on 2017-01-08.
 */
class BillableDao extends Dao<Billable> {

    static {
        ObjectifyService.register(Billable.class);
        ObjectifyService.register(SmsBillable.class);
        ObjectifyService.register(CallBillable.class);
    }

    BillableDao() {
        super(Billable.class);
    }
}
