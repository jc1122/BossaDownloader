package app.gui.saveToCSV;

import javax.swing.*;

public class SaveToCSVController {
    SaveToCSVModel model;
    SaveToCSVView view;
    SaveToCSVController(SaveToCSVModel model) {
        this.model = model;
        view = new SaveToCSVView(this, model);
        SwingUtilities.invokeLater(view::createGUI);
    }

    SaveToCSVView getView() {
        return view;
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
