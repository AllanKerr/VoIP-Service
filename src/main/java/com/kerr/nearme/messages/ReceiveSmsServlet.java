package com.kerr.nearme.messages;

import com.googlecode.objectify.Key;
import com.kerr.nearme.account.Account;
import com.kerr.nearme.account.AccountDao;
import com.kerr.nearme.account.PhoneNumber;
import com.kerr.nearme.billing.BillableQueue;
import com.kerr.nearme.billing.SmsBillable;
import com.twilio.sdk.verbs.TwiMLResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by allankerr on 2017-01-02.
 */
public class ReceiveSmsServlet extends HttpServlet {

    private  static final String TO_PARAM = "To";
    private  static final String MESSAGE_SID_PARAM = "MessageSid";

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {

        String toNumber = request.getParameter(TO_PARAM);
        PhoneNumber toPhoneNumber = new PhoneNumber(toNumber);
        Key<Account> accountKey = new AccountDao().loadKey(toPhoneNumber);
        if (accountKey == null) {
            // TODO handle error when account can't be found
        }
        TwiMLResponse twiml = new TwiMLResponse();
        response.setContentType("application/xml");
        response.getWriter().print(twiml.toXML());

        String messageSid = request.getParameter(MESSAGE_SID_PARAM);
        BillableQueue.push(new SmsBillable(accountKey, messageSid));
    }
}
