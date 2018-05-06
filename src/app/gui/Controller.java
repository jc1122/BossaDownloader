package app.gui;

import javax.swing.*;

public class Controller {
    private Model model;
    private View view;

    public Controller(Model model) {
        this.model = model;
        view = new View(this, model);
        SwingUtilities.invokeLater(() -> {
            view.createGUI();
            view.disableStopApiMenuItem();
            view.disableStatementAccountsMenuItem();
        });
    }

    void startAPI() {
        model.startAPI();
        view.enableStopApiMenuItem();
        view.enableStatementAccountsMenuItem();
        view.disableStartApiMenuItem();
    }

    void stopAPI() {
        model.stopAPI();
        view.disableStopApiMenuItem();
        view.enableStartApiMenuItem();
    }

    void showVersion() {
        view.showVersionDialog();
    }

    void showStatement() {
        view.showStatementDialog();
    }

    void selectTickers() {
        view.showSelectTickersDialog();
    }


}
