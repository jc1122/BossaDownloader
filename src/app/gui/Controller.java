package app.gui;

import javax.swing.*;

public class Controller {
    Model model;
    View view;

    public Controller(Model model) {
        this.model = model;
        view = new View(this, model);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view.createGUI();
                view.disableStopApiMenuItem();
                //view.disableStatementAccountsMenuItem(); TODO uncomment
            }
        });
    }

    public void startAPI() {
        model.startAPI();
        view.enableStopApiMenuItem();
        view.enableStatementAccountsMenuItem();
        view.disableStartApiMenuItem();
    }

    public void stopAPI() {
        model.stopAPI();
        view.disableStopApiMenuItem();
        view.enableStartApiMenuItem();
    }

    public void showVersion() {
        view.showVersionDialog();
    }

    public void showStatement() {
        view.showStatementDialog();
    }
    //TODO add status bar and logging of init functions
//TODO add GUI exception handling
    //TODO add window with statement


}
