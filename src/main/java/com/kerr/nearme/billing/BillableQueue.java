package com.kerr.nearme.billing;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;

/**
 * Created by allankerr on 2017-01-08.
 */
public final class BillableQueue {

    private static final long DELAY_MILLIS = 5000;

    private static final Queue queue = QueueFactory.getQueue("billing-queue");

    /**
     * Immediately record the billable by applying the price to the user's accounts
     * @precond billable.hasPrice() == true
     * @param billable The billable to be processed immediately.
     */
    public static void process(Billable billable) {
        if (!billable.hasPrice()) {
            throw new RuntimeException("Attempted to process a billable without a price.");
        }
        new BillableTask(billable).run();
    }

    /**
     * Push the billable to the queue to be applied to the user's account.
     * The billable will be processed after a delay to allow time for the
     * Twilio servers to update so that the price can be fetched.
     * @param billable The billable to be pushed to the queue for processing.
     */
    public static void push(Billable billable) {
        BillableTask task = new BillableTask(billable);
        long eta = System.currentTimeMillis() + DELAY_MILLIS;
        queue.add(TaskOptions.Builder.withPayload(task).etaMillis(eta));
    }

    private static class BillableTask implements DeferredTask {

        private Billable billable;

        public BillableTask(Billable billable) {
            this.billable = billable;
        }

        @Override
        public void run() {
            ObjectifyService.ofy().transact(new VoidWork() {
                @Override
                public void vrun() {
                    billable.process();
                    if (billable.getPrice() > 0) {
                        new BillableDao().save(billable);
                    }
                }
            });
        }
    }
}
