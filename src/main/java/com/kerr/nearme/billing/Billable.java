package com.kerr.nearme.billing;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.condition.IfTrue;
import com.kerr.nearme.account.Account;
import com.kerr.nearme.account.AccountDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by allankerr on 2017-01-08.
 */
@Entity
abstract class Billable implements Serializable {

    private static final Logger logger = Logger.getLogger(Billable.class.getName());
    /**
     * The SID for uniquely identifying the billable
     */
    protected String billableSid;
    /**
     * Determines whether or not the billable has been applied to the account's credit balance
     */
    @Index(IfTrue.class)
    private boolean isPending = true;
    /**
     * The account that incurred the billable
     */
    @Parent
    private Key<Account> accountKey;
    /**
     * The date and time the billable was created.
     */
    @Index
    private Date creationDate;
    /**
     * The date and time the billable was recorded.
     */
    @Index
    private Date recordDate;
    /**
     * The price of the billable to be set when it is recorded.
     */
    private Double price;
    @Id
    private Long id;

    private Billable() {
    }

    protected Billable(Key<Account> account, String billableSid) {
        this.accountKey = account;
        this.billableSid = billableSid;
        this.creationDate = new Date();
    }

    protected Billable(Key<Account> account, String billableSid, Double price) {
        this(account, billableSid);
        this.price = price;
    }

    protected boolean isPending() {
        return isPending;
    }

    protected boolean hasPrice() {
        return price != null;
    }

    protected Double getPrice() {
        return price;
    }

    protected List<String> getBillableStatuses() {
        return new ArrayList<String>();
    }

    protected List<String> getNonBillableStatuses() {
        return new ArrayList<String>();
    }

    protected boolean isBillable(String status) {
        if (getBillableStatuses().contains(status)) {
            return true;
        } else if (getNonBillableStatuses().contains(status)) {
            return false;
        } else {
            throw new IllegalStateException("Unable to process billable due to unexpected status: " + status);
        }
    }

    /**
     * Attempts to deduct the price of the billable from the account's credit balance.
     * Subclasses must determine how much to deduct and set is pending to true once
     * the balance has been accounted for.
     */
    public abstract void process();

    /**
     * Update the transaction to reflect that it has been recorded at a particular price.
     *
     * @param price The price of the transaction.
     */
    protected void finishedProcessing(Double price) {

        Account account = new AccountDao().load(accountKey);

        this.price = price;
        this.recordDate = new Date();
        this.isPending = false;

        // TODO decrement account credits
        logger.info("Successfully recorded transaction " + billableSid + " for " + account.getUserId() + " at cost " + price);
    }
}
