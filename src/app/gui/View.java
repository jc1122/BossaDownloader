package app.gui;

import app.API.BossaAPI;
import app.gui.menu.MainMenuBarFactory;
import app.gui.statement.StatementDialog;
import app.gui.tickers.SelectTickersDialog;

import javax.swing.*;
import java.util.logging.Logger;


public class View {
    private static final Logger logger =
            Logger.getLogger(View.class.getName());

    private Controller controller;
    private Model model;

    private JFrame frame;

    private SelectTickersDialog selectTickersDialog;

    View(Controller controller, Model model) {
        this.controller = controller;
        this.model = model;
    }

    private void createFrame() {
        logger.entering(this.getClass().getName(),"createFrame");
        frame = new JFrame("BossaDownloader");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 300);
        frame.setVisible(true);
        logger.exiting(this.getClass().getName(),"createFrame");

    }

    void createGUI() {
        logger.entering(this.getClass().getName(),"createGUI");
        createFrame();

        frame.setJMenuBar(new MainMenuBarFactory("MenuBar").getContainer());
        addEventListeners();
        logger.exiting(this.getClass().getName(),"createGUI");
    }

    //TODO replace indexes with something more meaningful; this will crash badly when new items are added to menubar
    private void addEventListeners() {
        logger.entering(this.getClass().getName(),"addEventListeners");
        JMenuItem start = frame.getJMenuBar().getMenu(0).getItem(0); // API/start
        start.addActionListener(new ActionListenerShowsDialogOnException((e) -> controller.startAPI()));

        JMenuItem stop = frame.getJMenuBar().getMenu(0).getItem(2); // API/stop
        stop.addActionListener(new ActionListenerShowsDialogOnException((e) -> controller.stopAPI()));

        JMenuItem statement = frame.getJMenuBar().getMenu(1).getItem(0); // Account/statement
        statement.addActionListener(new ActionListenerShowsDialogOnException((e) -> controller.showStatement()));

        JMenuItem select = frame.getJMenuBar().getMenu(2).getItem(0); // Tickers/Select...
        select.addActionListener(new ActionListenerShowsDialogOnException((e) -> controller.selectTickers()));

        JMenuItem version = frame.getJMenuBar().getMenu(3).getItem(0); // Help/version
        version.addActionListener(new ActionListenerShowsDialogOnException((e) -> controller.showVersion()));
        logger.exiting(this.getClass().getName(),"addEventListeners");
    }

    void showVersionDialog() {
        logger.entering(this.getClass().getName(),"showVersionDialog");
        JOptionPane.showMessageDialog(frame, "API showVersion: \n" + model.getVersion());
        logger.exiting(this.getClass().getName(),"showVersionDialog");
    }

    void showStatementDialog() {
        logger.entering(this.getClass().getName(),"showStatementDialog");
        new StatementDialog(model);
        logger.exiting(this.getClass().getName(),"showStatementDialog");
    }

    //only one dialog possible at a time
    void showSelectTickersDialog() {
        logger.entering(this.getClass().getName(),"showSelectTickersDialog");
        if (selectTickersDialog != null) {
            if (selectTickersDialog.getDialog().isVisible()) {
                logger.exiting(this.getClass().getName(),"showSelectTickersDialog", "already visible");
                return;
            }
        }
        selectTickersDialog = new SelectTickersDialog(model);
        selectTickersDialog.getDialog().setVisible(true);
        selectTickersDialog.getDialog().requestFocus();
        logger.exiting(this.getClass().getName(),"showSelectTickersDialog", "created a new dialog");
    }

    void disableStartApiMenuItem() {
        logger.entering(this.getClass().getName(),"disableStartApiMenuItem");
        frame.getJMenuBar().getMenu(0).getItem(0).setEnabled(false);
        logger.exiting(this.getClass().getName(),"disableStartApiMenuItem");
    }

    void enableStartApiMenuItem() {
        logger.entering(this.getClass().getName(),"enableStartApiMenuItem");
        frame.getJMenuBar().getMenu(0).getItem(0).setEnabled(true);
        logger.exiting(this.getClass().getName(),"enableStartApiMenuItem");
    }

    void disableStopApiMenuItem() {
        logger.entering(this.getClass().getName(),"disableStopApiMenuItem");
        frame.getJMenuBar().getMenu(0).getItem(2).setEnabled(false);
        logger.exiting(this.getClass().getName(),"disableStopApiMenuItem");
    }

    void enableStopApiMenuItem() {
        logger.entering(this.getClass().getName(),"enableStopApiMenuItem");
        frame.getJMenuBar().getMenu(0).getItem(2).setEnabled(true);
        logger.exiting(this.getClass().getName(),"enableStopApiMenuItem");
    }

    public void disableStatementAccountsMenuItem() {
        logger.entering(this.getClass().getName(),"disableStatementAccountsMenuItem");
        frame.getJMenuBar().getMenu(1).getItem(0).setEnabled(false);
        logger.exiting(this.getClass().getName(),"disableStatementAccountsMenuItem");
    }

    void enableStatementAccountsMenuItem() {
        logger.entering(this.getClass().getName(),"enableStatementAccountsMenuItem");
        frame.getJMenuBar().getMenu(1).getItem(0).setEnabled(true);
        logger.exiting(this.getClass().getName(),"enableStatementAccountsMenuItem");
    }

    public Model getModel() {
        return model;
    }
}
