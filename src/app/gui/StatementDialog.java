package app.gui;

import app.API.BossaAPI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO this is ugly, refactor
class StatementDialog implements PropertyChangeListener {

    private View view;
    private List<BossaAPI.NolStatementAPI> accountList;
    private JDialog dialog;
    private JComboBox<String> accountNameComboBox;

    private JPanel accountPanel;
    private JPanel statementPanel;
    private JPanel positionsPanel; //TODO add positions to interface

    private JLabel ikeLabel;
    private JLabel ikeStatusLabel;
    private JLabel accountTypeLabel;
    private JLabel accountTypeStatusLabel;
    private JLabel accountLabel;
    private Map<String, JLabel> statementLabels;
    private Map<String, JLabel> statementValues;

    private GridLayout statementPanelLayout;
    private GridLayout positionsPanelLayout;

    private Map<String, JLabel> positionIsins = new HashMap<>();
    private ActionListener accountNameComboBoxActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("event source");
            JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
            int index = comboBox.getSelectedIndex();

            updateStatementPanel(index);
            populatePositionsPanel(index);
        }
    };


    StatementDialog(View view) {
        this.view = view;
        try {
            this.view.getModel().addAccountsListener(this);
            this.view.getModel().addQuotesListener(this);
        } catch (NullPointerException e) {
            throw new NullPointerException("Unable to get accounts! Is API initialized?");
        }
        initDialogElements();
        accountList = view.getModel().getStatements(); //TODO will cause error when api is in investor offline status
        updateStatementPanel(0);
        populatePositionsPanel(0);
        resizePanels();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "statements":
                accountList = (List<BossaAPI.NolStatementAPI>) evt.getNewValue();
                updateStatementPanel(accountNameComboBox.getSelectedIndex());
                break;
            case "nolRecentInfoAPI":
                System.out.println("changing price!");
                BossaAPI.NolRecentInfoAPI quote = (BossaAPI.NolRecentInfoAPI) evt.getNewValue();
                synchronized (positionIsins) {
                    String isin = quote.getTicker().getIsin();
                    if (positionIsins.containsKey(isin)) {
                        positionIsins.get(isin).setText(Double.toString(quote.getReferPrice()));
                    }
                    positionIsins.notifyAll();
                }
                break;
        }

    }

    private void resizePanels() {
        dialog.setMinimumSize(new Dimension(accountNameComboBox.getWidth() + 100, accountNameComboBox.getHeight() + 100));
        dialog.setMinimumSize(new Dimension(new Double(statementPanel.getPreferredSize().getWidth() * 1.5).intValue(),
                new Double(statementPanel.getPreferredSize().getHeight() * 2).intValue()));
        accountPanel.setMaximumSize(
                new Dimension(3 * (accountNameComboBox.getWidth() + accountLabel.getWidth()), 4 * accountNameComboBox.getHeight()));
        statementPanelLayout.setHgap(accountNameComboBox.getWidth() / 2);
    }

    private void createAccountSelectionPane() {
        accountPanel = new JPanel();
        accountPanel.setBackground(new Color(100, 100, 100));
        accountPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        accountNameComboBox = new JComboBox<>();
        accountNameComboBox.addActionListener(accountNameComboBoxActionListener);
        accountLabel = new JLabel("Account: ");
        accountPanel.add(accountLabel);
        accountPanel.add(accountNameComboBox);
    }

    private void createStatementPane() {
        ikeLabel = new JLabel("Indywidualne Konto Emerytalne :");
        ikeStatusLabel = new JLabel(); //text will be set later
        accountTypeLabel = new JLabel("Account type :");
        accountTypeStatusLabel = new JLabel(); //text will be set later
        statementPanel = new JPanel();
        statementPanelLayout = new GridLayout(0, 2);
        statementPanelLayout.setHgap(20);
        statementPanel.setLayout(statementPanelLayout);
        statementPanel.setBackground(new Color(100, 200, 200));

        statementPanel.add(ikeLabel);
        statementPanel.add(ikeStatusLabel);

        statementPanel.add(accountTypeLabel);
        statementPanel.add(accountTypeStatusLabel);

        statementLabels = new HashMap<>();
        statementLabels.put("Deposit", new JLabel("Deposit:"));
        statementLabels.put("CashBlocked", new JLabel("Cash blocked:"));
        statementLabels.put("BlockedDeposit", new JLabel("Blocked deposit:"));
        statementLabels.put("FreeDeposit", new JLabel("Free deposit:"));
        statementLabels.put("SecSafetiesUsed", new JLabel("SecSafetiesUsed change to meaningful label:"));
        statementLabels.put("PortfolioValue", new JLabel("Portfolio value: "));
        statementLabels.put("SecValueSum", new JLabel("SevValSum:"));
        statementLabels.put("SecSafeties", new JLabel("SecSafeties:"));
        statementLabels.put("OptionBonus", new JLabel("OptionBonus:"));
        statementLabels.put("MaxBuy", new JLabel("MaxBuy:"));
        statementLabels.put("LiabilitiesLimitMax", new JLabel("LiabilitiesLimitMax:"));
        statementLabels.put("Recivables", new JLabel("Recivables:"));
        statementLabels.put("Liabilities", new JLabel("Liabilities:"));
        statementLabels.put("MaxOtpBuy", new JLabel("MaxOtpBuy:"));
        statementLabels.put("RecivablesBlocked", new JLabel("RecivablesBlocked:"));
        statementLabels.put("CashRecivables", new JLabel("CashRecivables:"));
        statementLabels.put("Cash", new JLabel("Cash:"));

        statementValues = new HashMap<>();
        for (String key : statementLabels.keySet()) {
            statementValues.put(key, new JLabel());

            statementPanel.add(statementLabels.get(key));
            statementPanel.add(statementValues.get(key));
        }
    }

    private void createPositionsPane() {
        positionsPanel = new JPanel();
        positionsPanel.setBackground(new Color(133, 133, 233));
        positionsPanelLayout = new GridLayout(0, 4);
        positionsPanel.setLayout(positionsPanelLayout);
    }

    private void createDialog() {
        dialog = new JDialog();
        dialog.setTitle("Statement");
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

        dialog.add(accountPanel);
        dialog.add(statementPanel);
        dialog.add(positionsPanel);

        dialog.pack();
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void initDialogElements() {
        //logger.entering(StatementDialog.class.getName(), "initDialogElements");
        createAccountSelectionPane();
        createStatementPane();
        createPositionsPane();
        createDialog();
    }

    private void refreshAccountNameComboBox(int index) {
        accountNameComboBox.removeActionListener(accountNameComboBoxActionListener);
        accountNameComboBox.removeAllItems();

        for (BossaAPI.NolStatementAPI account : accountList) {
            accountNameComboBox.addItem(account.getName());
        }
        accountNameComboBox.setSelectedIndex(index);
        accountNameComboBox.addActionListener(accountNameComboBoxActionListener);
    }

    //TODO add all known positions from map, add position list
    private void populateStatementPanel(int index) {

        BossaAPI.NolStatementAPI defaultAccount = accountList.get(Math.max(index, 0)); //Max is just in case, when no element = -1  selected
        ikeStatusLabel.setText(defaultAccount.getIke() ? "True" : "False");
        accountTypeStatusLabel.setText(defaultAccount.getType().equals("M") ? "Cash" : "Futures");

        for (JLabel value : statementLabels.values()) {
            value.setEnabled(false); //grey out labels
        }
        for (JLabel value : statementValues.values()) {
            value.setText(""); //remove text
        }

        for (Map.Entry<String, Double> fund : defaultAccount.getFundMap().entrySet()) {
            statementLabels.get(fund.getKey()).setEnabled(true);
            statementValues.get(fund.getKey()).setText(fund.getValue().toString());
        }
    }

    private void updateStatementPanel(int index) {
        refreshAccountNameComboBox(index);
        populateStatementPanel(index);
    }

    private void populatePositionsPanel(int index) {
        BossaAPI.NolStatementAPI currentAccount = accountList.get(index);
        List<BossaAPI.NolPosAPI> positions = currentAccount.getPositions();

        positionsPanel.removeAll();
        positionsPanel.add(new JLabel("Ticker"));
        positionsPanel.add(new JLabel("Securities count"));
        positionsPanel.add(new JLabel("Blocked for sale"));
        positionsPanel.add(new JLabel("Value"));

        positionIsins = new HashMap<>();
        String isins = "";
        for (BossaAPI.NolPosAPI position : positions) {
            String isin = position.getTicker().getIsin();
            positionIsins.put(isin, new JLabel());
            isins = isins + isin + ";";
        }
        System.out.println("isins: " + isins);
        //TODO this is temporary only, delegate it to model; code is ugly and will reset quote filter!
        if (!isins.equals("")) {
            for (BossaAPI.NolPosAPI position : positions) {
                String isin = position.getTicker().getIsin();
                positionsPanel.add(new JLabel(position.getTicker().getName()));
                positionsPanel.add(new JLabel(Integer.toString(position.getAcc110())));
                positionsPanel.add(new JLabel(Integer.toString(position.getAcc120())));
                positionsPanel.add(positionIsins.get(isin));
                BossaAPI.AddToFilter(isins, false);
            }
        }
    }
}

