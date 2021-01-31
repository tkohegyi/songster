package org.rockhill.songster.web.configuration;

/**
 * Holds module specific properties.
 */
public class PropertyDto {

    private final String googleClientId;
    private final String googleClientSecret;
    private final String googleRedirectUrl;
    private final String baseUrl;
    private final String facebookAppId;
    private final String facebookAppSecret;
    private final Integer sessionTimeout;
    private final String captchaSiteSecret;

    /**
     * Constructs a new property holding object with the given fields.
     */
    public PropertyDto(final String googleClientId, final String googleClientSecret, final String googleRedirectUrl, //NOSONAR
                       final String baseUrl, final String facebookAppId, final String facebookAppSecret,
                       final Integer sessionTimeout, final String captchaSiteSecret) {
        super();
        this.googleClientId = googleClientId;
        this.googleClientSecret = googleClientSecret;
        this.googleRedirectUrl = googleRedirectUrl;
        this.baseUrl = baseUrl;
        this.facebookAppId = facebookAppId;
        this.facebookAppSecret = facebookAppSecret;
        this.sessionTimeout = sessionTimeout;
        this.captchaSiteSecret = captchaSiteSecret;
    }

    public String getGoogleClientId() {
        return googleClientId;
    }

    public String getGoogleClientSecret() {
        return googleClientSecret;
    }

    public String getGoogleRedirectUrl() {
        return googleRedirectUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getFacebookAppId() {
        return facebookAppId;
    }

    public String getFacebookAppSecret() {
        return facebookAppSecret;
    }

    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    public String getCaptchaSiteSecret() {
        return captchaSiteSecret;
    }
}
