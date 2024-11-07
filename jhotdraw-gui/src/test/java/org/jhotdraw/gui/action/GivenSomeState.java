package org.jhotdraw.gui.action;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.figure.TextFigure;
import org.jhotdraw.gui.JFontChooser;

import java.awt.*;

public class GivenSomeState extends Stage<GivenSomeState> {
    @ProvidedScenarioState
    JFontChooser jFontChooser;
    @ProvidedScenarioState
    TextFigure textFigure;
    @ProvidedScenarioState
    Font font;

    public GivenSomeState the_current_font_style_is(Font providedFont) {
        font = providedFont;
        jFontChooser = new JFontChooser();
        textFigure = new TextFigure();

        jFontChooser.setSelectedFont(font);
        return self();
    }

    public GivenSomeState i_have_some_text(String text) {
        textFigure = new TextFigure();
        textFigure.setText(text);
        return self();
    }
}