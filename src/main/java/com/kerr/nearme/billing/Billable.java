package com.kerr.nearme.billing;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.condition.IfTrue;
import com.kerr.nearme.account.Account;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by allankerr on 2017-01-08.
 */
@Entity
abstract class Billable implements Serializable {

    private static final Logger logger = Logger.getLogger(Billable.class.getName());
    /**
     * Determines whether or not the billable has been applied to the account's credit balance
     */
    @Index(IfTrue.class)
    private boolean isPending = true;
    /**
     * The account that incurred the billable
     */
    @Parent
    private Ref<Account> accountRef;

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
    /**
     * The SID for uniquely identifying the billable
     */
    protected String billableSid;

    protected boolean isPending() {
        return isPending;
    }

    protected boolean hasPrice() {
        return price != null;
    }

    protected Double getPrice() {
        return price;
    }

    @Id
    private Long id;

    private Billable() {
    }

    protected Billable(Ref<Account> account, String billableSid) {
        this.accountRef = account;
        this.billableSid = billableSid;
        this.creationDate = new Date();
    }

    protected Billable(Ref<Account> account, String billableSid, Double price) {
        this(account, billableSid);
        this.price = price;
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

        Account account = accountRef.get();

        this.price = price;
        this.recordDate = new Date();
        this.isPending = false;

        // TODO decrement account credits
        logger.info("Successfully recorded transaction" + billableSid + " for ");
    }
}
