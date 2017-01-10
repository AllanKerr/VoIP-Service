package com.kerr.nearme.calls;

import com.twilio.sdk.verbs.Dial;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by allankerr on 2017-01-02.
 */
class CallServlet extends HttpServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String to = req.getParameter(CallServletParameters.TO);
        String from = req.getParameter(CallServletParameters.FROM);

        CallParticipant toParticipant = CallParticipantFactory.create(to);
        CallParticipant fromParticipant = CallParticipantFactory.create(from);

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

