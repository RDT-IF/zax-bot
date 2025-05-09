package org.rdtif.zaxbot;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.rdtif.zaxbot.interpreter.InterpreterModule;
import org.rdtif.zaxbot.slack.SlackApplicationModule;

import javax.inject.Singleton;

public class ZaxSlackBotModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new SlackApplicationModule());
        install(new InterpreterModule());
    }

    @Provides
    @Singleton
    public ZaxSlackBotConfiguration providesConfiguration(ConfigurationLoader configurationLoader) {
        return configurationLoader.getConfigurationFrom(".");
    }
}
