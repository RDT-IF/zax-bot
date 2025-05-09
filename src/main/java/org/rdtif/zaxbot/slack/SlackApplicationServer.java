package org.rdtif.zaxbot.slack;


import com.slack.api.bolt.jakarta_jetty.SlackAppServer;
import jakarta.inject.Inject;

public class SlackApplicationServer {
    private final SlackAppServer slackAppServer;

    @Inject
    public SlackApplicationServer(SlackAppServer slackAppServer) {
        this.slackAppServer = slackAppServer;
    }

    public void start() {
        try {
            slackAppServer.start();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
