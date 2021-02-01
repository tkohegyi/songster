package org.rockhill.songster.web.json;

import org.rockhill.songster.database.tables.Social;
import org.rockhill.songster.helper.JsonField;

/**
 * Json structure to hold information about the actual user.
 */
public class CurrentUserInformationJson {
    @JsonField
    public boolean isLoggedIn;
    @JsonField
    public Long socialId;
    @JsonField
    public String socialEmail;
    @JsonField
    public String loggedInUserName;
    @JsonField
    public boolean isAdmin;
    @JsonField
    public boolean isPrivilegedUser;
    @JsonField
    public boolean isRegisteredUser;
    @JsonField
    public boolean isGuest;
    @JsonField
    public String socialServiceUsed;

    /**
     * Constructor - fills the json structure with default values.
     */
    public CurrentUserInformationJson() {
        reset();
    }

    /**
     * Fill the json structure with basic and default (user not logged in) information.
     */
    public void reset() {
        socialId = null;
        socialEmail = "";
        isLoggedIn = false;
        loggedInUserName = "Anonymous";
        isAdmin = false;
        isPrivilegedUser = false;
        isRegisteredUser = false;
        isGuest = false;
        socialServiceUsed = "Undetermined";
    }

    /**
     * Fills json fields from the Social data.
     *
     * @param social is the Social data
     */
    public void fillIdentifiedSocialFields(Social social) {
        socialId = social.getId();
        String email = social.getGoogleEmail();
        if (email.length() == 0) {
            email = social.getFacebookEmail();
        }
        socialEmail = email;
        Integer userLevel = social.getSocialStatus();
        isGuest = userLevel > 1;
        isRegisteredUser = userLevel > 2;
        isPrivilegedUser = userLevel > 3;
        isAdmin = userLevel > 4;
    }
}
