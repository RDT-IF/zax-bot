package org.rdtif.zaxbot.interpreter;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import jakarta.inject.Singleton;
import org.rdtif.zaxbot.GameFileRepository;
import org.rdtif.zaxbot.ZaxSlackBotConfiguration;
import org.rdtif.zaxbot.slack.SlackClient;

import java.util.Map;

public class InterpreterModule extends AbstractModule {
    @Provides
    @Singleton
    public Map<LanguageAction, Action> providesActionMap(ZaxSlackBotConfiguration configuration, GameFileRepository repository, ZCpuFactory zCpuFactory, SlackClient slackClient) {
        return ImmutableMap.<LanguageAction, Action>builder()
                .put(LanguageAction.ListGames, new ListGamesAction(repository))
                .put(LanguageAction.StartGame, new StartGameAction(configuration, repository, zCpuFactory, slackClient))
                .build();
    }

    @Provides
    @Singleton
    public Interpreter providesLanguageProcessor(Map<LanguageAction, Action> actionActionMap, LanguagePatternLoader languagePatternLoader, SlackClient slackClient) {
        Interpreter interpreter = new Interpreter(actionActionMap, slackClient);
        interpreter.registerPattern(languagePatternLoader.load("GreetingPattern.json"));
        interpreter.registerPattern(languagePatternLoader.load("ListGamesPattern.json"));
        interpreter.registerPattern(languagePatternLoader.load("StartGamesPattern.json"));
        return interpreter;
    }
}
