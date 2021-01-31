package org.rockhill.songster.web.service;

import org.rockhill.songster.database.tables.Social;

/**
 * Facebook type of AuthenticatedUser.
 */
public class FacebookUser extends AuthenticatedUser {

    /**
     * Creates a Facebook User login class.
     *
     * @param social              is the associated Social login record.
     * @param sessionTimeoutInSec determines the validity of he session
     */
    public FacebookUser(Social social, Integer sessionTimeoutInSec) {
        super("Facebook", social, sessionTimeoutInSec);
    }

}
