package com.kerr.nearme;

import com.twilio.sdk.verbs.Dial;
import com.twilio.sdk.verbs.TwiMLResponse;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.Client;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by allankerr on 2017-01-02.
 */
public class ReceiveCallServlet extends HttpServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TwiMLResponse twiml = new TwiMLResponse();
        Dial dial = new Dial();
        try {
            // Hard coded until phone numbers can be associated with users
            dial.append(new Client("ZYh1BJU0QyWbVw1vDgQshDKelCP2"));
            twiml.append(dial);
        } catch (TwiMLException e) {

        }
        resp.setContentType("application/xml");
        resp.getWriter().print(twiml.toXML());
    }
}

