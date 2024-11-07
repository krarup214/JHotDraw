package org.jhotdraw.gui.action;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.figure.TextFigure;
import org.jhotdraw.gui.JFontChooser;
import java.awt.*;

public class WhenSomeAction extends Stage<WhenSomeAction> {
    @ExpectedScenarioState
    JFontChooser jFontChooser;
    @ExpectedScenarioState
    Font font;
    @ExpectedScenarioState
    TextFigure textFigure;

    public WhenSomeAction i_set_the_font_to(Font providedFont) {
        font = providedFont;

        jFontChooser.setSelectedFont(font);
        textFigure.set(AttributeKeys.FONT_FACE, jFontChooser.getSelectedFont());
        return self();
    }

    public WhenSomeAction i_enable_bold() {
        textFigure.set(AttributeKeys.FONT_BOLD, true);
        return self();
    }
}