package org.jhotdraw.draw.handle;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import java.awt.*;

public class WhenSomeAction extends Stage<WhenSomeAction> {
    @ExpectedScenarioState
    FontSizeHandle fontSizeHandle;
    @ExpectedScenarioState
    float size;

    public WhenSomeAction i_enlargen_the_text(float setFontSize) {
        Point lead = new Point(5, Math.round(setFontSize));
        Point anchor = new Point(2, Math.round(size));

        fontSizeHandle.trackStart(anchor, 1024);
        fontSizeHandle.trackStep(anchor, lead, 1024);
        fontSizeHandle.trackEnd(anchor, lead, 1024);

        return self();
    }
}
