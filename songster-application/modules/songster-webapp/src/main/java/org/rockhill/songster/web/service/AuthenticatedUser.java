package org.rockhill.songster.web.service;

import org.rockhill.songster.database.tables.Social;

/**
 * Base class of an authenticated user.
 */
public class AuthenticatedUser {

    private static final long ONE_SEC = 1000L;
    private final long sessionTimeoutExtender;
    private final String serviceName;
    private Social social;
    private long sessionTimeout;

    /**
     * initializes the object.
     *
     * @param serviceName         is the name of the Oath2 service used for authentication
     * @param social              is the Social login record used for the identification
     * @param sessionTimeoutInSec time period of the session
     */
    public AuthenticatedUser(String serviceName, Social social, Integer sessionTimeoutInSec) {
        this.serviceName = serviceName;
        this.social = social;
        this.sessionTimeoutExtender = (long) sessionTimeoutInSec * ONE_SEC;
        extendSessionTimeout();
    }

    public Social getSocial() {
        return social;
    }

    public void setSocial(Social social) {
        this.social = social;
    }

    /**
     * Extends the session validity with a preconfigured additional time period.
     */
    public void extendSessionTimeout() {
        this.sessionTimeout = System.currentTimeMillis() + sessionTimeoutExtender;
    }

    public boolean isSessionValid() {
        return this.sessionTimeout > System.currentTimeMillis();
    }

    public String getServiceName() {
        return serviceName;
    }
}
