package com.kerr.nearme.account;

/**
 * Created by allankerr on 2017-01-03.
 */
public class Message {

    private String phoneNumber;

    private String body;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBody() {
        return body;
    }

    public Message(String phoneNumber, String body) {
        this.phoneNumber = phoneNumber;
        this.body = body;
    }

    private Message() {

    }
}
