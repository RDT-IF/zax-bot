package org.rdtif.zaxbot;

import com.google.inject.Guice;
import org.rdtif.zaxbot.slack.SlackApplicationServer;

import jakarta.inject.Inject;

class ZaxSlackBot {
    public static void main(String... arguments) {
        Guice.createInjector(new ZaxSlackBotModule()).getInstance(ZaxSlackBot.class).run();
    }

    private final SlackApplicationServer slackApplicationServer;

    @Inject
    ZaxSlackBot(SlackApplicationServer slackApplicationServer) {
        this.slackApplicationServer = slackApplicationServer;
    }

    void run() {
        slackApplicationServer.start();
    }
}
