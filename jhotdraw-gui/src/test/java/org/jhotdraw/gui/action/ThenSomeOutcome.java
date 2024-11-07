package org.jhotdraw.gui.action;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.figure.TextFigure;

import java.awt.*;

import static org.assertj.core.api.Assertions.*;

public class ThenSomeOutcome extends Stage<ThenSomeOutcome> {
    @ExpectedScenarioState
    Font font;
    @ExpectedScenarioState
    TextFigure textFigure;

    public ThenSomeOutcome the_text_should_reflect_the_new_font() {
        assertThat(textFigure.getFont()).isEqualTo(font);
        return self();
    }

    public ThenSomeOutcome the_text_should_now_be_bold() {
        assertThat(textFigure.get(AttributeKeys.FONT_BOLD)).isEqualTo(true);
        return self();
    }
}