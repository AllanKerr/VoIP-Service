package com.kerr.nearme;

import com.twilio.sdk.verbs.Message;
import com.twilio.sdk.verbs.TwiMLException;
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

    private static final String SIGNATURE = "X-Twilio-Signature";

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {

        String fromNumber = request.getParameter("From");
        String toNumber = request.getParameter("To");
        String body = request.getParameter("Body");
        String message = String.format("Hello, %s, you said %s to %s.", fromNumber, body, toNumber);

        TwiMLResponse twiml = new TwiMLResponse();
        Message sms = new Message(message);
        try {
            twiml.append(sms);
        } catch (TwiMLException e) {
            throw new ServletException("Twilio error", e);
        }
        response.setContentType("application/xml");
        response.getWriter().print(twiml.toXML());
    }
}
