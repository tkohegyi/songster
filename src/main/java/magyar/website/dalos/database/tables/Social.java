package magyar.website.dalos.database.tables;

import magyar.website.dalos.database.tables.help.SocialServiceTypes;
import magyar.website.dalos.database.tables.help.SocialStatusTypes;
import magyar.website.dalos.database.tables.help.TableSupport;
import magyar.website.dalos.exception.DatabaseHandlingException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private Integer socialStatus;
    private Integer socialService;
    private String lastLoginDateTime;
    private String dhcSignedDate;
    private String userId;
    private String email;
    private String userName;
    private String userPicture;
    private String otherInfo;
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

    @Column(name = "socialService", nullable = false)
    public Integer getSocialService() {
        return socialService;
    }

    /**
     * Sets the id of the social service of the social user.
     *
     * @param socialService is the socialService id
     * @throws DatabaseHandlingException if the id is not valid
     */
    public void setSocialService(Integer socialService) {
        SocialServiceTypes.getTypeFromId(socialService); //validation
        this.socialService = socialService;
    }

    /**
     * Gets dhcSignedDate field of a Person record, ensures that it never will have null value.
     *
     * @return with the dhcSignedDate field value or with an empty string.
     */
    @Column(name = "dhcSignedDate", nullable = false)
    public String getDhcSignedDate() {
        return dhcSignedDate;
    }

    /**
     * Sets the data handling consent date according to the given string, that must be in "YYYY-MM-DD" format.
     * This have to be filled during the very first login event.
     *
     * @param dhcSignedDate is the string format of the date
     * @throws DatabaseHandlingException if the string format/content is invalid
     */
    public void setDhcSignedDate(String dhcSignedDate) {
        if (dhcSignedDate == null) {
            throw new DatabaseHandlingException("DhcSignedDate date format is unacceptable, it must be YYYY-MM-DD");
        }
        try {
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            df1.parse(dhcSignedDate);
        } catch (ParseException e) {
            throw new DatabaseHandlingException("DhcSignedDate date format is unacceptable, it must be YYYY-MM-DD");
        }
        this.dhcSignedDate = dhcSignedDate;
    }

    /**
     * Gets lastLoginDateTime field of a record, ensures that it never will have null value.
     *
     * @return with the lastLoginDateTime field value.
     */
    @Column(name = "lastLoginDateTime", nullable = false)
    public String getLastLoginDateTime() {
        return lastLoginDateTime;
    }

    /**
     * Sets the lastLoginDateTime field according to the given string, that must be in "YYYY-MM-DD HH:NN:SS.SSS" format.
     *
     * @param lastLoginDateTime is the string format of the date
     * @throws DatabaseHandlingException if the string format/content is invalid
     */
    public void setLastLoginDateTime(String lastLoginDateTime) {
        if (lastLoginDateTime == null) {
            throw new DatabaseHandlingException("LastLoginDateTime date format is unacceptable, it must be YYYY-MM-DD HH:NN:SS.SSS");
        }
        try {
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:NN:SS.SSS");
            df1.parse(lastLoginDateTime);
        } catch (ParseException e) {
            throw new DatabaseHandlingException("LastLoginDateTime date format is unacceptable, it must be YYYY-MM-DD HH:NN:SS.SSS");
        }
        this.lastLoginDateTime = lastLoginDateTime;
    }

    /**
     * Gets email field of a Social record, ensures that it never will have null value.
     *
     * @return with the email field value.
     */
    @Column(name = "email", nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null) {
            throw new DatabaseHandlingException("User email cannot be null");
        }
        this.email = email;
    }

    /**
     * Gets userName field of a Social record, ensures that it never will have null value.
     *
     * @return with the userName field value or with an empty string.
     */
    @Column(name = "userName", nullable = false)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets userId field of a Social record, ensures that it never will have null value.
     *
     * @return with the userId field value.
     */
    @Column(name = "userId", nullable = false)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets userPicture field of a Social record, ensures that it never will have null value.
     *
     * @return with the userPicture field value.
     */
    @Column(name = "userPicture", nullable = false)
    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    /**
     * Gets otherInfo field of a Social record, ensures that it never will have null value.
     * This is an embedded JSON object - see {@link magyar.website.dalos.database.tables.help.OtherInfoJson}
     *
     * @return with the otherInfo field value or with an empty string.
     */
    @Column(name = "otherInfo", nullable = true)
    public String getOtherInfo() {
        return Objects.requireNonNullElse(otherInfo, TableSupport.EMPTY_JSON);
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
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
