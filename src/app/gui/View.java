package app.gui;

import app.gui.dialog.generifiedSaveToCSV.SaveToCSVController;
import app.gui.dialog.generifiedSaveToCSV.SaveToCSVDialog;
import app.gui.dialog.generifiedSaveToCSV.SaveToCSVModel;
import app.gui.dialog.generifiedSaveToCSV.SaveToCSVView;
import app.gui.menu.MainMenuBarFactory;
import app.gui.statement.StatementDialog;
import app.gui.tickerSelector.SelectTickersDialog;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

class View {
    private static final Logger logger =
            Logger.getLogger(View.class.getName());

    private final Controller controller;
    private final Model model;

    private JFrame frame;
    private MainMenuBarFactory mainMenuBarFactory;
    private SelectTickersDialog selectTickersDialog;
    private SaveToCSVDialog<SaveToCSVModel, SaveToCSVView,
            SaveToCSVController<SaveToCSVModel, SaveToCSVView>> saveToCSVDialog;

    private JLabel bottomInfoLabel;
    View(Controller controller, Model model) {
        this.controller = controller;
        this.model = model;
    }

    void setBottomInfoText(String text) {
        bottomInfoLabel.setText(text);
    }

    private void createFrame() {
        logger.entering(this.getClass().getName(), "createFrame");
        frame = new JFrame("BossaDownloader");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.getContentPane().setLayout(new BorderLayout());

        JPanel bottomInfoPanel = new JPanel();
        bottomInfoLabel = new JLabel("Ready");
        bottomInfoPanel.add(bottomInfoLabel);
        bottomInfoPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        bottomInfoPanel.setLayout(new BoxLayout(bottomInfoPanel, BoxLayout.LINE_AXIS));

        frame.getContentPane().add(bottomInfoPanel, BorderLayout.PAGE_END);

        frame.setSize(600, 300);
        frame.setVisible(true);
        logger.exiting(this.getClass().getName(), "createFrame");

    }

    void createGUI() {
        logger.entering(this.getClass().getName(), "createGUI");
        createFrame();

        mainMenuBarFactory = new MainMenuBarFactory("MenuBar");
        frame.setJMenuBar(mainMenuBarFactory.getContainer());
        System.out.println("menu elements: " + mainMenuBarFactory.getNameMapping().keySet());
        addEventListeners();
        logger.exiting(this.getClass().getName(), "createGUI");
    }

    private void addEventListeners() {
        logger.entering(this.getClass().getName(), "addEventListeners");
        JMenuItem start = (JMenuItem)mainMenuBarFactory.getComponent("Start");// API/start
        start.addActionListener(new ActionListenerShowsDialogOnException((e) -> controller.startAPI()));

        JMenuItem stop = (JMenuItem)mainMenuBarFactory.getComponent("Stop"); // API/stop
        stop.addActionListener(new ActionListenerShowsDialogOnException((e) -> controller.stopAPI()));

        JMenuItem statement = (JMenuItem)mainMenuBarFactory.getComponent("Statement"); // Account/statement
        statement.addActionListener(new ActionListenerShowsDialogOnException((e) -> controller.showStatement()));

        JMenuItem select = (JMenuItem)mainMenuBarFactory.getComponent("Select..."); // Tickers/Select...
        select.addActionListener(new ActionListenerShowsDialogOnException((e) -> controller.selectTickers()));

        JMenuItem saveToCSV = (JMenuItem) mainMenuBarFactory.getComponent("Save to CSV"); //Tickers/Save to CSV
        saveToCSV.addActionListener(new ActionListenerShowsDialogOnException((e) -> controller.saveToCSV()));

        JMenuItem version = (JMenuItem)mainMenuBarFactory.getComponent("Version");// Help/version
        version.addActionListener(new ActionListenerShowsDialogOnException((e) -> controller.showVersion()));
        logger.exiting(this.getClass().getName(), "addEventListeners");
    }

    void showVersionDialog() {
        logger.entering(this.getClass().getName(), "showVersionDialog");
        JOptionPane.showMessageDialog(frame, "API version: \n" + model.getVersion());
        logger.exiting(this.getClass().getName(), "showVersionDialog");
    }

    void showStatementDialog() {
        logger.entering(this.getClass().getName(), "showStatementDialog");
        new StatementDialog(model);
        logger.exiting(this.getClass().getName(), "showStatementDialog");
    }

    //only one dialog possible at a time
    void showSelectTickersDialog() {
        logger.entering(this.getClass().getName(), "showSelectTickersDialog");
        if (selectTickersDialog != null) {
            if (selectTickersDialog.getDialog().isVisible()) {
                logger.exiting(this.getClass().getName(), "showSelectTickersDialog", "already visible");
                return;
            }
        }
        selectTickersDialog = new SelectTickersDialog(model);
        selectTickersDialog.getDialog().setVisible(true);
        selectTickersDialog.getDialog().requestFocus();
        logger.exiting(this.getClass().getName(), "showSelectTickersDialog", "created a new dialog");
    }

    void setComponentEnabled(String name, boolean enabled) {
        Object[] params = {name, enabled};
        logger.entering(this.getClass().getName(), "setComponentEnabled", params);
        mainMenuBarFactory.getComponent(name).setEnabled(enabled);
        logger.exiting(this.getClass().getName(), "setComponentEnabled");
    }

    public Model getModel() {
        return model;
    }

    //TODO refactor this and showSelectTickersDialog, abstract common class
    void showSaveToCSVDialog() {
        logger.entering(this.getClass().getName(), "showSelectTickersDialog");
        if (saveToCSVDialog != null) {
            if (saveToCSVDialog.getDialog().isVisible()) {
                logger.exiting(this.getClass().getName(), "showSelectTickersDialog", "already visible");
                return;
            }
        }
        saveToCSVDialog = new SaveToCSVDialog(model, SaveToCSVModel.class, SaveToCSVView.class, SaveToCSVController.class);
        saveToCSVDialog.getDialog().setVisible(true);
        saveToCSVDialog.getDialog().requestFocus();
        logger.exiting(this.getClass().getName(), "showSelectTickersDialog", "created a new dialog");
    }

}
