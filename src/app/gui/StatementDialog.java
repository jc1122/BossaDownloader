package app.gui;

import app.API.BossaAPI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;

class StatementDialog implements PropertyChangeListener {
    private View view;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        accountList = (List<BossaAPI.NolStatementAPI>) evt.getNewValue();
        updateStatementPanel(accountNameComboBox.getSelectedIndex());
    }

    private List<BossaAPI.NolStatementAPI> accountList;
    private JDialog dialog;
    private JComboBox<String> accountNameComboBox;
    private JPanel accountComboBoxPanel;
    private JPanel statementPanel;
    private JLabel ikeLabel;
    private JLabel ikeStatusLabel;
    private JLabel accountTypeLabel;
    private JLabel accountTypeStatusLabel;
    private JLabel accountLabel;
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

    private void initDialogElements() {
        //logger.entering(StatementDialog.class.getName(), "initDialogElements");
        ikeLabel = new JLabel("Indywidualne Konto Emerytalne :");
        ikeStatusLabel = new JLabel(); //text will be set later
        accountTypeLabel = new JLabel("Account type :");
        accountTypeStatusLabel = new JLabel(); //text will be set later

        dialog = new JDialog();
        dialog.setTitle("Statement");
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

        accountComboBoxPanel = new JPanel();
        accountComboBoxPanel.setBackground(new Color(100, 100, 100));
        accountComboBoxPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        dialog.add(accountComboBoxPanel);
        statementPanel = new JPanel();
        statementPanelLayout = new GridLayout(0, 2);
        statementPanelLayout.setHgap(20);
        statementPanel.setLayout(statementPanelLayout);
        statementPanel.setBackground(new Color(100, 200, 200));

        dialog.add(statementPanel);
        accountNameComboBox = new JComboBox<>();
        accountNameComboBox.addActionListener(accountNameComboBoxActionListener);
        accountLabel = new JLabel("Account: ");
        accountComboBoxPanel.add(accountLabel);
        accountComboBoxPanel.add(accountNameComboBox);

        dialog.setSize(new Dimension(300, 150));
        //dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void updateStatementPanel(int index) {
        statementPanel.removeAll();
        statementPanel.add(ikeLabel);
        statementPanel.add(ikeStatusLabel);

        statementPanel.add(accountTypeLabel);
        statementPanel.add(accountTypeStatusLabel);
        accountNameComboBox.removeActionListener(accountNameComboBoxActionListener);
        accountNameComboBox.removeAllItems();

        accountList = view.model.getStatements(); //TODO will cause error when api is in investor offline status

        for (BossaAPI.NolStatementAPI account : accountList) {
            accountNameComboBox.addItem(account.getName());
        }
        accountNameComboBox.setSelectedIndex(index);
        accountNameComboBox.addActionListener(accountNameComboBoxActionListener);
        BossaAPI.NolStatementAPI defaultAccount = accountList.get(index);
        ikeStatusLabel.setText(defaultAccount.getIke() ? "True" : "False");
        accountTypeStatusLabel.setText(defaultAccount.getType().equals("M") ? "Cash" : "Futures");

        for (Map.Entry<String, Double> fund : defaultAccount.getFundMap().entrySet()) {
            statementPanel.add(new JLabel(fund.getKey()));
            statementPanel.add(new JLabel(fund.getValue().toString()));
        }
    }

    StatementDialog(View view) {
        this.view = view;
        try {
            view.model.addAccountsListener(this);
        } catch (NullPointerException e) {
            throw new NullPointerException("Unable to get accounts! Is API initialized?");
        }
        initDialogElements();

        updateStatementPanel(0);
        dialog.setMinimumSize(new Dimension(accountNameComboBox.getWidth() + 100, accountNameComboBox.getHeight() + 100));

        accountComboBoxPanel.setMaximumSize(
                new Dimension(3 * (accountNameComboBox.getWidth() + accountLabel.getWidth()), 4 * accountNameComboBox.getHeight()));
        statementPanelLayout.setHgap(accountNameComboBox.getWidth() / 2);

    }
}
