package app.gui.dialog.statement;

import app.API.JNAinterface.NolPosAPI;
import app.API.JNAinterface.NolRecentInfoAPI;
import app.API.JNAinterface.NolStatementAPI;
import app.API.JNAinterface.NolTickerAPI;
import app.gui.Controller;
import app.gui.dialog.GUIView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StatementView<K extends StatementModel,
        L extends StatementView<K, L, M>,
        M extends StatementController<K, L>>
        extends GUIView<K, L, M> {

    protected PositionsPane positionsPane;
    protected AccountPane accountPane;
    protected StatementPane statementPane;


    StatementView(M controller, K model) {
        super(controller, model);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //TODO move all logic notifying the change of data in Pane classes here
    }

    @Override
    public void createGUI() {
        dialog.setTitle("Statement");
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        accountPane = new AccountPane(model);
        statementPane = new StatementPane(model);
        positionsPane = new PositionsPane(model, this);

        accountPane.addAccountSelectionListener(statementPane);
        accountPane.addAccountSelectionListener(positionsPane);

        dialog.add(accountPane.getPane());
        dialog.add(statementPane.getPane());
        dialog.add(positionsPane.getPane());

        dialog.pack();
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    static class AccountPane<K extends StatementModel> implements PropertyChangeListener {
        private static final Logger logger =
                Logger.getLogger(AccountPane.class.getName());

        private final JPanel accountPanel;
        private final JComboBox<String> accountNameComboBox;

        private List<NolStatementAPI> accountList;

        AccountPane(K model) {
            logger.entering(this.getClass().getName(), "constructor", model);

            accountPanel = new JPanel();
            accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.LINE_AXIS));

            accountNameComboBox = new JComboBox<>();

            accountPanel.add(new JLabel("Account: "));
            accountPanel.add(accountNameComboBox);

            try {
                model.addPropertyChangeListener(this);
            } catch (NullPointerException e) {
                NullPointerException exc = new NullPointerException("Unable to get accounts! Is API initialized?");
                logger.finer(exc.getMessage());
                throw exc;
            }

            this.accountList = model.getAccounts(); //TODO will cause error when api is in investor offline status

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

            this.accountList = (List<NolStatementAPI>) evt.getNewValue();
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

        private void addAccountsToComboBox(List<NolStatementAPI> accounts) {
            logger.entering(this.getClass().getName(), "addAccountSelectionListener", accounts);
            for (NolStatementAPI account : accounts) {
                accountNameComboBox.addItem(account.getName());
            }
            logger.exiting(this.getClass().getName(), "addAccountSelectionListener", accounts);
        }

        JPanel getPane() {
            return accountPanel;
        }

    }

    static class StatementPane<K extends StatementModel> implements PropertyChangeListener, ActionListener {
        private static final Logger logger =
                Logger.getLogger(StatementPane.class.getName());
        private JPanel statementPanel;
        private JLabel ikeStatusLabel;
        private JLabel accountTypeStatusLabel;
        private Map<String, JLabel> statementLabels;
        private Map<String, JLabel> statementValues;
        private List<NolStatementAPI> accountList;

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
            NolStatementAPI defaultAccount = accountList.get(Math.max(index, 0)); //Max is just in case, when no element = -1  selected
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
            this.accountList = (List<NolStatementAPI>) evt.getNewValue();
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

    //TODO check behavior in concurrency; remember to remove TickerSelector from filter on close!
    static class PositionsPane<K extends StatementModel> implements PropertyChangeListener, ActionListener {
        private static final Logger logger =
                Logger.getLogger(Controller.class.getName());
        private JPanel positionsPanel;
        private final K model;
        private int selectedAccount;
        private List<NolStatementAPI> accountList;
        private Map<String, Double> positionIsinsPrices;
        private Map<String, JLabel> positionIsinsLabels;
        private Map<String, Integer> positionIsinsCount;
        private Set<String> isinsInModelFilter;

        private final JDialog dialog;

        PositionsPane(K model, StatementView view) {
            Object[] params = {model, view};
            logger.entering(this.getClass().getName(), "constructor", params);

            this.dialog = view.getDialog();
            this.model = model;
            try {
                this.model.addPropertyChangeListener(this);
            } catch (NullPointerException e) {
                NullPointerException exc = new NullPointerException("Unable to get accounts! Is API initialized?");
                logger.finer(exc.getMessage());
                throw exc;
            }

            positionsPanel = new JPanel();
            //positionsPanel.setBackground(new Color(133, 133, 233));
            positionsPanel.setLayout(new GridLayout(0, 4));

            positionIsinsPrices = new HashMap<>();
            positionIsinsLabels = new HashMap<>();
            positionIsinsCount = new HashMap<>();

            this.accountList = model.getAccounts(); //TODO will cause error when api is in investor offline status
            isinsInModelFilter = model.getTickersInFilter().stream().map(NolTickerAPI::getIsin).collect(Collectors.toSet());
            //TODO this may be buggy
            updatePanel(0);
            logger.exiting(this.getClass().getName(), "constructor");
        }

        private synchronized void updatePanel(int index) {
            logger.entering(this.getClass().getName(), "updatePanel", index);
            NolStatementAPI currentAccount = accountList.get(index);

            positionsPanel.removeAll();
            positionsPanel.add(new JLabel("Ticker"));
            positionsPanel.add(new JLabel("Securities count"));
            positionsPanel.add(new JLabel("Blocked for sale"));
            positionsPanel.add(new JLabel("Value"));

            positionIsinsPrices.clear();
            positionIsinsLabels.clear();
            positionIsinsCount.clear();

            //TODO refactor to a set of isins
            List<NolPosAPI> positions = currentAccount.getPositions();
            if (!positions.isEmpty()) {
                for (NolPosAPI position : positions) {
                    String isin = position.getTicker().getIsin();
                    positionIsinsPrices.put(isin, -1.);
                    positionIsinsLabels.put(isin, new JLabel());
                    positionIsinsCount.put(isin, -10);
                }

                for (NolPosAPI position : positions) {
                    String isin = position.getTicker().getIsin();
                    positionsPanel.add(new JLabel(position.getTicker().getName()));
                    positionsPanel.add(new JLabel(Integer.toString(position.getAcc110())));
                    positionsPanel.add(new JLabel(Integer.toString(position.getAcc120())));
                    positionsPanel.add(positionIsinsLabels.get(isin));
                    positionIsinsCount.replace(isin, position.getAcc110() + position.getAcc120());
                }

                //add to filter forces callback, it must be called last, otherwise there may be race condition with code above
                logger.finest("adding isins to filter, callback execution expected");
                model.addToFilter(positionIsinsPrices.keySet());
            }
            int preferredHeight = positionsPanel.getPreferredSize().height;
            positionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, preferredHeight));
            dialog.setSize(dialog.getPreferredSize());
            logger.exiting(this.getClass().getName(), "updatePanel");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            logger.exiting(this.getClass().getName(), "actionPerformed", e);
            //noinspection unchecked
            JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
            this.selectedAccount = comboBox.getSelectedIndex();
            updatePanel(this.selectedAccount);
            logger.exiting(this.getClass().getName(), "actionPerformed");
        }

        private void updateValues(String isin) {
            logger.entering(this.getClass().getName(), "updateValues", isin);
            double price = positionIsinsPrices.get(isin);
            positionIsinsLabels.get(isin).setText(Double.toString(price));
            logger.exiting(this.getClass().getName(), "updateValues");
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            logger.entering(this.getClass().getName(), "propertyChange", evt);
            switch (evt.getPropertyName()) {
                case "Accounts":
                    //noinspection unchecked
                    this.accountList = (List<NolStatementAPI>) evt.getNewValue();
                    updatePanel(this.selectedAccount);
                    logger.finest("updated account panel");
                    break;
                case "Quotes":
                    NolRecentInfoAPI quote = (NolRecentInfoAPI) evt.getNewValue();
                    String isin = quote.getTicker().getIsin();
                    if (quote.getBitMask().get("ReferPrice")) {
                        this.positionIsinsPrices.replace(isin, quote.getReferPrice() * this.positionIsinsCount.get(isin));
                        updateValues(isin);
                        //remove redundant isin after price updates
                        if (!isinsInModelFilter.contains(isin)) {

                            Set<String> tickerISINSinFilter = model
                                    .getTickersInFilter()
                                    .stream()
                                    .map(NolTickerAPI::getIsin)
                                    .collect(Collectors.toSet());

                            if (tickerISINSinFilter.contains(isin)) {
                                Set<String> tmp = new HashSet<>();
                                tmp.add(isin);
                                model.removeFromFilter(tmp);
                            }
                        }
                    }
                    logger.finest("updated position values");
                    break;
            }
            logger.exiting(this.getClass().getName(), "propertyChange");
        }

        JPanel getPane() {
            return positionsPanel;
        }
    }
}
