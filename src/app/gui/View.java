package app.gui;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class View {
    Controller controller;
    Model model;

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

    private interface SimpleActionListenerFunctionalHelper {
        void execute();
    }

    private class SimpleActionListener implements ActionListener {
        SimpleActionListenerFunctionalHelper helper;

        SimpleActionListener(SimpleActionListenerFunctionalHelper helper) {
            this.helper = helper;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Nullable
                @Override
                protected Void doInBackground() {
                    try {
                        helper.execute();
                    } catch (Throwable exc) {
                        showExceptionDialog(exc);
                    }
                    return null;
                }
            };
            worker.execute();
        }

    }
    private void addEventListeners() {
        startApiMenuItem.addActionListener(new SimpleActionListener(() -> controller.startAPI()));
        stopApiMenuItem.addActionListener(new SimpleActionListener(() -> controller.stopAPI()));
        versionHelpMenuitem.addActionListener(new SimpleActionListener(() -> controller.showVersion()));
        statementAccountsMenuItem.addActionListener(new SimpleActionListener(() -> controller.showStatement()));
    }

    public void showVersionDialog() {
        JOptionPane.showMessageDialog(frame, "API showVersion: \n" + model.getAPIversion());
    }

    public void showStatementDialog() {
        new StatementDialog();
    }

    private class StatementDialog implements Observer {
        private JDialog dialog;
        private JComboBox<String> comboBox;
        StatementDialog() {
            String[] accounts = {"a","b"};
            dialog = new JDialog(frame, "Statement");
            comboBox = new JComboBox<>(accounts);
            dialog.add(comboBox);

            dialog.setSize(new Dimension(300, 150));
            dialog.setLocationRelativeTo(frame);
            dialog.setVisible(true);
        }
        @Override
        public void update(Observable o, Object arg) {

        }
    }

    private void showExceptionDialog(Throwable exc) {
        StringBuilder stackTrace = new StringBuilder();
        for (StackTraceElement element : exc.getStackTrace()) {
            stackTrace.append(element).append(System.lineSeparator());
        }
        JTextArea textArea = new JTextArea(exc.getMessage() + System.lineSeparator() + stackTrace);
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(null,
                textArea,
                "Exception!", JOptionPane.ERROR_MESSAGE);
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
