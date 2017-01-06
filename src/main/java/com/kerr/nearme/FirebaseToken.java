package com.kerr.nearme;

/**
 * Created by allankerr on 2017-01-06.
 */
public class FirebaseToken {

    private String uid;

    private String email;

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public FirebaseToken(String uid, String email) {
        this.uid = uid;
        this.email = email;
    }
}
