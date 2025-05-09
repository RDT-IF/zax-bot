package org.rdtif.zaxbot;

class UrlVerificationEvent extends SlackEvent {
    private final String challenge;

    UrlVerificationEvent(String challenge, String type) {
        super(type);
        this.challenge = challenge;
    }

    String getChallenge() {
        return challenge;
    }
}
