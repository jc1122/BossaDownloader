package app.gui.refactoredStatement;

import app.gui.Model;

import javax.swing.*;
import java.util.logging.Logger;

public class StatementDialog {
    private static final Logger logger =
            Logger.getLogger(StatementDialog.class.getName());

    private final JDialog dialog = new JDialog();
    private StatementModel model;
    private StatementView view;
    private StatementController controller;

    public StatementDialog(Model model) {
        this.model = new StatementModel(model);
        controller = new StatementController(this.model);
        this.view = controller.getView();
    }

    public JDialog getDialog() {
        return view.getDialog();
    }
}
