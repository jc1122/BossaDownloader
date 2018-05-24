package app.gui.dialog.refactoredStatement;

import app.gui.dialog.GUIController;
import app.gui.dialog.GUIModel;
import app.gui.dialog.GUIView;

import java.beans.PropertyChangeEvent;

public class StatementView<K extends StatementModel, L extends StatementView<K, L, M>, M extends StatementController<K, L>> extends GUIView<K,L,M> {

    StatementView(M controller, K model) {
        super(controller, model);

    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    @Override
    public void createGUI() {

    }
}
