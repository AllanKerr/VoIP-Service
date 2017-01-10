package com.kerr.nearme.calls;

import com.kerr.nearme.account.Account;
import com.kerr.nearme.account.AccountDao;
import com.kerr.nearme.account.PhoneNumber;

/**
 * Created by allankerr on 2017-01-09.
 */
final class CallParticipantFactory {

    private static final String CLIENT_PREFIX = "client";

    private static final String PHONE_NUMBER_PREFIX = "+";

    public static CallParticipant create(String target) {
        AccountDao dao = new AccountDao();
        if (target.startsWith(CLIENT_PREFIX)) {

            // Split from format "client:userId"
            String userId = target.split(":")[1];
            Account account = dao.load(userId);
            return new ClientParticipant(account);

        } else if (target.startsWith(PHONE_NUMBER_PREFIX)) {

            // Determine if the phone number is associated with a user
            PhoneNumber phoneNumber = new PhoneNumber(target);
            Account account = new AccountDao().load(phoneNumber);
            if (account == null) {
                return new PhoneNumberParticipant(phoneNumber);
            } else {
                return new ClientParticipant(account);
            }
        } else {
            throw new RuntimeException("Received unexpected call target: " + target);
        }
    }
}
