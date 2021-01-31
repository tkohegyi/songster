package org.rockhill.songster.web.configuration;

import org.apache.commons.lang3.math.NumberUtils;
import org.rockhill.songster.configuration.ConfigurationAccessBase;
import org.rockhill.songster.configuration.PropertyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Configures the module with the necessary properties.
 */
@Component
public class WebAppConfigurationAccess implements ConfigurationAccessBase {
    private static final int SESSION_TIMEOUT_DEFAULT_VALUE = 500;

    private PropertyDto properties;

    @Autowired
    private PropertyHolder propertyHolder;

    /**
     * Returns a {@link PropertyDto} holding all module specific properties.
     *
     * @return the propertiesDTO object
     */
    public PropertyDto getProperties() {
        return properties;
    }

    @Override
    public void loadProperties() {
        String googleClientId = propertyHolder.get("google_client_id");
        String googleClientSecret = propertyHolder.get("google_client_secret");
        String googleRedirectUrl = propertyHolder.get("google_redirect_url");
        String baseUrl = propertyHolder.get("base_url");
        String facebookAppId = propertyHolder.get("facebook_app_id");
        String facebookAppSecret = propertyHolder.get("facebook_app_secret");
        Integer sessionTimeout = NumberUtils.toInt(propertyHolder.get("sessionTimeout"), SESSION_TIMEOUT_DEFAULT_VALUE);
        String captchaSiteSecret = propertyHolder.get("captcha_site_secret");
        properties = new PropertyDto(googleClientId, googleClientSecret, googleRedirectUrl, baseUrl,
                facebookAppId, facebookAppSecret, sessionTimeout, captchaSiteSecret);
    }
}
