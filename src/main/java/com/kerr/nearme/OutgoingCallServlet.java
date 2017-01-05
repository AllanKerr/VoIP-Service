package com.kerr.nearme;

import com.twilio.sdk.verbs.Dial;
import com.twilio.sdk.verbs.Say;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by allankerr on 2017-01-03.
 */
public class OutgoingCallServlet extends HttpServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TwiMLResponse twiml = new TwiMLResponse();
        Dial dial = new Dial(req.getParameter("To"));
        dial.setCallerId("13067007600");
        try {
            twiml.append(dial);
        } catch (TwiMLException e) {

        }
        resp.setContentType("application/xml");
        resp.getWriter().print(twiml.toXML());
    }
}

