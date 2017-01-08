package com.kerr.nearme.billing;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.condition.IfTrue;
import com.kerr.nearme.account.Account;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by allankerr on 2017-01-08.
 */
@Entity
abstract class Billable {

    private static final Logger logger = Logger.getLogger(Billable.class.getName());
    /**
     * Determines whether or not the billable has been applied to the account's credit balance
     */
    @Index(IfTrue.class)
    protected boolean isPending = true;
    /**
     * The account that incurred the billable
     */
    @Parent
    protected Account account;
    /**
     * The date and time the billable was created.
     */
    @Index
    protected Date creationDate;
    /**
     * The date and time the billable was recorded.
     */
    @Index
    protected Date recordDate;
    /**
     * The price of the billable to be set when it is recorded.
     */
    protected Double price;
    /**
     * The SID for uniquely identifying the billable
     */
    protected String billableSid;
    @Id
    private Long id;

    private Billable() {
    }

    protected Billable(Account account, String billableSid) {
        this.account = account;
        this.billableSid = billableSid;
        this.creationDate = new Date();
    }

    /**
     * Attempts to deduct the price of the billable from the account's credit balance.
     * Subclasses must determine how much to deduct and set is pending to true once
     * the balance has been accounted for.
     */
    public abstract void recordTransaction();

    /**
     * Update the transaction to reflect that it has been recorded at a particular price.
     *
     * @param price The price of the transaction.
     */
    protected void recordingSucceeded(Double price) {
        this.price = price;
        this.recordDate = new Date();
        this.isPending = false;

        // TODO decrement account credits

        new BillableDao().save(this);
        logger.info("Successfully recorded transaction" + billableSid + " for " + account.getUserId());
    }

    protected void recordingFailed() {
        new BillableDao().save(this);
        logger.info("Failed to record transaction " + billableSid);
    }
}
