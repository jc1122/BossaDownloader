package app.gui;

import app.API.BossaAPI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

class AccountPane implements PropertyChangeListener {
    private Model model;

    private JPanel accountPanel;
    private JComboBox<String> accountNameComboBox;
    private JLabel accountLabel;

    private List<BossaAPI.NolStatementAPI> accountList;

    AccountPane(Model model) {
        this.model = model;

        accountPanel = new JPanel();
        accountPanel.setBackground(new Color(100, 100, 100));
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.LINE_AXIS));

        accountNameComboBox = new JComboBox<>();
        accountLabel = new JLabel("Account: ");
        accountPanel.add(accountLabel);
        accountPanel.add(accountNameComboBox);

        try {
            this.model.addAccountsListener(this);
        } catch (NullPointerException e) {
            throw new NullPointerException("Unable to get accounts! Is API initialized?");
        }

        this.accountList = model.getStatements(); //TODO will cause error when api is in investor offline status

        addAccountsToComboBox(accountList);

        accountNameComboBox.setSelectedIndex(0);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        int index = accountNameComboBox.getSelectedIndex();
        this.accountList = (List<BossaAPI.NolStatementAPI>) evt.getNewValue();
        synchronized (this) {
            //need to remove listeners before calling removeAllItems, or listeners will be notified of that
            ActionListener[] listeners = accountNameComboBox.getActionListeners();
            for (ActionListener listener : listeners) {
                accountNameComboBox.removeActionListener(listener);
            }

            accountNameComboBox.removeAllItems();
            addAccountsToComboBox(accountList);

            for (ActionListener listener : listeners) {
                accountNameComboBox.addActionListener(listener);
            }

            accountNameComboBox.setSelectedIndex(index);
        }
    }

    public void addAccountSelectionListener(ActionListener listener) {
        accountNameComboBox.addActionListener(listener);
    }

    private void addAccountsToComboBox(List<BossaAPI.NolStatementAPI> accounts) {
        for (BossaAPI.NolStatementAPI account : accounts) {
            accountNameComboBox.addItem(account.getName());
        }
    }
}
