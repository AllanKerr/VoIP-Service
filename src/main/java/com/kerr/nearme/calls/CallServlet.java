package com.kerr.nearme.calls;

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

    private static final Logger logger = Logger.getLogger(CallStatusServlet.class.getName());

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String to = req.getParameter(CallServletParameters.TO);
        String from = req.getParameter(CallServletParameters.FROM);

        CallParticipant toParticipant = CallParticipantFactory.create(to);
        CallParticipant fromParticipant = CallParticipantFactory.create(from);

        logger.info("From: " + fromParticipant);
        logger.info("To: " + toParticipant);

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

