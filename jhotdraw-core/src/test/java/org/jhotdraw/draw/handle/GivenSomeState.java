package org.jhotdraw.draw.handle;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.figure.TextFigure;

public class GivenSomeState extends Stage<GivenSomeState> {
    @ProvidedScenarioState
    TextFigure textFigure;
    @ProvidedScenarioState
    FontSizeHandle fontSizeHandle;
    @ProvidedScenarioState
    DefaultDrawingView mockDefaultDrawingView;
    @ProvidedScenarioState
    DefaultDrawingEditor defaultDrawingEditor;
    @ProvidedScenarioState
    float size;

    public GivenSomeState i_have_a_text_of_size_x(float givenSize) {
        size = givenSize;
        textFigure = new TextFigure();
        textFigure.setFontSize(size);

        mockDefaultDrawingView = new DefaultDrawingView();
        fontSizeHandle = new FontSizeHandle(textFigure);

        defaultDrawingEditor = new DefaultDrawingEditor();
        mockDefaultDrawingView.addNotify(defaultDrawingEditor);

        DefaultDrawing drawing = new DefaultDrawing();
        drawing.add(textFigure);
        mockDefaultDrawingView.setDrawing(drawing);

        fontSizeHandle.setView(mockDefaultDrawingView);

        return self();
    }

}
