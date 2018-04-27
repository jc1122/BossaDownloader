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
    private ActionListener accountNameComboBoxActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("event source");
            JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
            int index = comboBox.getSelectedIndex();

            updateStatementPanel(index);
        }
    };


    StatementDialog(View view) {
        this.view = view;
        try {
            this.view.getModel().addAccountsListener(this);
        } catch (NullPointerException e) {
            throw new NullPointerException("Unable to get accounts! Is API initialized?");
        }
        initDialogElements();
        accountList = view.getModel().getStatements(); //TODO will cause error when api is in investor offline status
        updateStatementPanel(0);
        resizePanels();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        accountList = (List<BossaAPI.NolStatementAPI>) evt.getNewValue();
        updateStatementPanel(accountNameComboBox.getSelectedIndex());
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

        statementLabels = new HashMap();
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

    private void createDialog() {
        dialog = new JDialog();
        dialog.setTitle("Statement");
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

        dialog.add(accountPanel);
        dialog.add(statementPanel);

        dialog.pack();
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void initDialogElements() {
        //logger.entering(StatementDialog.class.getName(), "initDialogElements");
        createAccountSelectionPane();
        createStatementPane();
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
}

