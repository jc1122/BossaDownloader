package app.gui.dialog;

import app.gui.Model;
import app.gui.dialog.SaveToCSV.SaveToCSVController;
import app.gui.dialog.SaveToCSV.SaveToCSVDialog;
import app.gui.dialog.SaveToCSV.SaveToCSVModel;
import app.gui.dialog.SaveToCSV.SaveToCSVView;
import org.jetbrains.annotations.NotNull;

//enum factory
public enum GUIDialogFactory {
    ;

    @NotNull
    public static SaveToCSVDialog<SaveToCSVModel, SaveToCSVView, SaveToCSVController<SaveToCSVModel, SaveToCSVView>> getSaveToCSVDialog(Model model) {
        return new SaveToCSVDialog<>
                (model, SaveToCSVModel.class, SaveToCSVView.class,
                        (Class<SaveToCSVController<SaveToCSVModel, SaveToCSVView>>) (Class) SaveToCSVController.class);
    }
}
