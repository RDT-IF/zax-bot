package org.rdtif.zaxbot;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class GameIdTest {
    @Test
    void equalsContract() {
        EqualsVerifier.forClass(GameId.class).verify();
    }
}
