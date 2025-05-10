package org.rdtif.zaxbot.slack;

import com.slack.api.bolt.context.builtin.ActionContext;
import com.slack.api.bolt.request.builtin.BlockActionRequest;
import com.slack.api.bolt.response.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.rdtif.zaxbot.userinterface.InputState;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Disabled
class InputWindowBlockActionHandlerTest {
    // TODO: Tests against this class
    @Test
    void acknowledgeSlackEvent() {
        InputWindowBlockActionHandler handler = new InputWindowBlockActionHandler(new InputState());
        Response response = handler.apply(new BlockActionRequest("", "", null), new ActionContext());

        assertThat(response.getStatusCode(), equalTo(200));
    }
}
