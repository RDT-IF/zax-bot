package org.rdtif.zaxbot.interpreter;

public interface Action {
    String execute(String channelID, String input, LanguagePattern pattern);
}
