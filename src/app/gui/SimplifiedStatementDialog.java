package app.gui;

import javax.swing.*;

public class SimplifiedStatementDialog {
    private JDialog dialog;


    SimplifiedStatementDialog(Model model) {
        dialog = new JDialog();
        dialog.setTitle("Statement");
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

        AccountPane accountPane = new AccountPane(model);
        StatementPane statementPane = new StatementPane(model);
        PositionsPane positionsPane = new PositionsPane(model);

        accountPane.addAccountSelectionListener(statementPane);
        accountPane.addAccountSelectionListener(positionsPane);

        dialog.add(accountPane.getPane());
        dialog.add(statementPane.getPane());
        dialog.add(positionsPane.getPane());

        dialog.pack();
        dialog.setResizable(false);
        dialog.setVisible(true);
    }
}
