package org.rockhill.songster.initialize;

import org.rockhill.songster.configuration.ConfigurationAccessBase;
import org.rockhill.songster.configuration.PropertyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Configures the module with the necessary properties.
 */
@Component
public class EngineConfigurationAccess implements ConfigurationAccessBase {

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
        Integer port = propertyHolder.getInt("webapp.port");
        properties = new PropertyDto(port);
    }
}
