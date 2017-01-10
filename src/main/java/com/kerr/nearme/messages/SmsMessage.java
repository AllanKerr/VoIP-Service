package com.kerr.nearme.messages;

/**
 * Created by allankerr on 2017-01-03.
 */
class SmsMessage {

    private String phoneNumber;

    private String body;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBody() {
        return body;
    }

    public SmsMessage(String phoneNumber, String body) {
        this.phoneNumber = phoneNumber;
        this.body = body;
    }

    private SmsMessage() {

    }
}
