package org.jhotdraw.draw.handle;

import static org.junit.Assert.*;
import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class FontSizeHandleTest extends ScenarioTest<GivenSomeState, WhenSomeAction, ThenSomeOutcome> {
    @Test
    public void settingCorrectFontSize() {
        given().i_have_a_text_of_size_x(15);
        when().i_enlargen_the_text(25);
        then().the_text_should_be_of_size_y(25);
    }
}