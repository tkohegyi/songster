package org.rockhill.songster.web.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;
import org.rockhill.songster.database.business.BusinessWithAuditTrail;
import org.rockhill.songster.database.business.BusinessWithNextGeneralKey;
import org.rockhill.songster.database.business.BusinessWithSocial;
import org.rockhill.songster.database.business.helper.enums.SocialStatusTypes;
import org.rockhill.songster.database.json.GoogleUserInfoJson;
import org.rockhill.songster.database.tables.AuditTrail;
import org.rockhill.songster.database.tables.Social;
import org.rockhill.songster.helper.EmailSender;
import org.rockhill.songster.web.configuration.PropertyDto;
import org.rockhill.songster.web.configuration.WebAppConfigurationAccess;
import org.rockhill.songster.web.service.helper.Oauth2ServiceBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * For Handling proper Google Oauth2 authorization tasks.
 */
@Component
public class GoogleOauth2Service extends Oauth2ServiceBase {

    public static final String GOOGLE_TEXT = "Google";

    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
    private static final String SUBJECT = "[DalosApp] - Új Google Social";
    private static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/userinfo.profile",
            "https://www.googleapis.com/auth/userinfo.email");

    private final Logger logger = LoggerFactory.getLogger(GoogleOauth2Service.class);
    private final HttpTransport httpTransport = new NetHttpTransport();

    private GoogleAuthorizationCodeFlow flow;

    @Autowired
    private ApplicationCustomAuthenticationProvider applicationCustomAuthenticationProvider;
    @Autowired
    private WebAppConfigurationAccess webAppConfigurationAccess;
    @Autowired
    private BusinessWithSocial businessWithSocial;
    @Autowired
    private BusinessWithAuditTrail businessWithAuditTrail;
    @Autowired
    private BusinessWithNextGeneralKey businessWithNextGeneralKey;
    @Autowired
    private EmailSender emailSender;

    @PostConstruct
    private void googleOauth2Service() {
        PropertyDto propertyDto = webAppConfigurationAccess.getProperties();
        flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
                new JacksonFactory(), propertyDto.getGoogleClientId(), propertyDto.getGoogleClientSecret(), SCOPES).build();
    }

    /**
     * Gets the Google login url.
     *
     * @return with Google login url
     */
    public String getLoginUrlInformation() {
        //see help from https://www.programcreek.com/java-api-examples/?api=com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
        //see help from https://www.programcreek.com/java-api-examples/index.php?api=com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl

        GoogleClientSecrets.Details installedDetails = new GoogleClientSecrets.Details();
        PropertyDto propertyDto = webAppConfigurationAccess.getProperties();
        installedDetails.setClientId(propertyDto.getGoogleClientId());
        installedDetails.setClientSecret(propertyDto.getGoogleClientSecret());

        GoogleClientSecrets clientSecrets = new GoogleClientSecrets();
        clientSecrets.setInstalled(installedDetails);

        GoogleAuthorizationCodeRequestUrl googleAuthorizationCodeRequestUrl =
                new GoogleAuthorizationCodeRequestUrl(clientSecrets, propertyDto.getGoogleRedirectUrl(), SCOPES);
        return googleAuthorizationCodeRequestUrl.build();
    }

    /**
     * Beware that this can be NULL, if something is wrong.
     *
     * @param authCode authenticaton code received from Google
     * @return with Spring Authentication object
     */
    public Authentication getGoogleUserInfoJson(final String authCode) {
        Authentication authentication = null;
        PropertyDto propertyDto = webAppConfigurationAccess.getProperties();
        try {
            GoogleUser googleUser;
            final GoogleTokenResponse response = flow.newTokenRequest(authCode)
                    .setRedirectUri(propertyDto.getGoogleRedirectUrl())
                    .execute();
            final Credential credential = flow.createAndStoreCredential(response, null);
            final HttpRequest request = httpTransport.createRequestFactory(credential)
                    .buildGetRequest(new GenericUrl(USER_INFO_URL));
            request.getHeaders().setContentType("application/json");

            Gson gson = new Gson();
            GoogleUserInfoJson googleUserInfoJson = gson.fromJson(request.execute().parseAsString(), GoogleUserInfoJson.class);

            Social social = detectSocial(googleUserInfoJson);
            googleUser = new GoogleUser(social, propertyDto.getSessionTimeout());

            //googleUser used as Principal, credential is coming from Google
            authentication = applicationCustomAuthenticationProvider.authenticate(new PreAuthenticatedAuthenticationToken(googleUser, credential));
        } catch (Exception ex) {
            logger.warn("Was unable to get Google User Information.", ex);
        }
        return authentication;
    }

    private Social detectSocial(GoogleUserInfoJson googleUserInfoJson) {
        googleUserInfoJson.email = makeEmptyStringFromNull(googleUserInfoJson.email);
        googleUserInfoJson.name = makeEmptyStringFromNull(googleUserInfoJson.name);
        googleUserInfoJson.picture = makeEmptyStringFromNull(googleUserInfoJson.picture);
        Social social = businessWithSocial.getSocialByGoogleUserId(googleUserInfoJson.id); //if there is no social this will cause exception that is unhandled !!!
        if (social == null) {
            social = new Social();
            social.setGoogleEmail(googleUserInfoJson.email);
            social.setGoogleUserName(googleUserInfoJson.name);
            social.setGoogleUserId(googleUserInfoJson.id);
            social.setGoogleUserPicture(googleUserInfoJson.picture);
            social.setSocialStatus(SocialStatusTypes.WAIT_FOR_IDENTIFICATION.getTypeValue());
            Long id = businessWithNextGeneralKey.getNextGeneralId();
            social.setId(id);
            AuditTrail auditTrail = businessWithAuditTrail.prepareAuditTrail(id, social.getGoogleUserName(),
                    AUDIT_SOCIAL_CREATE + id.toString(), "New Google Social login created.", GOOGLE_TEXT);
            String text = "New Social id: " + id.toString() + "\nGoogle Type,\nName: " + social.getGoogleUserName() + ",\nEmail: " + social.getGoogleEmail();
            emailSender.sendMailToAdministrator(SUBJECT, text); //to administrator to inform about the person
            text = "Kedves " + social.getGoogleUserName()
                    + "!\n\nKöszönettel vettük első bejelentkezésedet a Dalos Magyar Website weboldalán."
                    + "\n\nA következő adatokat ismertük meg rólad:"
                    + "\nNév: " + social.getGoogleUserName()
                    + "\nE-mail: " + social.getGoogleEmail()
                    + "\nGoogle azonosító: " + social.getGoogleUserId()
                    + "\nGoogle kép: " + social.getGoogleUserPicture()
                    + "\n\nAdatkezelési tájékoztatónkat megtalálhatod itt: https://dalos.magyar.website/resources/img/AdatkezelesiSzabalyzat.pdf"
                    + "\nAdataidról információt illetve azok törlését pedig erre az e-mail címre írva kérheted: kohegyi.tamas (kukac) vac-deakvar.vaciegyhazmegye.hu."
                    + "\nUgyanezen a címen várjuk leveledet akkor is, ha kérdésed, észrevételed vagy javaslatod van a weboldallal kapcsolatban. "
                    + "\n\nAmennyiben dalokat szeretnél felhasználni, vagy feltölteni, erre a levélre válaszolva kérlek írd ezt meg, egyébként sajnos nem tudunk további hozzáférést bisztosítani az oldal tartalmához."
                    + "\n\nÜdvözlettel:\nKőhegyi Tamás\n+36-70-375-4140\n";
            //send feedback mail to the registered user
            emailSender.sendMailFromSocialLogin(social.getGoogleEmail(), "Belépés a dalos.magyar.website weboldalán Google azonosítóval", text);
            id = businessWithSocial.newSocial(social, auditTrail);
            social.setId(id); //Social object is ready
        } else {
            //detect social update and act
            autoUpdateGoogleSocial(social, googleUserInfoJson.name, googleUserInfoJson.email, googleUserInfoJson.picture);
        }
        return social;
    }

    private void autoUpdateGoogleSocial(Social social, String name, String email, String picture) {
        Collection<AuditTrail> auditTrailCollection = new ArrayList<>();
        if (social.getGoogleUserName().compareToIgnoreCase(name) != 0) {
            social.setGoogleUserName(name);
            Long id = businessWithNextGeneralKey.getNextGeneralId();
            AuditTrail auditTrail = businessWithAuditTrail.prepareAuditTrail(id, social.getGoogleUserName(), AUDIT_SOCIAL_UPDATE + social.getId().toString(),
                    "Google Username updated to:" + name, GOOGLE_TEXT);
            auditTrailCollection.add(auditTrail);
        }
        if (social.getGoogleEmail().compareToIgnoreCase(email) != 0) {
            social.setGoogleEmail(email);
            Long id = businessWithNextGeneralKey.getNextGeneralId();
            AuditTrail auditTrail = businessWithAuditTrail.prepareAuditTrail(id, social.getGoogleUserName(), AUDIT_SOCIAL_UPDATE + social.getId().toString(),
                    "Google Email updated to:" + email, GOOGLE_TEXT);
            auditTrailCollection.add(auditTrail);
        }
        if (social.getGoogleUserPicture().compareToIgnoreCase(picture) != 0) {
            social.setGoogleUserPicture(picture);
            Long id = businessWithNextGeneralKey.getNextGeneralId();
            AuditTrail auditTrail = businessWithAuditTrail.prepareAuditTrail(id, social.getGoogleUserName(), AUDIT_SOCIAL_UPDATE + social.getId().toString(),
                    "Google Picture updated to:" + picture, GOOGLE_TEXT);
            auditTrailCollection.add(auditTrail);
        }
        if (!auditTrailCollection.isEmpty()) {
            businessWithSocial.updateSocial(social, auditTrailCollection);
        }
    }

}
