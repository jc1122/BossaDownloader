package app.gui.dialog.refactoredTickerSelector;

import app.API.BossaAPI;
import app.API.TypeOfList;
import app.gui.Model;
import app.gui.dialog.GUIController;
import app.gui.dialog.GUIDialog;
import app.gui.dialog.GUIModel;
import app.gui.dialog.GUIView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

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
