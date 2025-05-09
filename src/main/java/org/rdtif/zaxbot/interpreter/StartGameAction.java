package org.rdtif.zaxbot.interpreter;

import com.google.inject.Inject;
import com.zaxsoft.zax.zmachine.ZCPU;
import org.rdtif.zaxbot.GameFileRepository;
import org.rdtif.zaxbot.ZaxSlackBotConfiguration;
import org.rdtif.zaxbot.slack.SlackClient;
import org.rdtif.zaxbot.slack.SlackDisplayMessageUpdater;
import org.rdtif.zaxbot.userinterface.Extent;
import org.rdtif.zaxbot.userinterface.SlackTextScreen;
import org.rdtif.zaxbot.userinterface.SlackZUserInterface;
import org.rdtif.zaxbot.GameFileRepository;
import org.rdtif.zaxbot.ZaxSlackBotConfiguration;
import org.rdtif.zaxbot.slack.SlackClient;
import org.rdtif.zaxbot.slack.SlackDisplayMessageMaker;
import org.rdtif.zaxbot.slack.SlackDisplayMessageUpdater;
import org.rdtif.zaxbot.userinterface.Extent;
import org.rdtif.zaxbot.userinterface.InputState;
import org.rdtif.zaxbot.userinterface.SlackTextScreen;
import org.rdtif.zaxbot.userinterface.SlackZUserInterface;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartGameAction implements Action {
    private final ZaxSlackBotConfiguration configuration;
    private final GameFileRepository gameFileRepository;
    private final ZCpuFactory zCpuFactory;
    private final SlackClient slackClient;
    private final InputState inputState;

    @Inject
    public StartGameAction(ZaxSlackBotConfiguration configuration, GameFileRepository gameFileRepository, ZCpuFactory zCpuFactory, SlackClient slackClient, InputState inputState) {
        this.configuration = configuration;
        this.gameFileRepository = gameFileRepository;
        this.zCpuFactory = zCpuFactory;
        this.slackClient = slackClient;
        this.inputState = inputState;
    }

    @Override
    public String execute(String channelID, String input, LanguagePattern pattern) {
        String gameName = extractGameName(input, pattern);

        if (gameFileRepository.fileNames().contains(gameName)) {
            SlackTextScreen screen = new SlackTextScreen(new Extent(25, 80), new SlackDisplayMessageUpdater(slackClient, channelID), new SlackDisplayMessageMaker(inputState));
            SlackZUserInterface userInterface = new SlackZUserInterface(screen, inputState);
            ZCPU cpu = zCpuFactory.create(userInterface);

            cpu.initialize(configuration.getGameDirectory() + gameName);
            cpu.start();
            return "";
        }

        return pattern.responseFor("default");
    }

    private String extractGameName(String input, LanguagePattern pattern) {
        Pattern regex = Pattern.compile(pattern.getPattern());
        Matcher matcher = regex.matcher(input);

        if (matcher.find()) {
            return matcher.group(2);
        }

        return "";
    }
}
