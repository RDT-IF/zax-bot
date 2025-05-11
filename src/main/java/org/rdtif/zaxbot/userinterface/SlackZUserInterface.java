package org.rdtif.zaxbot.userinterface;

import com.zaxsoft.zax.zmachine.ZUserInterface;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Vector;

public class SlackZUserInterface implements ZUserInterface {
    // These are my best guess based on observations -- scaling will change the actual.
    private static final int FONT_WIDTH_IN_PIXELS = 8;
    private static final int FONT_HEIGHT_IN_PIXELS = 16;
    private static final int Z_MACHINE_BLACK = 2;
    private static final int Z_MACHINE_WHITE = 9;
    private final SlackTextScreen screen;
    private final InputState inputState;
    private int ZMachineVersion = 0;

    public SlackZUserInterface(SlackTextScreen screen, InputState inputState) {
        this.screen = screen;
        this.inputState = inputState;
    }

    @Override
    public void initialize(int ZMachineVersion) {
        System.out.println(ZMachineVersion);
        this.ZMachineVersion = ZMachineVersion;
        screen.initialize(ZMachineVersion);
        screen.update();
    }

    @Override
    public void fatal(String message) {
        screen.print(message);
        screen.update();
        throw new ZaxFatalException(message);
    }

    @Override
    public Dimension getScreenCharacters() {
        return new Dimension(screen.getSize().getColumns(), screen.getSize().getRows());
    }

    @Override
    public Dimension getFontSize() {
        return new Dimension(FONT_WIDTH_IN_PIXELS, FONT_HEIGHT_IN_PIXELS);
    }

    @Override
    public Dimension getScreenUnits() {
        Dimension fontSizeInPixels = getFontSize();
        Dimension screenSizeInCharacters = getScreenCharacters();
        return new Dimension(screenSizeInCharacters.width * fontSizeInPixels.width, screenSizeInCharacters.height * fontSizeInPixels.height);
    }

    @Override
    public boolean hasUpperWindow() {
        System.out.println("hasUpperWindow()");
        return false;
    }

    @Override
    public Dimension getWindowSize(int window) {
        System.out.println("getWindowSize( window: " + window + ")");
        throw new UnsupportedOperationException();
    }

    @Override
    public void splitScreen(int lines) {
        System.out.println("splitScreen( lines: " + lines + ")");
        screen.splitScreen(lines);
    }

    @Override
    public void setCurrentWindow(int window) {
        System.out.println("setCurrentWindow( window: " + window + ")");
        screen.setCurrentWindow(window);
    }

    @Override
    public void eraseWindow(int window) {
        System.out.println("eraseWindow( window: " + window + ")");
        screen.eraseWindow(window);
        screen.update();
    }

    @Override
    public int getDefaultForeground() {
        System.out.println("getDefaultForeground()");
        return Z_MACHINE_BLACK;
    }

    @Override
    public int getDefaultBackground() {
        System.out.println("getDefaultBackground()");
        return Z_MACHINE_WHITE;
    }

    @Override
    public boolean hasBoldface() {
        System.out.println("hasBoldface()");
        return false;
    }

    @Override
    public boolean hasFixedWidth() {
        System.out.println("hasFixedWidth()");
        return true;
    }

    @Override
    public boolean hasItalic() {
        System.out.println("hasItalic()");
        return false;
    }

    @Override
    public boolean hasTimedInput() {
        System.out.println("hasTimedInput()");
        return false;
    }

    @Override
    public boolean defaultFontProportional() {
        System.out.println("defaultFontProportional()");
        return false;
    }

    @Override
    public boolean hasColors() {
        System.out.println("hasColors()");
        return false;
    }

    @Override
    public void setTerminatingCharacters(Vector characters) {
        System.out.println("setTerminatingCharacters(characters: " + characters + ")");
    }

    @Override
    public Point getCursorPosition() {
        System.out.println("getCursorPosition()");
        return screen.getCursorPosition().toPoint();
    }

    @Override
    public boolean hasStatusLine() {
        System.out.println("hasStatusLine()");
        return (ZMachineVersion >= 1) && (ZMachineVersion <= 3);
    }

    @Override
    public void showStatusBar(String statusMessage, int a, int b, boolean flag) {
        System.out.println("showStatusBar(s:" + statusMessage + ", a:" + a + ", b:" + b + ", flag:" + flag + ")");
        String leftPart, middlePart, rightPart;

        leftPart = " " + statusMessage + " ";
        if (flag) {
            middlePart = " Time: " + a + ":";
            if (b < 10) middlePart += "0";
            middlePart = middlePart + b;
            rightPart = " ";
        } else {
            middlePart = " Score: " + a + " ";
            rightPart = " Turns: " + b + " ";
        }

        int allPartsLength = leftPart.length() + middlePart.length() + rightPart.length();
        int paddingRequired = screen.getSize().getColumns() - allPartsLength;
        String padding = " ".repeat(Math.min(0, paddingRequired));

        String statusBar = leftPart + padding + middlePart + rightPart;
        screen.setStatusBar(statusBar);
    }

    @Override
    public void setCursorPosition(int x, int y) {
        System.out.println("setCursorPosition(" + x + ", " + y + ")");
        screen.setCursorPosition(x, y);
    }

    @Override
    public void setColor(int foreground, int background) {
        System.out.println("setColor( foreground: " + foreground + ", background: " + background + ")");
    }

    @Override
    public void setTextStyle(int style) {
        System.out.println("setTextStyle(style: " + style + ")");
        screen.setTextStyle(style);
    }

    @Override
    public void setFont(int font) {
        System.out.println("setFont( font: " + font + ")");
    }

    @Override
    public int readChar(int time) {
        System.out.println("readChar(time: " + time + ")");
        inputState.mode = InputMode.Character;
        screen.update();
        while (inputState.currentInput.isEmpty()) {
            try {
                // I know, this seems pointless, but it makes it work. Its likely that the compiler is mistakenly optimizing this away
                //noinspection BusyWait
                Thread.sleep(0);
            } catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }
        }
        System.out.println("Current Input: " + inputState.currentInput);
        char c = inputState.currentInput.charAt(0);
        inputState.currentInput = "";
        return c;
    }

    @Override
    public int readLine(StringBuffer buffer, int time) {
        System.out.println("readLine(time: " + time + ")");
        inputState.mode = InputMode.Line;
        screen.update();
        while (inputState.currentInput.isEmpty()) {
            try {
                // I know, this seems pointless, but it makes it work. Its likely that the compiler is mistakenly optimizing this away
                //noinspection BusyWait
                Thread.sleep(0);
            } catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }
        }
        buffer.append(inputState.currentInput).append('\0');
        inputState.currentInput = "";
        return 0;
    }

    @Override
    public void showString(String string) {
        System.out.println("showString( string: " + string + ")");
        screen.print(string);
    }

    @Override
    public void scrollWindow(int lines) {
        System.out.println("ScrollWindow(lines: " + lines + ")");
    }

    @Override
    public void eraseLine(int size) {
        System.out.println("Erase line(size: " + size + ")");
    }

    @Override
    public String getFilename(String title, String suggested, boolean saveFlag) {
        System.out.println("getFilename(title: " + title + ", suggested: " + suggested + ", saveFlag: " + saveFlag + ")");
        return "";
    }

    @Override
    public void quit() {
        System.out.println("quit()");
    }

    @Override
    public void restart() {
        System.out.println("restart()");
    }
}
