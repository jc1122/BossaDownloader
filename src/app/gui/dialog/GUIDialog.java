package app.gui.dialog;

import app.gui.Model;
import app.gui.refactoredStatement.StatementController;
import app.gui.refactoredStatement.StatementModel;
import app.gui.refactoredStatement.StatementView;

import javax.swing.*;

public class GUIDialog<K extends GUIModel, L extends GUIView, M extends GUIController> {
    private final JDialog dialog = new JDialog();
    private K model;
    private L view;
    private M controller;

    private Class<K> modelClass;
    private Class<L> viewClass;
    private Class<M> controllerClass;

    public GUIDialog(Model model) {
        this.model = new StatementModel(model);
        controller = new StatementController(this.model);
        this.view = controller.getView();
    }

    public JDialog getDialog() {
        return view.getDialog();
    }
}
