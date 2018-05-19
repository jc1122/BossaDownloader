package app.gui;

import javax.swing.*;

/**
 * Part of MVC pattern. Handles user actions provided by {@link View} and informs {@link Model} about them.
 */
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

    /**
     * Initialize {@link Model}
     */
    void startAPI() {
        model.initialize();
        view.enableStopApiMenuItem();
        view.enableStatementAccountsMenuItem();
        view.disableStartApiMenuItem();
    }

    /**
     * Shut down
     */
    void stopAPI() {
        model.shutdown();
        view.disableStopApiMenuItem();
        view.enableStartApiMenuItem();
    }

    void showVersion() {
        view.showVersionDialog();
    }

    /**
     * Show information about available accounts and their details.
     */
    void showStatement() {
        view.showStatementDialog();
    }

    /**
     * Select tickers, whose market quotes will be tracked and updated.
     */
    void selectTickers() {
        view.showSelectTickersDialog();
    }


}
