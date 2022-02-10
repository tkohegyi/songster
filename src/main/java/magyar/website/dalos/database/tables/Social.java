package magyar.website.dalos.database.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Descriptor class for Database table: Social.
 * Technically: Record of social (oauth2) login possibilities.
 */
@Entity
@Table(name = "dbo.social")
public class Social {

    private Long id;
    private Boolean isLoggedIn;
    private String lastLoginDateTime;
    private String googleEmail;
    private String googleUserName;
    private String googleUserId;
    private String googleUserPicture;
    private String facebookEmail;
    private String facebookUserName;
    private String facebookUserId;
    private String githubEmail;
    private String githubUserName;
    private String githubUserId;
    private String githubUserPicture;
    private String comment;

    /**
     * General constructor, used by Hibernate.
     * Shall be used only when a new record is created - then fields need to be filled of course before saving it to the database.
     */
    public Social() {
        // this form used by Hibernate
    }

    @Column(name = "id", nullable = false)
    @Id
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "isLoggedIn", nullable = false)
    public Boolean getIsLoggedIn() {
        return isLoggedIn;
    }
    public void setIsLoggedIn(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    /**
     * Gets googleEmail field of a Social record, ensures that it never will have null value.
     *
     * @return with the googleEmail field value or with an empty string.
     */
    @Column(name = "googleEmail", nullable = true)
    public String getGoogleEmail() {
        if (googleEmail != null) {
            return googleEmail;
        } else {
            return "";
        }
    }

    public void setGoogleEmail(String googleEmail) {
        this.googleEmail = googleEmail;
    }

    /**
     * Gets googleUserName field of a Social record, ensures that it never will have null value.
     *
     * @return with the googleUserName field value or with an empty string.
     */
    @Column(name = "googleUserName", nullable = true)
    public String getGoogleUserName() {
        if (googleUserName != null) {
            return googleUserName;
        } else {
            return "";
        }
    }

    public void setGoogleUserName(String googleUserName) {
        this.googleUserName = googleUserName;
    }

    /**
     * Gets googleUserId field of a Social record, ensures that it never will have null value.
     *
     * @return with the googleUserId field value or with an empty string.
     */
    @Column(name = "googleUserId", nullable = true)
    public String getGoogleUserId() {
        if (googleUserId != null) {
            return googleUserId;
        } else {
            return "";
        }
    }

    public void setGoogleUserId(String googleUserId) {
        this.googleUserId = googleUserId;
    }

    /**
     * Gets googleUserPicture field of a Social record, ensures that it never will have null value.
     *
     * @return with the googleUserPicture field value or with an empty string.
     */
    @Column(name = "googleUserPicture", nullable = true)
    public String getGoogleUserPicture() {
        if (googleUserPicture != null) {
            return googleUserPicture;
        } else {
            return "";
        }
    }

    public void setGoogleUserPicture(String googleUserPicture) {
        this.googleUserPicture = googleUserPicture;
    }

    /**
     * Gets facebookEmail field of a Social record, ensures that it never will have null value.
     *
     * @return with the facebookEmail field value or with an empty string.
     */
    @Column(name = "facebookEmail", nullable = true)
    public String getFacebookEmail() {
        if (facebookEmail != null) {
            return facebookEmail;
        } else {
            return "";
        }
    }

    public void setFacebookEmail(String facebookEmail) {
        this.facebookEmail = facebookEmail;
    }

    /**
     * Gets facebookUserName field of a Social record, ensures that it never will have null value.
     *
     * @return with the facebookUserName field value or with an empty string.
     */
    @Column(name = "facebookUserName", nullable = true)
    public String getFacebookUserName() {
        if (facebookUserName != null) {
            return facebookUserName;
        } else {
            return "";
        }
    }

    public void setFacebookUserName(String facebookUserName) {
        this.facebookUserName = facebookUserName;
    }

    /**
     * Gets facebookUserId field of a Social record, ensures that it never will have null value.
     *
     * @return with the facebookUserId field value or with an empty string.
     */
    @Column(name = "facebookUserId", nullable = true)
    public String getFacebookUserId() {
        if (facebookUserId != null) {
            return facebookUserId;
        } else {
            return "";
        }
    }

    public void setFacebookUserId(String facebookUserId) {
        this.facebookUserId = facebookUserId;
    }

    /**
     * Gets comment field of a Social record, ensures that it never will have null value.
     *
     * @return with the comment field value or with an empty string.
     */
    @Column(name = "comment", nullable = true)
    public String getComment() {
        if (comment != null) {
            return comment;
        } else {
            return "";
        }
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
