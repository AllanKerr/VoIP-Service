package com.kerr.nearme;

import com.twilio.security.RequestValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by allankerr on 2017-01-03.
 */
public class TwilioRequestValidator {

    private static final String SIGNATURE = "X-Twilio-Signature";

    private final RequestValidator requestValidator;

    public TwilioRequestValidator(RequestValidator requestValidator) {
        this.requestValidator = requestValidator;
    }

    public boolean validate(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        Map<String, String> params = extractParametersFrom(request);

        String signature = request.getHeader(SIGNATURE);

        return requestValidator.validate(url, params, signature);
    }

    private Map<String, String> extractParametersFrom(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();

        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String currentName = names.nextElement();
            params.put(currentName, request.getParameter(currentName));
        }
        return params;
    }
}
