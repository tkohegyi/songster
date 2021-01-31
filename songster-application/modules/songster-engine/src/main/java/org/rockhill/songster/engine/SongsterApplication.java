package org.rockhill.songster.engine;

import org.rockhill.songster.bootstrap.ApplicationBootstrap;

/**
 * Starts the application.
 */
public final class SongsterApplication {

    public static String[] arguments; //NOSONAR

    private SongsterApplication() {
    }

    /**
     * The app main entry point.
     *
     * @param args The program needs the path of conf.properties to run.
     */
    public static void main(final String[] args) {
        arguments = args; //NOSONAR
        new ApplicationBootstrap().bootstrap(args);
    }
}
