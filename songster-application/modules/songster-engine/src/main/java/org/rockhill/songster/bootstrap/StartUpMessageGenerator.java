package org.rockhill.songster.bootstrap;

import org.rockhill.songster.configuration.VersionTitleProvider;
import org.rockhill.songster.initialize.EngineConfigurationAccess;
import org.rockhill.songster.initialize.PropertyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Logs the startup message.
 */
@Component
public class StartUpMessageGenerator {

    private final Logger logger = LoggerFactory.getLogger(StartUpMessageGenerator.class);

    @Value("#{startMessage}")
    private String startMessage;
    @Autowired
    private EngineConfigurationAccess configurationAccess;
    @Autowired
    private VersionTitleProvider versionTitleProvider;

    /**
     * Logs the startup message.
     */
    public void logStartUpMessage() {
        PropertyDto properties = configurationAccess.getProperties();
        Integer port = properties.getPort();
        String startUpMessage = String.format(startMessage, versionTitleProvider.getVersionTitle(), port);
        logger.info(startUpMessage);

    }

}
