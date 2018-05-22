package app.gui.dialog;

import app.gui.Model;

public class GUIDialogFactory {
    GUIDialog createDialog(Model model, String type) {
        switch(type) {
            case "saveToCSV":
                return new GUIDialog<GUIModel, GUIView, GUIController>(model);
        }
    }
}
