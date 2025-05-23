package org.rdtif.zaxbot;


import org.junit.jupiter.api.Test;
import org.rdtif.zaxbot.slack.SlackApplicationServer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ZaxSlackBotTest {
    @Test
    void startServer() {
        SlackApplicationServer server = mock(SlackApplicationServer.class);
        ZaxSlackBot bot = new ZaxSlackBot(server);

        bot.run();

        verify(server).start();
    }
}
