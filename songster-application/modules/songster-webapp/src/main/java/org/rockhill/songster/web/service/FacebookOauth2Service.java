package org.rockhill.songster.web.service;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.rockhill.songster.database.business.BusinessWithAuditTrail;
import org.rockhill.songster.database.business.BusinessWithNextGeneralKey;
import org.rockhill.songster.database.business.BusinessWithSocial;
import org.rockhill.songster.database.business.helper.enums.SocialStatusTypes;
import org.rockhill.songster.database.tables.AuditTrail;
import org.rockhill.songster.database.tables.Social;
import org.rockhill.songster.exception.SystemException;
import org.rockhill.songster.helper.EmailSender;
import org.rockhill.songster.web.configuration.PropertyDto;
import org.rockhill.songster.web.configuration.WebAppConfigurationAccess;
import org.rockhill.songster.web.service.helper.Oauth2ServiceBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;

/**
 * For Handling proper Facebook Oauth2 authorization tasks.
 */
@Component
public class FacebookOauth2Service extends Oauth2ServiceBase {

    public static final String FACEBOOK_TEXT = "Facebook";

    private static final String GRAPH_URL = "https://graph.facebook.com/v5.0/oauth/access_token?";
    private static final String AUTHORIZATION_URL = "https://www.facebook.com/v5.0/dialog/oauth?";
    private static final String SUBJECT = "[DalosApp] - Új Facebook Social";

    private final Logger logger = LoggerFactory.getLogger(FacebookOauth2Service.class);

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

    private FacebookConnectionFactory facebookConnectionFactory;

    @PostConstruct
    private void facebookOauth2Service() {
        PropertyDto propertyDto = webAppConfigurationAccess.getProperties();
        facebookConnectionFactory = new FacebookConnectionFactory(propertyDto.getFacebookAppId(), propertyDto.getFacebookAppSecret());
        facebookConnectionFactory.setScope("email,public_profile");
    }

    /**
     * Get Facebook redirect URL to its Oauth2 service.
     *
     * @return with the Url
     */
    public String getLoginUrlInformation() {
        PropertyDto propertyDto = webAppConfigurationAccess.getProperties();
        String authorizationUrl;
        authorizationUrl = AUTHORIZATION_URL
                + "client_id=" + propertyDto.getFacebookAppId()
                + "&redirect_uri=" + propertyDto.getGoogleRedirectUrl()
                + "&state=no-state&display=popup&response_type=code&scope=" + facebookConnectionFactory.getScope();
        //note use this: &response_type=granted_scopes to get list of granted scopes
        return authorizationUrl;
    }

    private String getFacebookGraphUrl(final String code, final String applicationId,
                                       final String applicationSecret, final String redirectUrl) throws UnsupportedEncodingException {
        String fbGraphUrl;
        fbGraphUrl = GRAPH_URL
                + "client_id=" + applicationId
                + "&redirect_uri=" + URLEncoder.encode(redirectUrl, "UTF-8")
                + "&client_secret=" + applicationSecret
                + "&code=" + code;
        return fbGraphUrl;
    }

    private String getAccessToken(String code, String applicationId, String applicationSecret, String redirectUrl) throws IOException, ParseException {
        URL fbGraphURL;
        String fbGraphURLString = getFacebookGraphUrl(code, applicationId, applicationSecret, redirectUrl);
        fbGraphURL = new URL(fbGraphURLString);
        URLConnection fbConnection = fbGraphURL.openConnection(); //NOSONAR - code is properly protected
        BufferedReader in = new BufferedReader(new InputStreamReader(fbConnection.getInputStream()));
        String inputLine;
        StringBuilder b = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            b.append(inputLine + "\n");
        }
        in.close();

        String accessToken = b.toString();
        JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        JSONObject json = (JSONObject) parser.parse(accessToken);
        accessToken = json.getAsString("access_token");
        return accessToken;
    }

    private JSONObject getFacebookGraph(String accessToken) {
        String graph;
        JSONObject json;
        try {
            String g = "https://graph.facebook.com/me?access_token=" + accessToken + "&fields=name,id,email";
            URL u = new URL(g);
            URLConnection c = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    c.getInputStream()));
            String inputLine;
            StringBuilder b = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                b.append(inputLine + "\n");
            }
            in.close();
            graph = b.toString();
            JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
            json = (JSONObject) parser.parse(graph);
        } catch (Exception e) {
            throw new SystemException("ERROR in getting FB graph data. ", e);
        }
        return json;
    }

    /**
     * Authenticate the user with Facebook Oauth2 service.
     *
     * @param authCode is the code arrived from Facebook
     * @return with the Authentication class (with a Facebook user in it) or null
     */
    public Authentication getFacebookUserInfoJson(final String authCode) {
        FacebookUser facebookUser;
        Authentication authentication = null;
        PropertyDto propertyDto = webAppConfigurationAccess.getProperties();
        try {
            String accessToken = getAccessToken(authCode, propertyDto.getFacebookAppId(), propertyDto.getFacebookAppSecret(), propertyDto.getGoogleRedirectUrl());
            JSONObject facebookUserInfoJson = getFacebookGraph(accessToken);
            Social social = detectSocial(facebookUserInfoJson);
            facebookUser = new FacebookUser(social, propertyDto.getSessionTimeout());

            // googleUser used as Principal, credential is coming from Google
            authentication = applicationCustomAuthenticationProvider.authenticate(new PreAuthenticatedAuthenticationToken(facebookUser, facebookUserInfoJson));
        } catch (Exception e) {
            logger.warn("Was unable to get Facebook User Information.", e);
        }
        return authentication;
    }

    private Social detectSocial(JSONObject facebookUserInfoJson) {
        String userId = facebookUserInfoJson.getAsString("id");
        String email = facebookUserInfoJson.getAsString("email");
        email = makeEmptyStringFromNull(email);
        String firstName = facebookUserInfoJson.getAsString("name");
        firstName = makeEmptyStringFromNull(firstName);
        Social social = businessWithSocial.getSocialByFacebookUserId(userId);
        if (social == null) {
            social = new Social();
            social.setFacebookUserId(userId);
            social.setFacebookEmail(email);
            social.setFacebookFirstName(firstName);
            social.setFacebookUserName(social.getFacebookFirstName());  // this is what we can access by default...
            social.setSocialStatus(SocialStatusTypes.WAIT_FOR_IDENTIFICATION.getTypeValue());
            Long id = businessWithNextGeneralKey.getNextGeneralId();
            social.setId(id);
            AuditTrail auditTrail = businessWithAuditTrail.prepareAuditTrail(id, social.getFacebookUserName(),
                    AUDIT_SOCIAL_CREATE + id.toString(), "New Facebook Social login created.", FACEBOOK_TEXT);
            String text = "New Social id: " + id.toString() + "\nFacebook Type,\n Name: " + social.getFacebookUserName() + ",\nEmail: " + social.getFacebookEmail();
            emailSender.sendMailToAdministrator(SUBJECT, text); //send mail to administrator
            text = "Kedves " + social.getFacebookUserName()
                    + "!\n\nKöszönettel vettük első bejelentkezésedet a Dalos Magyar Website weboldalán."
                    + "\n\nA következő adatokat ismertük meg rólad:"
                    + "\nNév: " + social.getFacebookUserName()
                    + "\nE-mail: " + social.getFacebookEmail()
                    + "\nFacebook azonosító: " + social.getFacebookUserId()
                    + "\n\nAdatkezelési tájékoztatónkat megtalálhatod itt: https://dalos.magyar.website/resources/img/AdatkezelesiSzabalyzat.pdf"
                    + "\nAdataidról információt illetve azok törlését pedig erre az e-mail címre írva kérheted: kohegyi.tamas (kukac) vac-deakvar.vaciegyhazmegye.hu."
                    + "\nUgyanezen a címen várjuk leveledet akkor is, ha kérdésed, észrevételed vagy javaslatod van a weboldallal kapcsolatban. "
                    + "\n\nAmennyiben dalokat szeretnél felhasználni, vagy feltölteni, erre a levélre válaszolva kérlek írd ezt meg, egyébként sajnos nem tudunk további hozzáférést bisztosítani az oldal tartalmához."
                    + "\n\nÜdvözlettel:\nKőhegyi Tamás\n+36-70-375-4140\n";
            //send feedback mail to the registered user
            emailSender.sendMailFromSocialLogin(social.getGoogleEmail(), "Belépés a dalos.magyar.website weboldalán Facebook azonosítóval", text);
            id = businessWithSocial.newSocial(social, auditTrail);
            social.setId(id); //Social object is ready
        } else {
            //detect social update and act
            autoUpdateFacebookSocial(social, firstName, email);
        }
        return social;
    }

    private void autoUpdateFacebookSocial(Social social, String firstName, String email) {
        Collection<AuditTrail> auditTrailCollection = new ArrayList<>();
        if (social.getFacebookFirstName().compareToIgnoreCase(firstName) != 0) {
            social.setFacebookFirstName(firstName);
            social.setFacebookUserName(firstName);  // this is what we can access by default...
            Long id = businessWithNextGeneralKey.getNextGeneralId();
            AuditTrail auditTrail = businessWithAuditTrail.prepareAuditTrail(id, social.getFacebookUserName(), AUDIT_SOCIAL_UPDATE + social.getId().toString(),
                    "Facebook FirstName/Name updated to:" + firstName, FACEBOOK_TEXT);
            auditTrailCollection.add(auditTrail);
        }
        if (social.getFacebookEmail().compareToIgnoreCase(email) != 0) {
            social.setFacebookEmail(email);
            Long id = businessWithNextGeneralKey.getNextGeneralId();
            AuditTrail auditTrail = businessWithAuditTrail.prepareAuditTrail(id, social.getFacebookUserName(), AUDIT_SOCIAL_UPDATE + social.getId().toString(),
                    "Facebook Email updated to:" + email, FACEBOOK_TEXT);
            auditTrailCollection.add(auditTrail);
        }
        if (!auditTrailCollection.isEmpty()) {
            businessWithSocial.updateSocial(social, auditTrailCollection);
        }
    }

}
