package app.gui.dialog;

import app.gui.Model;
import app.gui.dialog.SaveToCSV.SaveToCSVController;
import app.gui.dialog.SaveToCSV.SaveToCSVDialog;
import app.gui.dialog.SaveToCSV.SaveToCSVModel;
import app.gui.dialog.SaveToCSV.SaveToCSVView;
import app.gui.dialog.statement.StatementController;
import app.gui.dialog.statement.StatementDialog;
import app.gui.dialog.statement.StatementModel;
import app.gui.dialog.statement.StatementView;
import org.jetbrains.annotations.NotNull;

//enum factory
public enum GUIDialogFactory {
    ;

    @NotNull
    public static SaveToCSVDialog<SaveToCSVModel, SaveToCSVView, SaveToCSVController<SaveToCSVModel, SaveToCSVView>>
    getSaveToCSVDialog(Model model) {
        return new SaveToCSVDialog<>
                (model, SaveToCSVModel.class, SaveToCSVView.class,
                        (Class<SaveToCSVController<SaveToCSVModel, SaveToCSVView>>) (Class) SaveToCSVController.class);
    }

    public static StatementDialog<StatementModel,
            StatementView,
            StatementController<StatementModel, StatementView>
            >
    getStatementDialog(Model model) {
        return new StatementDialog<>(model, StatementModel.class, StatementView.class,
                (Class<StatementController<StatementModel, StatementView>>) (Class) StatementController.class);
    }
}
