package org.rockhill.songster.database.tables;

import org.rockhill.songster.database.business.helper.enums.SocialStatusTypes;
import org.rockhill.songster.database.exception.DatabaseHandlingException;
import org.rockhill.songster.database.tables.helper.TableBase;

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
public class Social extends TableBase {

    private Long id;
    private Integer socialStatus;
    private String googleEmail;
    private String googleUserName;
    private String googleUserId;
    private String googleUserPicture;
    private String facebookEmail;
    private String facebookUserName;
    private String facebookUserId;
    private String facebookFirstName;
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

    @Column(name = "socialStatus", nullable = false)
    public Integer getSocialStatus() {
        return socialStatus;
    }

    /**
     * Sets the id of the social status of the social user.
     *
     * @param socialStatus is the status id
     * @throws DatabaseHandlingException if the id is not valid
     */
    public void setSocialStatus(Integer socialStatus) {
        SocialStatusTypes.getTypeFromId(socialStatus); //validation
        this.socialStatus = socialStatus;
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
            return EMPTY_STRING;
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
            return EMPTY_STRING;
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
            return EMPTY_STRING;
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
            return EMPTY_STRING;
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
            return EMPTY_STRING;
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
            return EMPTY_STRING;
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
            return EMPTY_STRING;
        }
    }

    public void setFacebookUserId(String facebookUserId) {
        this.facebookUserId = facebookUserId;
    }

    /**
     * Gets facebookFirstName field of a Social record, ensures that it never will have null value.
     *
     * @return with the facebookFirstName field value or with an empty string.
     */
    @Column(name = "facebookFirstName", nullable = true)
    public String getFacebookFirstName() {
        if (facebookFirstName != null) {
            return facebookFirstName;
        } else {
            return EMPTY_STRING;
        }
    }

    public void setFacebookFirstName(String facebookFirstName) {
        this.facebookFirstName = facebookFirstName;
    }

    /**
     * Gets comment field of a Social record, ensures that it never will have null value.
     *
     * @return with the comment field value or with an empty string.
     */
    @Column(name = "adminComment", nullable = true)
    public String getComment() {
        if (comment != null) {
            return comment;
        } else {
            return EMPTY_STRING;
        }
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
