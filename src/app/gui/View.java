package app.gui;

import app.API.BossaAPI;

import javax.swing.*;
import java.util.logging.Logger;

//TODO this class is a mess, tidy the code
public class View {
    private Controller controller;
    private Model model;
    private static final Logger logger =
            Logger.getLogger(BossaAPI.class.getName());

    private JFrame frame;

    View(Controller controller, Model model) {
        this.controller = controller;
        this.model = model;
    }

    private void createFrame() {
        frame = new JFrame("BossaDownloader");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 300);
        frame.setVisible(true);

    }

    void createGUI() {
        createFrame();

        frame.setJMenuBar(new MainMenuBarFactory("MenuBar").getContainer());

        //TODO cache gui component names and use in search
        addEventListeners();
    }

    //TODO replace indexes with something more meaningful; this will crash badly when new items are added to menubar
    private void addEventListeners() {
        JMenuItem start = frame.getJMenuBar().getMenu(0).getItem(0); // API/start
        start.addActionListener(new ActionListenerShowsDialogOnException(() -> controller.startAPI()));

        JMenuItem stop = frame.getJMenuBar().getMenu(0).getItem(2); // API/stop
        stop.addActionListener(new ActionListenerShowsDialogOnException(() -> controller.stopAPI()));

        JMenuItem statement = frame.getJMenuBar().getMenu(1).getItem(0); // Account/statement
        statement.addActionListener(new ActionListenerShowsDialogOnException(() -> controller.showStatement()));

        JMenuItem version = frame.getJMenuBar().getMenu(2).getItem(0); // Help/version
        version.addActionListener(new ActionListenerShowsDialogOnException(() -> controller.showVersion()));
    }

    void showVersionDialog() {
        JOptionPane.showMessageDialog(frame, "API showVersion: \n" + model.getAPIversion());
    }

    void showStatementDialog() {
        new SimplifiedStatementDialog(model);
    }


    void disableStartApiMenuItem() {
        frame.getJMenuBar().getMenu(0).getItem(0).setEnabled(false);
    }

    void enableStartApiMenuItem() {
        frame.getJMenuBar().getMenu(0).getItem(0).setEnabled(true);
    }

    void disableStopApiMenuItem() {
        frame.getJMenuBar().getMenu(0).getItem(2).setEnabled(false);
    }

    void enableStopApiMenuItem() {
        frame.getJMenuBar().getMenu(0).getItem(2).setEnabled(true);
    }

    public void disableStatementAccountsMenuItem() {
        frame.getJMenuBar().getMenu(1).getItem(0).setEnabled(false);
    }

    void enableStatementAccountsMenuItem() {
        frame.getJMenuBar().getMenu(1).getItem(0).setEnabled(true);
    }

    public Model getModel() {
        return model;
    }
}
