package app.gui.dialog.statement;

import app.gui.dialog.GUIController;

public class StatementController<K extends StatementModel, L extends StatementView> extends GUIController<K,L> {
    StatementController(K model, Class<L> viewClass) {
        super(model, viewClass);
    }
}
