package magyar.website.dalos.database.tables;

import magyar.website.dalos.database.tables.help.TableSupport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

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
        return Objects.requireNonNullElse(googleEmail, TableSupport.EMPTY_STRING);
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
        return Objects.requireNonNullElse(googleUserName, TableSupport.EMPTY_STRING);
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
        return Objects.requireNonNullElse(googleUserId, TableSupport.EMPTY_STRING);
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
        return Objects.requireNonNullElse(googleUserPicture, TableSupport.EMPTY_STRING);
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
        return Objects.requireNonNullElse(facebookEmail, TableSupport.EMPTY_STRING);
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
        return Objects.requireNonNullElse(facebookUserName, TableSupport.EMPTY_STRING);
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
        return Objects.requireNonNullElse(facebookUserId, TableSupport.EMPTY_STRING);
    }

    public void setFacebookUserId(String facebookUserId) {
        this.facebookUserId = facebookUserId;
    }

    /**
     * Gets githubEmail field of a Social record, ensures that it never will have null value.
     *
     * @return with the githubEmail field value or with an empty string.
     */
    @Column(name = "githubEmail", nullable = true)
    public String getGithubEmail() {
        return Objects.requireNonNullElse(githubEmail, TableSupport.EMPTY_STRING);
    }

    public void setGithubEmail(String githubEmail) {
        this.githubEmail = githubEmail;
    }

    /**
     * Gets githubUserName field of a Social record, ensures that it never will have null value.
     *
     * @return with the githubUserName field value or with an empty string.
     */
    @Column(name = "githubUserName", nullable = true)
    public String getGithubUserName() {
        return Objects.requireNonNullElse(githubUserName, TableSupport.EMPTY_STRING);
    }

    public void setGithubUserName(String githubUserName) {
        this.githubUserName = githubUserName;
    }

    /**
     * Gets githubUserId field of a Social record, ensures that it never will have null value.
     *
     * @return with the githubUserId field value or with an empty string.
     */
    @Column(name = "githubUserId", nullable = true)
    public String getGithubUserId() {
        return Objects.requireNonNullElse(githubUserId, TableSupport.EMPTY_STRING);
    }

    public void setGithubUserId(String githubUserId) {
        this.githubUserId = githubUserId;
    }

    /**
     * Gets githubUserPicture field of a Social record, ensures that it never will have null value.
     *
     * @return with the githubUserPicture field value or with an empty string.
     */
    @Column(name = "githubUserPicture", nullable = true)
    public String getGithubUserPicture() {
        return Objects.requireNonNullElse(githubUserPicture, TableSupport.EMPTY_STRING);
    }

    public void setGithubUserPicture(String githubUserPicture) {
        this.githubUserPicture = githubUserPicture;
    }

    /**
     * Gets comment field of a Social record, ensures that it never will have null value.
     *
     * @return with the comment field value or with an empty string.
     */
    @Column(name = "comment", nullable = true)
    public String getComment() {
        return Objects.requireNonNullElse(comment, TableSupport.EMPTY_STRING);
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
