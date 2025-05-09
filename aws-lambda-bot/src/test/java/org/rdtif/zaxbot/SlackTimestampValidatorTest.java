package org.rdtif.zaxbot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class SlackTimestampValidatorTest {
    private final SlackTimestampValidator validator = new SlackTimestampValidator();

    @Test
    void timeStampInValidRangeMinimum() {
        long timestamp = Instant.now().toEpochMilli() / 1000;

        boolean valid = validator.validate(String.valueOf(timestamp));

        assertThat(valid, equalTo(true));
    }

    @Test
    void timeStampInValidRangeMaximum() {
        long timestamp = (Instant.now().toEpochMilli() / 1000) - (5 * 60);

        boolean valid = validator.validate(String.valueOf(timestamp));

        assertThat(valid, equalTo(true));
    }

    @Test
    void timeStampAboveValidRange() {
        long timestamp = Instant.now().toEpochMilli() + 1;

        boolean valid = validator.validate(String.valueOf(timestamp));

        assertThat(valid, equalTo(false));
    }

    @Test
    void timeStampBelowValidRange() {
        long timestamp = Instant.now().toEpochMilli() - (5 * 60) - 1;

        boolean valid = validator.validate(String.valueOf(timestamp));

        assertThat(valid, equalTo(false));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void timeStampBelowValidRange(String timestamp) {
        boolean valid = validator.validate(timestamp);

        assertThat(valid, equalTo(false));
    }
}
