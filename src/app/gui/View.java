package app.gui;

import app.API.BossaAPI;
import app.API.BossaAPI.NolStatementAPI;
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
        private JComboBox<String> accountNameComboBox;
        private JPanel comboBoxPanel;
        StatementDialog() {
            model.getAccountsObservable().addObserver(this);
            accountNameComboBox = new JComboBox<>();
            java.util.List<NolStatementAPI> accountList = model.getAccountsObservable().getStatements();

            for (NolStatementAPI account : accountList) {
                accountNameComboBox.addItem(account.getName());
            }
            String[] accounts = {"asdf", "basdfafdasdfasdfasdfasbasdfafdasdfasdfasdfasdd"};
            dialog = new JDialog(frame, "Statement");

            Container pane = dialog.getContentPane();
            pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

            comboBoxPanel = new JPanel();
            comboBoxPanel.setBackground(new Color(100, 100, 100));
            comboBoxPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            dialog.getContentPane().add(comboBoxPanel);


            comboBoxPanel.add(accountNameComboBox);

            JPanel statementPanel = new JPanel();
            GridLayout gridLayout = new GridLayout(0, 2);
            gridLayout.setHgap(20);
            statementPanel.setLayout(gridLayout);
            statementPanel.setBackground(new Color(200, 200, 200));

            JLabel label1 = new JLabel("asdfasdfasdfa");

            label1.setHorizontalAlignment(SwingConstants.RIGHT);
            JLabel label2 = new JLabel("asdfasdfa");

            label2.setHorizontalAlignment(SwingConstants.RIGHT);
            JLabel label3 = new JLabel("asdfafa");

            JLabel label4 = new JLabel("asdfasdfasdfa");

            statementPanel.add(label1);
            statementPanel.add(label2);
            statementPanel.add(label3);
            statementPanel.add(label4);
            dialog.getContentPane().add(statementPanel);

            //dialog.setSize(new Dimension(300, 150));
            dialog.setLocationRelativeTo(frame);
            dialog.setVisible(true);

            dialog.setMinimumSize(new Dimension(accountNameComboBox.getWidth() + 100, accountNameComboBox.getHeight() + 100));
            comboBoxPanel.setMaximumSize(new Dimension(accountNameComboBox.getWidth() + 100, accountNameComboBox.getHeight() + 100));
            gridLayout.setHgap(accountNameComboBox.getWidth() / 2);

        }
        @Override
        public void update(Observable o, Object arg) {
            BossaAPI.AccountsObservable accountsObservable = (BossaAPI.AccountsObservable) o;

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
