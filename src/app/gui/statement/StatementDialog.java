package app.gui.statement;

import app.gui.Model;

import javax.swing.*;

public class StatementDialog {
    JDialog dialog = new JDialog();

    //TODO this method is an ugly hack; should refactor this to a listener and change PositionsPane constructor
    public void resize() {
        dialog.setSize(dialog.getPreferredSize());
    }

    public StatementDialog(Model model) {

        dialog.setTitle("Statement");
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

        AccountPane accountPane = new AccountPane(model);
        StatementPane statementPane = new StatementPane(model);
        PositionsPane positionsPane = new PositionsPane(model, this);

        accountPane.addAccountSelectionListener(statementPane);
        accountPane.addAccountSelectionListener(positionsPane);

        dialog.add(accountPane.getPane());
        dialog.add(statementPane.getPane());
        dialog.add(positionsPane.getPane());

        dialog.pack();
        dialog.setResizable(false);
        SwingUtilities.invokeLater(() -> dialog.setVisible(true));
    }
}
