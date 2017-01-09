package com.kerr.nearme;

import com.kerr.nearme.account.Account;
import com.kerr.nearme.account.AccoutDao;
import com.kerr.nearme.account.PhoneNumber;
import com.twilio.sdk.verbs.Client;
import com.twilio.sdk.verbs.Dial;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by allankerr on 2017-01-02.
 */
public class CallServlet extends HttpServlet {

    private static final String DIRECTION_PARAM = "Direction";

    private static final String DIRECTION_INBOUND = "inbound";
    private static final String DIRECTION_OUTBOUND = "outbound";

    private static final Logger logger = Logger.getLogger(FirebaseTokenVerifier.class.getName());

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TwiMLResponse twiml;
        String direction = req.getParameter(DIRECTION_PARAM);
        if (direction.equals(DIRECTION_INBOUND)) {
            twiml = incomingCallResponse(req);
        } else if (direction.equals(DIRECTION_OUTBOUND)) {
            twiml = outgoingCallResponse(req);
        } else {
            // TODO Error response
            twiml = null;
        }
        resp.setContentType("application/xml");
        resp.getWriter().print(twiml.toXML());
    }

    private TwiMLResponse incomingCallResponse(HttpServletRequest req) {

        String number = req.getParameter("Called");
        PhoneNumber phoneNumber = new PhoneNumber(number);
        Account account = new AccoutDao().load(phoneNumber);

        TwiMLResponse twiml = new TwiMLResponse();
        Dial dial = new Dial();
        try {
            // Hard coded until phone numbers can be associated with users
            dial.append(new Client(account.getUserId()));
            twiml.append(dial);
        } catch (TwiMLException e) {

        }
        return twiml;
    }

    private TwiMLResponse outgoingCallResponse(HttpServletRequest req) {

        String clientParam = req.getParameter("From");
        String userId = clientParam.split(":")[1];
        Account account = new AccoutDao().load(userId);

        TwiMLResponse twiml = new TwiMLResponse();
        Dial dial = new Dial(req.getParameter("To"));
        dial.setCallerId(account.getOutgoingNumber().getPhoneNumber());
        try {
            twiml.append(dial);
        } catch (TwiMLException e) {

        }
        return twiml;
    }
}

