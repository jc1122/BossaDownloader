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

public class StatementPane implements PropertyChangeListener, ActionListener {
    private JPanel statementPanel;
    private JLabel ikeLabel;
    private JLabel ikeStatusLabel;
    private JLabel accountTypeLabel;
    private JLabel accountTypeStatusLabel;
    private JLabel accountLabel;
    private GridLayout statementPanelLayout;
    private Map<String, JLabel> statementLabels;
    private Map<String, JLabel> statementValues;
    private List<BossaAPI.NolStatementAPI> accountList;

    private Model model;
    private int selectedAccount;

    StatementPane(Model model) {
        this.model = model;

        try {
            this.model.addAccountsListener(this);
        } catch (NullPointerException e) {
            throw new NullPointerException("Unable to get accounts! Is API initialized?");
        }

        this.accountList = model.getStatements();

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
        //TODO this may be buggy
        updateStatementPanel(0);
    }

    private void updateStatementPanel(int index) {
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.accountList = (List<BossaAPI.NolStatementAPI>) evt.getNewValue();
        updateStatementPanel(this.selectedAccount);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
        this.selectedAccount = comboBox.getSelectedIndex();
        updateStatementPanel(this.selectedAccount);
    }

    public JPanel getPane() {
        return statementPanel;
    }
}
