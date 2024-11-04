/**
 * @(#)FontChooserHandler.java
 *
 * Copyright (c) 2008 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.gui.action;

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPopupMenu;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoableEdit;

import org.jhotdraw.draw.*;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.draw.action.AbstractSelectedAction;
import org.jhotdraw.gui.JFontChooser;

/**
 * FontChooserHandler.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class FontChooserHandler extends AbstractSelectedAction
        implements PropertyChangeListener {

    private static final long serialVersionUID = 1L;
    protected AttributeKey<Font> key;
    protected JFontChooser fontChooser;
    protected JPopupMenu popupMenu;
    protected int isUpdating;
    protected String noFontAssertErr = "no font selected";
    /**
     * Creates a new instance.
     */
    public FontChooserHandler(DrawingEditor editor, AttributeKey<Font> key, JFontChooser fontChooser, JPopupMenu popupMenu) {
        super(editor);
        this.key = key;
        this.fontChooser = fontChooser;
        this.popupMenu = popupMenu;
        fontChooser.addActionListener(this);
        fontChooser.addPropertyChangeListener(this);
        updateEnabledState();
    }

    @Override
    public DrawingView getView() {
        return super.getView();
    }

    @Override
    public Drawing getDrawing() {
        return super.getDrawing();
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getActionCommand().equals(JFontChooser.APPROVE_SELECTION)) {
            applySelectedFontToFigures();
        }
        popupMenu.setVisible(false);
    }

    protected void applySelectedFontToFigures() {
        final ArrayList<Figure> selectedFigures = new ArrayList<>(getView().getSelectedFigures());
        final ArrayList<Object> restoreData = collectRestoreData(selectedFigures);

        assert !selectedFigures.isEmpty() : "No selected figures";
        assert fontChooser.getSelectedFont() != null : noFontAssertErr;

        applyFont(selectedFigures);
        getEditor().setDefaultAttribute(key, fontChooser.getSelectedFont());

        UndoableEdit edit = createUndoableEdit(selectedFigures, restoreData);
        fireUndoableEditHappened(edit);
    }

    public ArrayList<Object> collectRestoreData(ArrayList<Figure> selectedFigures) {
        final ArrayList<Object> restoreData = new ArrayList<>(selectedFigures.size());
        for (Figure figure : selectedFigures) {
            restoreData.add(figure.getAttributesRestoreData());
        }
        return restoreData;
    }

    public void applyFont(ArrayList<Figure> selectedFigures) {
        assert fontChooser.getSelectedFont() != null : noFontAssertErr;

        for (Figure figure : selectedFigures) {
            figure.willChange();
            figure.set(key, fontChooser.getSelectedFont());
            figure.changed();
        }
    }

    private UndoableEdit createUndoableEdit(ArrayList<Figure> selectedFigures, ArrayList<Object> restoreData) {
        assert fontChooser.getSelectedFont() != null : noFontAssertErr;
        final Font undoValue = fontChooser.getSelectedFont();
        return new AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getPresentationName() {
                return AttributeKeys.FONT_FACE.getPresentationName();
            }

            @Override
            public void undo() {
                super.undo();
                Iterator<Object> iRestore = restoreData.iterator();
                for (Figure figure : selectedFigures) {
                    figure.willChange();
                    figure.restoreAttributesTo(iRestore.next());
                    figure.changed();
                }
            }

            @Override
            public void redo() {
                super.redo();
                for (Figure figure : selectedFigures) {
                    figure.willChange();
                    figure.set(key, undoValue);
                    figure.changed();
                }
            }
        };
    }

    @Override
    protected void updateEnabledState() {
        setEnabled(getEditor().isEnabled());
        if (getView() != null && fontChooser != null && popupMenu != null) {
            fontChooser.setEnabled(getView().getSelectionCount() > 0);
            popupMenu.setEnabled(getView().getSelectionCount() > 0);
            isUpdating++;
            if (getView().getSelectionCount() > 0 /*&& fontChooser.isShowing()*/) {
                for (Figure f : getView().getSelectedFigures()) {
                    if (f instanceof TextHolderFigure) {
                        TextHolderFigure thf = (TextHolderFigure) f;
                        fontChooser.setSelectedFont(thf.getFont());
                        break;
                    }
                }
            }
            isUpdating--;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (isUpdating++ == 0 && evt.getPropertyName().equals(JFontChooser.SELECTED_FONT_PROPERTY)) {
            applySelectedFontToFigures();
        }
        isUpdating--;
    }
}
