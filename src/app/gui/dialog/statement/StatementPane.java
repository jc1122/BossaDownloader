package app.gui.dialog.statement;

import app.API.BossaAPI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

class StatementPane<K extends StatementModel> implements PropertyChangeListener, ActionListener {
    private static final Logger logger =
            Logger.getLogger(StatementPane.class.getName());
    private JPanel statementPanel;
    private JLabel ikeStatusLabel;
    private JLabel accountTypeStatusLabel;
    private Map<String, JLabel> statementLabels;
    private Map<String, JLabel> statementValues;
    private List<BossaAPI.NolStatementAPI> accountList;

    private int selectedAccount;

    StatementPane(K model) {
        logger.entering(this.getClass().getName(), "constructor", model);

        try {
            model.addPropertyChangeListener(this);
        } catch (NullPointerException e) {
            throw new NullPointerException("Unable to get accounts! Is API initialized?");
        }

        //noinspection unchecked
        this.accountList = model.getAccounts();

        ikeStatusLabel = new JLabel(); //text will be set later
        accountTypeStatusLabel = new JLabel(); //text will be set later

        statementPanel = new JPanel();
        //private JLabel accountLabel;
        GridLayout statementPanelLayout = new GridLayout(0, 2);
        statementPanelLayout.setHgap(20);
        statementPanel.setLayout(statementPanelLayout);
        //statementPanel.setBackground(new Color(100, 200, 200));

        statementPanel.add(new JLabel("Indywidualne Konto Emerytalne :"));
        statementPanel.add(ikeStatusLabel);

        statementPanel.add(new JLabel("Account type :"));
        statementPanel.add(accountTypeStatusLabel);

        statementLabels = new LinkedHashMap<>();
        statementLabels.put("CashAccount", new JLabel(""));

        statementLabels.put("CashRecivables", new JLabel("Total cash:"));
        statementLabels.put("MaxBuy", new JLabel("Max buying power:"));
        statementLabels.put("MaxOtpBuy", new JLabel("Max OTP buy:"));
        statementLabels.put("LiabilitiesLimitMax", new JLabel("Amount to reach buying limit:"));
        statementLabels.put("RecivablesBlocked", new JLabel("Receivables blocked for orders:"));
        statementLabels.put("Recivables", new JLabel("Recivables:"));
        statementLabels.put("Liabilities", new JLabel("Liabilities:"));

        statementLabels.put("FuturesAccount", new JLabel(""));

        statementLabels.put("Deposit", new JLabel("Deposit:"));
        statementLabels.put("BlockedDeposit", new JLabel("Blocked deposit:"));
        statementLabels.put("FreeDeposit", new JLabel("Free deposit:"));
        statementLabels.put("SecSafetiesUsed", new JLabel("SecSafetiesUsed change to meaningful label:"));
        statementLabels.put("SecSafeties", new JLabel("SecSafeties:"));
        statementLabels.put("OptionBonus", new JLabel("OptionBonus:"));

        statementLabels.put("AllAccounts", new JLabel(""));

        statementLabels.put("Cash", new JLabel("Cash available for orders:"));
        statementLabels.put("CashBlocked", new JLabel("Cash blocked for pending orders:"));
        statementLabels.put("SecValueSum", new JLabel("Securities value::"));
        statementLabels.put("PortfolioValue", new JLabel("Total portfolio value: "));

        statementLabels.put("Separator", new JLabel(""));

        statementValues = new HashMap<>();
        for (String key : statementLabels.keySet()) {
            statementValues.put(key, new JLabel());

            statementPanel.add(statementLabels.get(key));
            statementPanel.add(statementValues.get(key));
        }
        //TODO this may be buggy
        updateStatementPanel(0);
        logger.exiting(this.getClass().getName(), "constructor");
    }

    private void updateStatementPanel(int index) {
        logger.entering(this.getClass().getName(), "updateStatementPanel", index);
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
        int preferredHeight = statementPanel.getPreferredSize().height;
        statementPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, preferredHeight));
        logger.exiting(this.getClass().getName(), "updateStatementPanel");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        logger.entering(this.getClass().getName(), "propertyChange", evt);
        if (!evt.getPropertyName().equals("Accounts"))
            return;
        //noinspection unchecked
        this.accountList = (List<BossaAPI.NolStatementAPI>) evt.getNewValue();
        updateStatementPanel(this.selectedAccount);
        logger.exiting(this.getClass().getName(), "propertyChange");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.entering(this.getClass().getName(), "actionPerformed", e);
        //noinspection unchecked
        JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
        this.selectedAccount = comboBox.getSelectedIndex();
        updateStatementPanel(this.selectedAccount);
        logger.exiting(this.getClass().getName(), "actionPerformed");
    }

    public JPanel getPane() {
        return statementPanel;
    }
}
