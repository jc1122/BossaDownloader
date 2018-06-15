package app.gui.dialog.statement;


import app.API.JNAinterface.NolRecentInfoAPI;
import app.API.PublicAPI.Position;
import app.API.PublicAPI.Statement;
import app.API.PublicAPI.Ticker;
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

        private List<Statement> accountList;

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

            this.accountList = (List<Statement>) evt.getNewValue();
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

        private void addAccountsToComboBox(List<Statement> accounts) {
            logger.entering(this.getClass().getName(), "addAccountSelectionListener", accounts);
            for (Statement account : accounts) {
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
        private List<Statement> accountList;

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
            Statement defaultAccount = accountList.get(Math.max(index, 0)); //Max is just in case, when no element = -1  selected
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
            this.accountList = (List<Statement>) evt.getNewValue();
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
        private List<Statement> accountList;
        private Map<Ticker, Double> positionTickersPrices;
        private Map<Ticker, JLabel> positionTickersLabels;
        private Map<Ticker, Integer> positionTickersCount;
        private Set<Ticker> tickersInModelFilter;

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

            positionTickersPrices = new HashMap<>();
            positionTickersLabels = new HashMap<>();
            positionTickersCount = new HashMap<>();

            this.accountList = model.getAccounts(); //TODO will cause error when api is in investor offline status
            tickersInModelFilter = model.getTickersInFilter();
            //TODO this may be buggy
            updatePanel(0);
            logger.exiting(this.getClass().getName(), "constructor");
        }

        private synchronized void updatePanel(int index) {
            logger.entering(this.getClass().getName(), "updatePanel", index);
            Statement currentAccount = accountList.get(index);

            positionsPanel.removeAll();
            positionsPanel.add(new JLabel("Ticker"));
            positionsPanel.add(new JLabel("Securities count"));
            positionsPanel.add(new JLabel("Blocked for sale"));
            positionsPanel.add(new JLabel("Value"));

            positionTickersPrices.clear();
            positionTickersLabels.clear();
            positionTickersCount.clear();

            //TODO refactor to a set of isins
            List<Position> positions = currentAccount.getPositions();
            if (!positions.isEmpty()) {
                for (Position position : positions) {
                    Ticker ticker = position.getTicker();
                    positionTickersPrices.put(ticker, -1.);
                    positionTickersLabels.put(ticker, new JLabel());
                    positionTickersCount.put(ticker, -10);
                }

                for (Position position : positions) {
                    Ticker ticker = position.getTicker();
                    positionsPanel.add(new JLabel(position.getTicker().getName()));
                    positionsPanel.add(new JLabel(Integer.toString(position.getAcc110())));
                    positionsPanel.add(new JLabel(Integer.toString(position.getAcc120())));
                    positionsPanel.add(positionTickersLabels.get(ticker));
                    positionTickersCount.replace(ticker, position.getAcc110() + position.getAcc120());
                }

                //add to filter forces callback, it must be called last, otherwise there may be race condition with code above
                logger.finest("adding isins to filter, callback execution expected");
                model.addTickersToFilter(positions.stream().map(Position::getTicker).collect(Collectors.toSet()));
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

        private void updateValues(Ticker ticker) {
            logger.entering(this.getClass().getName(), "updateValues", ticker);
            double price = positionTickersPrices.get(ticker);
            positionTickersLabels.get(ticker).setText(Double.toString(price));
            logger.exiting(this.getClass().getName(), "updateValues");
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            logger.entering(this.getClass().getName(), "propertyChange", evt);
            switch (evt.getPropertyName()) {
                case "Accounts":
                    //noinspection unchecked
                    this.accountList = (List<Statement>) evt.getNewValue();
                    updatePanel(this.selectedAccount);
                    logger.finest("updated account panel");
                    break;
                case "Quotes":
                    NolRecentInfoAPI quote = (NolRecentInfoAPI) evt.getNewValue();
                    Ticker ticker = quote.getTicker();
                    if (quote.getBitMask().get("ReferPrice")) {
                        System.out.println(ticker);
                        System.out.println(quote.getReferPrice());
                        System.out.println(this.positionTickersCount.get(ticker));
                        this.positionTickersPrices.replace(ticker, quote.getReferPrice() * this.positionTickersCount.get(ticker));
                        updateValues(ticker);
                        //remove redundant isin after price updates
                        if (!tickersInModelFilter.contains(ticker)) {

                            Set<String> tickerISINSinFilter = model
                                    .getTickersInFilter()
                                    .stream()
                                    .map(Ticker::getIsin)
                                    .collect(Collectors.toSet());

                            if (tickerISINSinFilter.contains(ticker)) {
                                Set<Ticker> tmp = new HashSet<>();
                                tmp.add(ticker);
                                model.removeTickersFromFilter(tmp);
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
