package app.gui.dialog.refactoredTickerSelector;

import app.gui.Model;
import app.gui.dialog.GUIDialog;

import javax.swing.*;

/**
 * You need to {@link SelectTickersDialog#getDialog()} then {@link JDialog#setVisible(boolean)}
 * manually to display the dialog.
 */
//TODO refactor to MVC
public class SelectTickersDialog<K extends SelectTickersModel,
        L extends SelectTickersView,
        M extends SelectTickersController<K, L>> extends GUIDialog<K,L,M> {

    public SelectTickersDialog(Model model, Class<K> modelClass, Class<L> viewClass, Class<M> controllerClass) {
        super(model, modelClass, viewClass, controllerClass);
    }
}
