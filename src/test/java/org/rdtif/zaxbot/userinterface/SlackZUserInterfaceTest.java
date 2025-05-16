package org.rdtif.zaxbot.userinterface;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.rdtif.zaxbot.Zoey;

import java.awt.Dimension;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SlackZUserInterfaceTest {
    // These are my best guess based on observations -- scaling will change the actual.
    private static final int FONT_WIDTH_IN_PIXELS = 8;
    private static final int FONT_HEIGHT_IN_PIXELS = 16;
    private static final int Z_MACHINE_BLACK = 2;
    private static final int Z_MACHINE_WHITE = 9;
    private final SlackTextScreen slackTextScreen = mock(SlackTextScreen.class);
    private final SlackZUserInterface slackZUserInterface = new SlackZUserInterface(slackTextScreen, null);

    @Test
    void initializeInitializesScreen() {
        slackZUserInterface.initialize(0);

        verify(slackTextScreen).initialize(0);
    }

    @Test
    void initializeCallsUpdate() {
        slackZUserInterface.initialize(0);

        verify(slackTextScreen).update();
    }

    @Test
    void eraseWindow() {
        //noinspection ConstantConditions
        int window = new Random().nextInt(1);
        slackZUserInterface.eraseWindow(window);

        verify(slackTextScreen).eraseWindow(window);
    }

    @Test
    void displayMessageWhenFatal() {
        String message = RandomStringUtils.randomAlphabetic(13);

        assertThrows(ZaxFatalException.class, () -> new SlackZUserInterface(slackTextScreen, null).fatal(message));

        verify(slackTextScreen).print(message);
    }

    @Test
    void fatalThrowsException() {
        assertThrows(ZaxFatalException.class, () -> new SlackZUserInterface(slackTextScreen, null).fatal(RandomStringUtils.randomAlphabetic(13)));
    }

    @Test
    void fatalThrowsExceptionWithMessage() {
        String message = RandomStringUtils.randomAlphabetic(13);
        ZaxFatalException exception = assertThrows(ZaxFatalException.class, () -> new SlackZUserInterface(slackTextScreen, null).fatal(message));
        assertThat(exception.getMessage(), equalTo(message));
    }

    @Test
    void getScreenCharactersReturnsDimensionFromTextScreenExtent() {
        Extent size = Zoey.randomExtent();
        when(slackTextScreen.getSize()).thenReturn(size);
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);

        Dimension screenSizeInCharacters = userInterface.getScreenCharacters();

        assertThat(screenSizeInCharacters.height, equalTo(size.getRows()));
        assertThat(screenSizeInCharacters.width, equalTo(size.getColumns()));
    }

    @Test
    void getFontSizeReturnsEightBy16() {
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);

        Dimension fontSizeInPixels = userInterface.getFontSize();

        assertThat(fontSizeInPixels.width, equalTo(FONT_WIDTH_IN_PIXELS));
        assertThat(fontSizeInPixels.height, equalTo(FONT_HEIGHT_IN_PIXELS));
    }

    @Test
    void getScreenUnits() {
        Extent size = Zoey.randomExtent();
        when(slackTextScreen.getSize()).thenReturn(size);
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);

        Dimension screenSizeInPixels = userInterface.getScreenUnits();

        assertThat(screenSizeInPixels.width, equalTo(FONT_WIDTH_IN_PIXELS * size.getColumns()));
        assertThat(screenSizeInPixels.height, equalTo(FONT_HEIGHT_IN_PIXELS * size.getRows()));
    }

    @Test
    void getDefaultForeground() {
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);

        assertThat(userInterface.getDefaultForeground(), equalTo(Z_MACHINE_BLACK));
    }

    @Test
    void getDefaultBackground() {
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);

        assertThat(userInterface.getDefaultBackground(), equalTo(Z_MACHINE_WHITE));
    }

    @Test
    void hasBoldFaceShouldReturnTrue() {
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);

        assertThat(userInterface.hasBoldface(), equalTo(false));
    }

    @Test
    void hasItalicShouldReturnTrue() {
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);

        assertThat(userInterface.hasItalic(), equalTo(false));
    }

    @Test
    void hasFixedWidthShouldReturnTrue() {
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);

        assertThat(userInterface.hasFixedWidth(), equalTo(true));
    }


    @Test
    void defaultFontProportionalShouldReturnFalse() {
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);

        assertThat(userInterface.defaultFontProportional(), equalTo(false));
    }

    @Test
    void hasColorsShouldReturnFalse() {
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);

        assertThat(userInterface.hasColors(), equalTo(false));
    }

    @Test
    void hasTimedInputShouldReturnFalse() {
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);

        assertThat(userInterface.hasTimedInput(), equalTo(false));
    }

    @Test
    void hasUpperWindow() {
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);

        assertThat(userInterface.hasUpperWindow(), equalTo(slackTextScreen.hasUpperWindow()));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void hasStatusLineTrue(int ZMachineVersion) {
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);
        userInterface.initialize(ZMachineVersion);

        assertThat(userInterface.hasStatusLine(), equalTo(true));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 4, 5})
    void hasStatusLineFalse(int ZMachineVersion) {
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);
        userInterface.initialize(ZMachineVersion);

        assertThat(userInterface.hasStatusLine(), equalTo(false));
    }

    @Test
    void statusBarIncludesStatusMessage() {
        String statusMessage = "status message";
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);
        Extent size = new Extent(1, 29);
        when(slackTextScreen.getSize()).thenReturn(size);

        userInterface.showStatusBar(statusMessage, 0, 0, false);

        verify(slackTextScreen).setStatusBar(ArgumentMatchers.startsWith(" " + statusMessage + " "));
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 11, 12, 13, 59})
    void statusBarMessageWithTimeThatHasSeconds10OrMore(int b) {
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);
        Extent size = new Extent(1, 29);
        when(slackTextScreen.getSize()).thenReturn(size);

        userInterface.showStatusBar("status message", 100, b, true);

        verify(slackTextScreen).setStatusBar(ArgumentMatchers.eq(" status message  Time: 100:" + b + " "));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9})
    void statusBarMessageWithTimeThatHasSecondsLessThan10(int b) {
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);
        Extent size = new Extent(1, 29);
        when(slackTextScreen.getSize()).thenReturn(size);

        userInterface.showStatusBar("status message", 100, b, true);

        verify(slackTextScreen).setStatusBar(ArgumentMatchers.eq(" status message  Time: 100:0" + b + " "));
    }

    @Test
    void handleScoreAndTurnsMode() {
        int score = RandomUtils.insecure().randomInt(1, 1000);
        int turns = RandomUtils.insecure().randomInt(1, 1000);
        String statusMessage = "status message";
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);
        Extent size = new Extent(1, 40);
        when(slackTextScreen.getSize()).thenReturn(size);

        userInterface.showStatusBar(statusMessage, score, turns, false);

        verify(slackTextScreen).setStatusBar(ArgumentMatchers.eq(" status message  Score: " + score + "  Turns: " + turns + " "));
    }

    @Test
    void rightAlignEverythingExceptStatusMessage() {
        int lineLength = RandomUtils.insecure().randomInt(37, 200);
        int expectedSpaces = lineLength - 34;
        String statusMessage = "status message";
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);
        Extent size = new Extent(1, lineLength);
        when(slackTextScreen.getSize()).thenReturn(size);

        userInterface.showStatusBar(statusMessage, 0, 0, false);

        verify(slackTextScreen).setStatusBar(ArgumentMatchers.startsWith(" status message" + " ".repeat(expectedSpaces)));
    }

    @Test
    void handleNarrowLength() {
        int lineLength = RandomUtils.insecure().randomInt(37, 200);
        int expectedSpaces = lineLength - 34;
        String statusMessage = "status message";
        SlackZUserInterface userInterface = new SlackZUserInterface(slackTextScreen, null);
        Extent size = new Extent(1, 30);
        when(slackTextScreen.getSize()).thenReturn(size);

        userInterface.showStatusBar(statusMessage, 0, 0, false);

        verify(slackTextScreen).setStatusBar(ArgumentMatchers.eq(" status message  Score: 0  Turns: 0 "));
    }
}
