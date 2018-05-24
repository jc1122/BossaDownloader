package app.gui.dialog.statement;

import app.API.BossaAPI;
import app.gui.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

//TODO check behavior in concurrency; remember to remove tickerSelector from filter on close!
class PositionsPane<K extends StatementModel> implements PropertyChangeListener, ActionListener {
    private static final Logger logger =
            Logger.getLogger(Controller.class.getName());
    private JPanel positionsPanel;
    private final K model;
    private int selectedAccount;
    private List<BossaAPI.NolStatementAPI> accountList;
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
        isinsInModelFilter = model.getTickerISINSinFilter();
        //TODO this may be buggy
        updatePanel(0);
        logger.exiting(this.getClass().getName(), "constructor");
    }

    private synchronized void updatePanel(int index) {
        logger.entering(this.getClass().getName(), "updatePanel", index);
        BossaAPI.NolStatementAPI currentAccount = accountList.get(index);

        positionsPanel.removeAll();
        positionsPanel.add(new JLabel("Ticker"));
        positionsPanel.add(new JLabel("Securities count"));
        positionsPanel.add(new JLabel("Blocked for sale"));
        positionsPanel.add(new JLabel("Value"));

        positionIsinsPrices.clear();
        positionIsinsLabels.clear();
        positionIsinsCount.clear();

        //TODO refactor to a set of isins
        List<BossaAPI.NolPosAPI> positions = currentAccount.getPositions();
        if (!positions.isEmpty()) {
            for (BossaAPI.NolPosAPI position : positions) {
                String isin = position.getTicker().getIsin();
                positionIsinsPrices.put(isin, -1.);
                positionIsinsLabels.put(isin, new JLabel());
                positionIsinsCount.put(isin, -10);
            }

            for (BossaAPI.NolPosAPI position : positions) {
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
                this.accountList = (List<BossaAPI.NolStatementAPI>) evt.getNewValue();
                updatePanel(this.selectedAccount);
                logger.finest("updated account panel");
                break;
            case "Quotes":
                BossaAPI.NolRecentInfoAPI quote = (BossaAPI.NolRecentInfoAPI) evt.getNewValue();
                String isin = quote.getTicker().getIsin();
                if (quote.getBitMask().get("Close")) {
                    this.positionIsinsPrices.replace(isin, quote.getClose() * this.positionIsinsCount.get(isin));
                    updateValues(isin);
                    //remove redundant isin after price updates
                    if (!isinsInModelFilter.contains(isin)) {
                        if (model.getTickerISINSinFilter().contains(isin)) {
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
