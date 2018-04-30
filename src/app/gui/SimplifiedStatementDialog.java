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

        accountPane.addAccountSelectionListener(statementPane);

        dialog.add(accountPane.getPane());
        dialog.add(statementPane.getPane());

        dialog.pack();
        dialog.setResizable(false);
        dialog.setVisible(true);
    }
}
