package org.rdtif.zaxbot;

class SlackEvent {
    SlackEvent(String type) {
        this.type = type;
    }

    String getType() {
        return type;
    }

    private final String type;
}
