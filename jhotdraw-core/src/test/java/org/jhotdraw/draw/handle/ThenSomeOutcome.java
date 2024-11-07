package org.jhotdraw.draw.handle;

import static org.assertj.core.api.Assertions.*;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.figure.TextFigure;

public class ThenSomeOutcome extends Stage<ThenSomeOutcome> {
    @ExpectedScenarioState
    TextFigure textFigure;

    public ThenSomeOutcome the_text_should_be_of_size_y(float expectedSize) {
        assertThat(textFigure.getFontSize()).isEqualTo(expectedSize);
        return self();
    }
}