package org.rockhill.songster.web.provider;

import org.rockhill.songster.database.business.BusinessWithAuditTrail;
import org.rockhill.songster.database.tables.AuditTrail;
import org.rockhill.songster.database.tables.Social;
import org.rockhill.songster.web.json.CurrentUserInformationJson;
import org.rockhill.songster.web.service.AuthenticatedUser;
import org.rockhill.songster.web.service.FacebookUser;
import org.rockhill.songster.web.service.GoogleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * Class to provide information about the actual user.
 */
@Component
public class CurrentUserProvider {
    private static final String GUEST_NAME_INTRO = "Vend\u00e9g - ";

    @Autowired
    private BusinessWithAuditTrail businessWithAuditTrail;

    /**
     * Get information about the actual user.
     *
     * @param httpSession that the user have
     * @return with current user information in json
     */
    public CurrentUserInformationJson getUserInformation(HttpSession httpSession) {
        CurrentUserInformationJson currentUserInformationJson = new CurrentUserInformationJson(); //default info - user not logged in

        Authentication authentication = null;
        SecurityContext securityContext = (SecurityContext) httpSession.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        if (securityContext != null) {
            authentication = securityContext.getAuthentication();
        }
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof AuthenticatedUser) {
                AuthenticatedUser user = (AuthenticatedUser) principal;
                if (user.isSessionValid()) {
                    user.extendSessionTimeout();
                    currentUserInformationJson = getCurrentUserInformation(user);
                } else { //session expired!
                    securityContext.setAuthentication(null); // this cleans up the authentication data technically
                    httpSession.removeAttribute(SPRING_SECURITY_CONTEXT_KEY); // this clean up the session itself
                }
            }
        }
        return currentUserInformationJson;
    }

    private CurrentUserInformationJson getCurrentUserInformation(AuthenticatedUser user) {
        String loggedInUserName;
        String userName;
        Social social;
        CurrentUserInformationJson currentUserInformationJson = new CurrentUserInformationJson();
        currentUserInformationJson.isLoggedIn = true;  // if authentication is not null then the person is logged in
        userName = "Anonymous";
        loggedInUserName = GUEST_NAME_INTRO + userName;
        if (user instanceof GoogleUser) {
            userName = user.getSocial().getGoogleUserName();
            loggedInUserName = GUEST_NAME_INTRO + userName;
        }
        if (user instanceof FacebookUser) {
            userName = user.getSocial().getFacebookUserName();
            loggedInUserName = GUEST_NAME_INTRO + userName;
        }
        currentUserInformationJson.socialServiceUsed = user.getServiceName();
        currentUserInformationJson.loggedInUserName = loggedInUserName; //user who logged in via social
        social = user.getSocial();
        if (social != null) {
            currentUserInformationJson.fillIdentifiedSocialFields(social);
        }
        return currentUserInformationJson;
    }

    /**
     * Gets the name of the actual user.
     *
     * @param authentication is the authentication object
     * @return with the name
     */
    public String getQuickUserName(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String loggedInUserName = "";
        if (principal instanceof AuthenticatedUser) {
            AuthenticatedUser user = (AuthenticatedUser) principal;
            loggedInUserName = GUEST_NAME_INTRO + "Anonymous";
            if (principal instanceof GoogleUser) {
                loggedInUserName = GUEST_NAME_INTRO + user.getSocial().getGoogleUserName();
            }
            if (principal instanceof FacebookUser) {
                loggedInUserName = GUEST_NAME_INTRO + user.getSocial().getFacebookUserName();
            }
        }
        return loggedInUserName;
    }

    /**
     * Register login event in audit trail.
     *
     * @param httpSession       identifies the user
     * @param oauth2ServiceName identifies the used social service
     */
    public void registerLogin(HttpSession httpSession, final String oauth2ServiceName) {
        CurrentUserInformationJson currentUserInformationJson = getUserInformation(httpSession);
        String data = oauth2ServiceName;
        long socialId = 0;
        if (currentUserInformationJson.socialId != null) {
            socialId = currentUserInformationJson.socialId;
        } else {
            data = "Unidentified Social data.";
        }
        AuditTrail auditTrail = businessWithAuditTrail.prepareAuditTrail(socialId,
                currentUserInformationJson.loggedInUserName, "Login", "User logged in: " + currentUserInformationJson.loggedInUserName, data);
        businessWithAuditTrail.saveAuditTrailSafe(auditTrail);
    }

    /**
     * Register logout event in audit trail.
     *
     * @param httpSession identifies the user
     */
    public void registerLogout(HttpSession httpSession) {
        CurrentUserInformationJson currentUserInformationJson = getUserInformation(httpSession);
        String data = "";
        long socialId = 0;
        if (currentUserInformationJson.socialId != null) {
            socialId = currentUserInformationJson.socialId;
        } else {
            data = "Unidentified Social data.";
        }
        AuditTrail auditTrail = businessWithAuditTrail.prepareAuditTrail(socialId,
                currentUserInformationJson.loggedInUserName, "Logout", "User logged out: " + currentUserInformationJson.loggedInUserName, data);
        businessWithAuditTrail.saveAuditTrailSafe(auditTrail);
    }
}
