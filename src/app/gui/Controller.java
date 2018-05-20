package app.gui;

import javax.swing.*;
import java.util.logging.Logger;

/**
 * Part of MVC pattern. Handles user actions provided by {@link View} and informs {@link Model} about them.
 */
public class Controller {
    private static final Logger logger =
            Logger.getLogger(Controller.class.getName());

    private final Model model;
    private final View view;

    public Controller(Model model) {
        logger.entering(this.getClass().getName(), "constructor");
        this.model = model;
        view = new View(this, model);
        SwingUtilities.invokeLater(() -> {
            view.createGUI();
            view.disableStopApiMenuItem();
            view.disableStatementAccountsMenuItem();
        });
        logger.exiting(this.getClass().getName(), "constructor");
    }

    /**
     * Initialize {@link Model}
     */
    void startAPI() {
        logger.entering(this.getClass().getName(), "startAPI");
        model.initialize();
        view.enableStopApiMenuItem();
        view.enableStatementAccountsMenuItem();
        view.disableStartApiMenuItem();
        logger.exiting(this.getClass().getName(), "startAPI");
    }

    /**
     * Shut down
     */
    void stopAPI() {
        logger.entering(this.getClass().getName(), "stopAPI");
        model.shutdown();
        view.disableStopApiMenuItem();
        view.enableStartApiMenuItem();
        logger.exiting(this.getClass().getName(), "stopAPI");
    }

    void showVersion() {
        logger.entering(this.getClass().getName(), "showVersion");
        view.showVersionDialog();
        logger.exiting(this.getClass().getName(), "showVersion");
    }

    /**
     * Show information about available accounts and their details.
     */
    void showStatement() {
        logger.entering(this.getClass().getName(), "showStatement");
        view.showStatementDialog();
        logger.exiting(this.getClass().getName(), "showStatement");
    }

    /**
     * Select tickers, whose market quotes will be tracked and updated.
     */
    void selectTickers() {
        logger.entering(this.getClass().getName(), "selectTickers");
        view.showSelectTickersDialog();
        logger.exiting(this.getClass().getName(), "selectTickers");
    }


}
