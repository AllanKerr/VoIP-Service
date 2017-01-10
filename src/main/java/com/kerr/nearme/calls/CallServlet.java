package com.kerr.nearme;

import com.kerr.nearme.calls.CallParticipant;
import com.kerr.nearme.calls.CallParticipantFactory;
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

    private static final Logger logger = Logger.getLogger(CallServlet.class.getName());


    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String to = req.getParameter("To");
        String from = req.getParameter("From");

        CallParticipant toParticipant = CallParticipantFactory.create(to);
        CallParticipant fromParticipant = CallParticipantFactory.create(from);

        logger.info("From: " + fromParticipant.toString());
        logger.info("To: " + toParticipant.toString());

        TwiMLResponse twiml = new TwiMLResponse();
        Dial dial = new Dial();
        try {
            dial.setCallerId(fromParticipant.getCallerId());
            dial.append(toParticipant.getEndpoint());
            twiml.append(dial);
        } catch (TwiMLException e) {

        }
        resp.setContentType("application/xml");
        resp.getWriter().print(twiml.toXML());
    }
}

