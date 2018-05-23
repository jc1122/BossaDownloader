package app.gui.dialog.generifiedSaveToCSV;

import app.gui.Model;
import app.gui.dialog.GUIDialog;

//TODO test this dialog, especially property listeners
//TODO on dialog close, remove listeners
public class SaveToCSVDialog<K extends SaveToCSVModel, L extends SaveToCSVView, M extends SaveToCSVController<K, L>>
        extends GUIDialog<K, L, M> {

    public SaveToCSVDialog(Model model, Class<K> modelClass, Class<L> viewClass, Class<M> controllerClass) {
        super(model, modelClass, viewClass, controllerClass);
    }

}
