package org.rockhill.songster.web.service;

import org.rockhill.songster.database.tables.Social;

/**
 * Google type of AuthenticatedUser.
 */
public class GoogleUser extends AuthenticatedUser {

    /**
     * Creates a Google User login class.
     *
     * @param social              is the associated Social login record.
     * @param sessionTimeoutInSec determines the validity of he session
     */
    public GoogleUser(Social social, Integer sessionTimeoutInSec) {
        super("Google", social, sessionTimeoutInSec);
    }

}
