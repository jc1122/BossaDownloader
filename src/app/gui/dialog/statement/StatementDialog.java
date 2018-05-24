package app.gui.dialog.statement;

import app.gui.Model;
import app.gui.dialog.GUIDialog;

//TODO now refactor this whole class
public class StatementDialog<K extends StatementModel,
        L extends StatementView,
        M extends StatementController<K, L>
        > extends GUIDialog<K,L,M> {

    public StatementDialog(Model model, Class<K> modelClass, Class<L> viewClass, Class<M> controllerClass) {
        super(model, modelClass, viewClass, controllerClass);

    }

}
