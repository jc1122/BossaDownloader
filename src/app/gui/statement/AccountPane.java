package app.gui.statement;

import app.API.BossaAPI;
import app.gui.Model;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.logging.Logger;

class AccountPane implements PropertyChangeListener {
    private static final Logger logger =
            Logger.getLogger(AccountPane.class.getName());

    private final JPanel accountPanel;
    private final JComboBox<String> accountNameComboBox;

    private List<BossaAPI.NolStatementAPI> accountList;

    AccountPane(Model model) {
        logger.entering(this.getClass().getName(), "constructor", model);

        accountPanel = new JPanel();
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.LINE_AXIS));

        accountNameComboBox = new JComboBox<>();

        accountPanel.add(new JLabel("Account: "));
        accountPanel.add(accountNameComboBox);

        try {
            model.addPropertyListener(this);
        } catch (NullPointerException e) {
            NullPointerException exc = new NullPointerException("Unable to get accounts! Is API initialized?");
            logger.finer(exc.getMessage());
            throw exc;
        }

        this.accountList = (List<BossaAPI.NolStatementAPI>) model.getProperty("Accounts"); //TODO will cause error when api is in investor offline status

        addAccountsToComboBox(accountList);

        accountNameComboBox.setSelectedIndex(0);
        accountNameComboBox.setMaximumSize(accountNameComboBox.getPreferredSize());
        logger.exiting(this.getClass().getName(), "constructor");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent evt) {
        logger.entering(this.getClass().getName(), "propertyChange", evt);

        if (!evt.getPropertyName().equals("Accounts")) {
            logger.exiting(this.getClass().getName(), "propertyChange", evt);
            return;
        }

        int index = accountNameComboBox.getSelectedIndex();

        this.accountList = (List<BossaAPI.NolStatementAPI>) evt.getNewValue();
        synchronized (this) {
            logger.finest("entering synchronized block");
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
            accountNameComboBox.setMaximumSize(accountNameComboBox.getPreferredSize());
            accountNameComboBox.setSelectedIndex(index);
        }
        logger.exiting(this.getClass().getName(), "propertyChange", evt);
    }

    void addAccountSelectionListener(ActionListener listener) {
        logger.entering(this.getClass().getName(), "addAccountSelectionListener", listener);
        accountNameComboBox.addActionListener(listener);
        logger.exiting(this.getClass().getName(), "addAccountSelectionListener");
    }

    private void addAccountsToComboBox(List<BossaAPI.NolStatementAPI> accounts) {
        logger.entering(this.getClass().getName(), "addAccountSelectionListener", accounts);
        for (BossaAPI.NolStatementAPI account : accounts) {
            accountNameComboBox.addItem(account.getName());
        }
        logger.exiting(this.getClass().getName(), "addAccountSelectionListener", accounts);
    }

    JPanel getPane() {
        return accountPanel;
    }

}
