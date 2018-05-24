package app.gui.dialog.statement;

import app.gui.dialog.GUIView;

import javax.swing.*;
import java.beans.PropertyChangeEvent;

public class StatementView<K extends StatementModel,
        L extends StatementView<K, L, M>,
        M extends StatementController<K, L>>
        extends GUIView<K, L, M> {
    private PositionsPane positionsPane;

    StatementView(M controller, K model) {
        super(controller, model);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    @Override
    public void createGUI() {
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
        dialog.setVisible(true);
    }
}
