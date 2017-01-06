package com.kerr.nearme;

import com.twilio.sdk.verbs.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by allankerr on 2017-01-02.
 */
public class CallServlet extends HttpServlet {

    private static final String DIRECTION_PARAM = "Direction";

    private static final String DIRECTION_INBOUND = "inbound";
    private static final String DIRECTION_OUTBOUND = "outbound";

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
        TwiMLResponse twiml = new TwiMLResponse();
        Dial dial = new Dial();
        try {
            // Hard coded until phone numbers can be associated with users
            dial.append(new Client("ZYh1BJU0QyWbVw1vDgQshDKelCP2"));
            twiml.append(dial);
        } catch (TwiMLException e) {

        }
        return twiml;
    }

    private TwiMLResponse outgoingCallResponse(HttpServletRequest req) {
        TwiMLResponse twiml = new TwiMLResponse();
        Dial dial = new Dial(req.getParameter("To"));
        dial.setCallerId("13067007600");
        try {
            twiml.append(dial);
        } catch (TwiMLException e) {

        }
        return twiml;
    }
}

