package org.jhotdraw.gui.action;

import junit.framework.TestCase;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.gui.JFontChooser;

import javax.swing.*;
import javax.swing.undo.UndoableEdit;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class FontChooserHandlerTest extends TestCase {
    private FontChooserHandler handler;
    private DrawingEditor editorMock;
    private DrawingView viewMock;
    private JFontChooser fontChooserMock;
    private JPopupMenu popupMenuMock;
    private AttributeKey<Font> fontKey;

    @Override
    public void setUp() throws Exception {
        editorMock = mock(DrawingEditor.class);
        viewMock = mock(DrawingView.class);
        fontChooserMock = mock(JFontChooser.class);
        popupMenuMock = mock(JPopupMenu.class);
        fontKey = AttributeKeys.FONT_FACE;

        when(editorMock.getActiveView()).thenReturn(viewMock);

        handler = new FontChooserHandler(editorMock, fontKey, fontChooserMock, popupMenuMock);
    }

    ///Most important Domain logic - Task 2
    public void testApplySelectedFontToFigures() {
        Set<Figure> selectedFigures = new HashSet<>();
        Figure mockFigure = mock(Figure.class);
        selectedFigures.add(mockFigure);

        Drawing mockDrawing = mock(Drawing.class);

        when(handler.getView().getSelectedFigures()).thenReturn(selectedFigures);
        when(handler.getDrawing()).thenReturn(mockDrawing);
        when(fontChooserMock.getSelectedFont()).thenReturn(new Font("Arial", Font.PLAIN, 12));

        handler.applySelectedFontToFigures();

        verify(mockFigure).set(fontKey, fontChooserMock.getSelectedFont());
        verify(editorMock).setDefaultAttribute(fontKey, fontChooserMock.getSelectedFont());
        verify(mockDrawing).fireUndoableEditHappened(any(UndoableEdit.class));
    }

    ///Most important Domain logic - Task 2
    public void testCollectRestoreData() {
        ArrayList<Figure> selectedFigures = new ArrayList<>();
        Figure mockFigure1 = mock(Figure.class);
        Figure mockFigure2 = mock(Figure.class);
        selectedFigures.add(mockFigure1);
        selectedFigures.add(mockFigure2);

        Object restoreData1 = new Object();
        Object restoreData2 = new Object();

        when(mockFigure1.getAttributesRestoreData()).thenReturn(restoreData1);
        when(mockFigure2.getAttributesRestoreData()).thenReturn(restoreData2);

        ArrayList<Object> result = handler.collectRestoreData(selectedFigures);

        assertEquals(2, result.size());
        assertEquals(restoreData1, result.get(0));
        assertEquals(restoreData2, result.get(1));
    }

    ///Most important Domain logic - Task 2
    public void testApplyFont() {
        ArrayList<Figure> selectedFigures = new ArrayList<>();
        Figure mockFigure = mock(Figure.class);
        selectedFigures.add(mockFigure);

        when(fontChooserMock.getSelectedFont()).thenReturn(new Font("Arial", Font.PLAIN, 12));

        handler.applyFont(selectedFigures);

        verify(mockFigure).willChange();
        verify(mockFigure).set(fontKey, fontChooserMock.getSelectedFont());
        verify(mockFigure).changed();
    }

    ///Best case scenario - Task 3
    public void testConstructorInit() {
        verify(fontChooserMock).addActionListener(handler);
        verify(fontChooserMock).addPropertyChangeListener(handler);

        assertNotNull(handler);
    }

    ///Best case scenario - Task 3
    public void testActionPerformedWithApproveSelection() {
        ActionEvent mockEvent = new ActionEvent(fontChooserMock, ActionEvent.ACTION_PERFORMED, JFontChooser.APPROVE_SELECTION);
        Drawing mockDrawing = mock(Drawing.class);

        Figure mockFigure = mock(Figure.class);
        Set<Figure> selectedFigures = new HashSet<>();
        selectedFigures.add(mockFigure);

        FontChooserHandler spyHandler = spy(handler);
        when(spyHandler.getDrawing()).thenReturn(mockDrawing);
        when(spyHandler.getView().getSelectedFigures()).thenReturn(selectedFigures);
        when(fontChooserMock.getSelectedFont()).thenReturn(new Font("Arial", Font.PLAIN, 12));

        spyHandler.actionPerformed(mockEvent);

        verify(spyHandler).applySelectedFontToFigures();
        verify(popupMenuMock).setVisible(false);
    }

    ///Best case scenario - Task 3
    public void testUpdateEnabledStateWithoutSelectedFigures() {
        DrawingView mockView = mock(DrawingView.class);
        when(editorMock.getActiveView()).thenReturn(mockView);
        when(mockView.getSelectionCount()).thenReturn(0);

        handler.updateEnabledState();

        verify(fontChooserMock, atLeastOnce()).setEnabled(false);
        verify(popupMenuMock, atLeastOnce()).setEnabled(false);
    }

    ///Identified boundary case - Task 4
    public void testApplySelectedFontToFiguresWithNoSelectedFigures() {
        Drawing mockDrawing = mock(Drawing.class);
        when(handler.getDrawing()).thenReturn(mockDrawing);
        when(handler.getView().getSelectedFigures()).thenReturn(new HashSet<>());

        try {
            handler.applySelectedFontToFigures();
        } catch (AssertionError e) {
            verify(fontChooserMock, never()).getSelectedFont();
            verify(mockDrawing, never()).fireUndoableEditHappened(any());
        }
    }

    ///Identified boundary case - Task 4
    public void testApplyFontWithNullFont() {
        // Arrange
        Figure mockFigure = mock(Figure.class);
        when(fontChooserMock.getSelectedFont()).thenReturn(null); // Font is null
        ArrayList<Figure> selectedFigures = new ArrayList<>(Collections.singletonList(mockFigure));
        when(fontChooserMock.getSelectedFont()).thenReturn(new Font(null));
        // Act
        handler.applyFont(selectedFigures);

        // Assert - ensure `set` is never called on figures with a null font
        verify(mockFigure, never()).set(any(AttributeKey.class), eq(null));
    }

    ///Identified boundary case - Task 4
    public void testPropertyChangeWithUnexpectedPropertyName() {
        PropertyChangeEvent event = new PropertyChangeEvent(fontChooserMock, "unexpectedProperty", null, null);
        FontChooserHandler spyHandler = spy(handler);
        spyHandler.propertyChange(event);

        verify(spyHandler, never()).applySelectedFontToFigures();
    }
}
