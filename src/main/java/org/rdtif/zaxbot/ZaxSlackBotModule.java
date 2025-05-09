package org.rdtif.zaxbot;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import jakarta.inject.Singleton;
import org.rdtif.zaxbot.interpreter.InterpreterModule;
import org.rdtif.zaxbot.slack.SlackApplicationModule;
import org.rdtif.zaxbot.userinterface.InputState;

public class ZaxSlackBotModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new SlackApplicationModule());
        install(new InterpreterModule());
    }

    @Provides
    @Singleton
    public ZaxSlackBotConfiguration providesConfiguration(ConfigurationLoader configurationLoader) {
        return configurationLoader.getConfigurationFrom("..");
    }

    @Provides
    @Singleton
    public InputState providesInputState() {
        return new InputState();
    }
}
