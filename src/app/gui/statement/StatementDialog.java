package app.gui.statement;

import app.gui.Model;

import javax.swing.*;
import java.util.logging.Logger;

//TODO refactor to MVC
public class StatementDialog {
    private static final Logger logger =
            Logger.getLogger(StatementDialog.class.getName());

    private final JDialog dialog = new JDialog();
    private Model model;
    private PositionsPane positionsPane;

    //TODO this method is an ugly hack; should refactor this to a listener and change PositionsPane constructor
    public void resize() {
        dialog.setSize(dialog.getPreferredSize());
    }

    public StatementDialog(Model model) {
        this.model = model;
        logger.entering(this.getClass().getName(), "constructor", model);
        dialog.setTitle("Statement");
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

        AccountPane accountPane = new AccountPane(model);
        StatementPane statementPane = new StatementPane(model);
        positionsPane = new PositionsPane(model, this);

        accountPane.addAccountSelectionListener(statementPane);
        accountPane.addAccountSelectionListener(positionsPane);

        dialog.add(accountPane.getPane());
        dialog.add(statementPane.getPane());
        dialog.add(positionsPane.getPane());

        dialog.pack();
        dialog.setResizable(false);
        SwingUtilities.invokeLater(() -> dialog.setVisible(true));
        logger.exiting(this.getClass().getName(), "constructor");
    }
}
