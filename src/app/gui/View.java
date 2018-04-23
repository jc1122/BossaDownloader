package app.gui;

import app.API.BossaAPI;

import javax.swing.*;
import java.util.logging.Logger;

//TODO this class is a mess, tidy the code
public class View {
    Controller controller;
    Model model;
    private static final Logger logger =
            Logger.getLogger(BossaAPI.class.getName());

    private JFrame frame;
    private JMenuBar menuBar;

    private JMenu apiMenu;
    private JMenuItem startApiMenuItem;
    private JMenuItem logsApiMenuItem;
    private JMenuItem stopApiMenuItem;

    private JMenu accountsMenu;
    private JMenuItem statementAccountsMenuItem;

    private JMenu helpMenu;
    private JMenuItem versionHelpMenuitem;

    public View(Controller controller, Model model) {
        this.controller = controller;
        this.model = model;
    }

    public void createGUI() {
        frame = new JFrame("BossaDownloader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(createMainMenubar());

        frame.setSize(600, 300);
        frame.setVisible(true);

        addEventListeners();
    }


    private JMenuBar createMainMenubar() {
        menuBar = new JMenuBar();

        {
            apiMenu = new JMenu("API");
            startApiMenuItem = new JMenuItem("Start");
            logsApiMenuItem = new JMenuItem("Logs");
            stopApiMenuItem = new JMenuItem("Stop");
            apiMenu.add(startApiMenuItem);
            apiMenu.add(logsApiMenuItem);
            apiMenu.add(stopApiMenuItem);

            menuBar.add(apiMenu);
        }

        {
            JMenu accountsMenu = new JMenu("Accounts");

            statementAccountsMenuItem = new JMenuItem("Statement");
            accountsMenu.add(statementAccountsMenuItem);

            menuBar.add(accountsMenu);
        }

        {
            helpMenu = new JMenu("Help");
            versionHelpMenuitem = new JMenuItem("Version");
            helpMenu.add(versionHelpMenuitem);

            menuBar.add(helpMenu);
        }
        return menuBar;
    }

    private void addEventListeners() {
        startApiMenuItem.addActionListener(new ActionListenerShowsDialogOnException(() -> controller.startAPI()));
        stopApiMenuItem.addActionListener(new ActionListenerShowsDialogOnException(() -> controller.stopAPI()));
        versionHelpMenuitem.addActionListener(new ActionListenerShowsDialogOnException(() -> controller.showVersion()));
        statementAccountsMenuItem.addActionListener(new ActionListenerShowsDialogOnException(() -> controller.showStatement()));
    }

    public void showVersionDialog() {
        JOptionPane.showMessageDialog(frame, "API showVersion: \n" + model.getAPIversion());
    }

    public void showStatementDialog() {
        new StatementDialog(this);
    }


    public void disableStartApiMenuItem() {
        startApiMenuItem.setEnabled(false);
    }

    public void enableStartApiMenuItem() {
        startApiMenuItem.setEnabled(true);
    }

    public void disableStopApiMenuItem() {
        stopApiMenuItem.setEnabled(false);
    }

    public void enableStopApiMenuItem() {
        stopApiMenuItem.setEnabled(true);
    }

    public void disableStatementAccountsMenuItem() { statementAccountsMenuItem.setEnabled(false);}

    public void enableStatementAccountsMenuItem() { statementAccountsMenuItem.setEnabled(true);}
}
