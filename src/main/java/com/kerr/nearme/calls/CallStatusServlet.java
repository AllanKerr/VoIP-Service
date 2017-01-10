package com.kerr.nearme.calls;

import com.googlecode.objectify.Key;
import com.kerr.nearme.account.Account;
import com.kerr.nearme.billing.BillableQueue;
import com.kerr.nearme.billing.CallBillable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by allankerr on 2017-01-08.
 */
public class CallStatusServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(CallStatusServlet.class.getName());

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String to = req.getParameter(CallParameters.TO);
        String from = req.getParameter(CallParameters.FROM);

        CallParticipant fromParticipant = CallParticipantFactory.create(from);
        CallParticipant toParticipant = CallParticipantFactory.create(to);

        logger.info("From: " + fromParticipant);
        logger.info("To: " + toParticipant);

        Key<Account> accountKey;
        if (fromParticipant.isBillable()) {
            accountKey = Key.create(Account.class, fromParticipant.getId());
        } else if (toParticipant.isBillable()) {
            accountKey = Key.create(Account.class, toParticipant.getId());
        } else {
            throw new ServletException("Both call participants were not billable.");
        }
        String callSid = req.getParameter(CallParameters.CALL_SID);
        BillableQueue.push(new CallBillable(accountKey, callSid));
    }
}
