package org.rdtif.zaxbot.slack;

import com.slack.api.model.block.InputBlock;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.DispatchActionConfig;
import com.slack.api.model.block.element.PlainTextInputElement;
import org.junit.jupiter.api.Test;
import org.rdtif.zaxbot.userinterface.InputMode;
import org.rdtif.zaxbot.userinterface.InputState;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

class SlackDisplayMessageMakerTest {
    private final InputState inputState = new InputState();
    private final SlackDisplayMessageMaker maker = new SlackDisplayMessageMaker(inputState);

    @Test
    void neverReturnNull() {
        List<LayoutBlock> message = maker.makeMessageOf("");

        assertThat(message, notNullValue());
    }

    @Test
    void includeTextWindow() {
        List<LayoutBlock> message = maker.makeMessageOf("");

        assertThat(message.get(0).getType(), equalTo("section"));
    }

    @Test
    void includeContentInTextWindow() {
        List<LayoutBlock> message = maker.makeMessageOf("some content");

        SectionBlock section = (SectionBlock) message.get(0);
        assertThat(section.getText().getText(), equalTo("```\nsome content\n```"));
    }

    @Test
    void includeInputWindow() {
        List<LayoutBlock> message = maker.makeMessageOf("");

        assertThat(message.get(1).getType(), equalTo("input"));
    }

    @Test
    void configureInputWindowAsFocusOnLoad() {
        List<LayoutBlock> message = maker.makeMessageOf("some content");

        PlainTextInputElement inputBoxElement = (PlainTextInputElement) ((InputBlock) message.get(1)).getElement();
        assertThat(inputBoxElement.getFocusOnLoad(), equalTo(true));
    }

    @Test
    void configureInputWindowToRespondToEnter() {
        inputState.mode = InputMode.Line;
        List<LayoutBlock> message = maker.makeMessageOf("some content");

        PlainTextInputElement inputBoxElement = (PlainTextInputElement) ((InputBlock) message.get(1)).getElement();
        DispatchActionConfig dispatchActionConfig = inputBoxElement.getDispatchActionConfig();
        assertThat(dispatchActionConfig.getTriggerActionsOn(), hasItem("on_enter_pressed"));
        assertThat(dispatchActionConfig.getTriggerActionsOn(), not(hasItem("on_character_entered")));
    }

    @Test
    void configureInputWindowToRespondToTyping() {
        inputState.mode = InputMode.Character;
        List<LayoutBlock> message = maker.makeMessageOf("some content");

        PlainTextInputElement inputBoxElement = (PlainTextInputElement) ((InputBlock) message.get(1)).getElement();
        DispatchActionConfig dispatchActionConfig = inputBoxElement.getDispatchActionConfig();
        assertThat(dispatchActionConfig.getTriggerActionsOn(), hasItem("on_character_entered"));
        assertThat(dispatchActionConfig.getTriggerActionsOn(), not(hasItem("on_enter_pressed")));
    }

    @Test
    void configureInputWindowMisc() {
        List<LayoutBlock> message = maker.makeMessageOf("some content");

        InputBlock inputBox = (InputBlock) message.get(1);
        assertThat(inputBox.isOptional(), equalTo(true));
        assertThat(inputBox.getLabel().getText(), equalTo("Command Input:"));
        assertThat(inputBox.getDispatchAction(), equalTo(true));
        assertThat(inputBox.getBlockId(), equalTo("input-window"));
    }

    @Test
    void escapeTripleBacktickInContent() {
        List<LayoutBlock> message = maker.makeMessageOf("```");

        SectionBlock section = (SectionBlock) message.get(0);
        String replaced = "`" + (char) 11 + "`" + (char) 11 + "`";
        assertThat(section.getText().getText(), equalTo("```\n" + replaced + "\n```"));
    }
}
