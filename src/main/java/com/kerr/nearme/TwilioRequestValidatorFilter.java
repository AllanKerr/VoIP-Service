package com.kerr.nearme;

import com.twilio.security.RequestValidator;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by allankerr on 2017-01-03.
 */
public class TwilioRequestValidatorFilter implements Filter {

    private RequestValidator requestValidator;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        requestValidator = new RequestValidator(ApiKeys.TWILIO_AUTH_TOKEN);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        boolean isValidRequest = false;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            TwilioRequestValidator validator = new TwilioRequestValidator(requestValidator);
            isValidRequest = validator.validate(httpRequest);
        }
        if(isValidRequest) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse)response).sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    public void destroy() {
        // Nothing to do
    }
}
