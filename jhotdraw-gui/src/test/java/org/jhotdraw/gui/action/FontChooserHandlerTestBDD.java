package org.jhotdraw.gui.action;
import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

import java.awt.*;

public class FontChooserHandlerTestBDD extends ScenarioTest<GivenSomeState, WhenSomeAction, ThenSomeOutcome> {
    @Test
    public void settingNewFontFace() {
        given().the_current_font_style_is(new Font("Times New Roman", Font.PLAIN, 12));
        when().i_set_the_font_to(new Font("Arial", Font.PLAIN, 12));
        then().the_text_should_reflect_the_new_font();
    }

    @Test
    public void setTextBold() {
        given().i_have_some_text("This is a test");
        when().i_enable_bold();
        then().the_text_should_now_be_bold();
    }
}