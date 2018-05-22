package app.gui.dialog.generifiedSaveToCSV;


import app.gui.dialog.GUIController;

public class SaveToCSVController<K extends SaveToCSVModel, L extends SaveToCSVView> extends GUIController<K, L> {

    SaveToCSVController(K model, Class<L> viewClass) {
        super(model, viewClass);
    }

    void startSaving() {
        model.startSaving();
        view.setStopSavingEnabled(true);
        view.setStartSavingEnabled(false);
    }

    void stopSaving() {
        model.stopSaving();
        view.setStopSavingEnabled(false);
        view.setStartSavingEnabled(true);
    }
}
