package app.gui.saveToCSV;

import javax.swing.*;

//TODO test this dialog, especially property listeners
//TODO on dialog close, remove listeners
public class SaveToCSVDialog {
    SaveToCSVModel model;
    SaveToCSVView view;
    SaveToCSVController controller;

    public SaveToCSVDialog(app.gui.Model model) {
        this.model = new SaveToCSVModel(model);
        controller = new SaveToCSVController(this.model);
        this.view = controller.getView();
    }
    public JDialog getDialog() {
        return view.getDialog();
    }
}
