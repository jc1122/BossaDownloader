package app.gui.statement;

import app.API.BossaAPI;
import app.gui.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO check behavior in concurrency; remember to remove tickers from filter on close!
class PositionsPane implements PropertyChangeListener, ActionListener {
    private JPanel positionsPanel;
    private GridLayout positionsPanelLayout;
    private Model model;
    private int selectedAccount;
    private List<BossaAPI.NolStatementAPI> accountList;
    private Map<String, Double> positionIsinsPrices;
    private Map<String, JLabel> positionIsinsLabels;
    private Map<String, Integer> positionIsinsCount;

    private StatementDialog dialog;

    PositionsPane(Model model, StatementDialog dialog) {
        this.dialog = dialog;
        this.model = model;
        try {
            this.model.addPropertyListener(this);
        } catch (NullPointerException e) {
            throw new NullPointerException("Unable to get accounts! Is API initialized?");
        }

        positionsPanel = new JPanel();
        //positionsPanel.setBackground(new Color(133, 133, 233));
        positionsPanelLayout = new GridLayout(0, 4);
        positionsPanel.setLayout(positionsPanelLayout);

        positionIsinsPrices = new HashMap<>();
        positionIsinsLabels = new HashMap<>();
        positionIsinsCount = new HashMap<>();

        this.accountList = (List<BossaAPI.NolStatementAPI>) model.getProperty("Accounts"); //TODO will cause error when api is in investor offline status
        //TODO this may be buggy
        updatePanel(0);

    }

    private synchronized void updatePanel(int index) {
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
            model.addToFilter(positionIsinsPrices.keySet());
        }
        int preferredHeight = positionsPanel.getPreferredSize().height;
        positionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, preferredHeight));
        dialog.resize();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
        this.selectedAccount = comboBox.getSelectedIndex();
        updatePanel(this.selectedAccount);
    }

    private void updateValues(String isin) {
        double price = positionIsinsPrices.get(isin);
        positionIsinsLabels.get(isin).setText(Double.toString(price));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "Accounts":
                this.accountList = (List<BossaAPI.NolStatementAPI>) evt.getNewValue();
                updatePanel(this.selectedAccount);
                break;
            case "Quotes":
                BossaAPI.NolRecentInfoAPI quote = (BossaAPI.NolRecentInfoAPI) evt.getNewValue();
                String isin = quote.getTicker().getIsin();
                if (quote.getBitMask().get("Close")) {
                    this.positionIsinsPrices.replace(isin, quote.getClose() * this.positionIsinsCount.get(isin));
                    updateValues(isin);
                }
                break;
        }
    }

    public JPanel getPane() {
        return positionsPanel;
    }
}
