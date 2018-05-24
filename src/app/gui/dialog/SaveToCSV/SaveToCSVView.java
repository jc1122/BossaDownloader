package app.gui.dialog.SaveToCSV;

import app.API.BossaAPI;
import app.gui.dialog.GUIView;
import app.gui.tickerSelector.TickerTable;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class SaveToCSVView<K extends SaveToCSVModel, L extends SaveToCSVView<K, L, M>, M extends SaveToCSVController<K, L>>
        extends GUIView<K, L, M> {
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (Objects.equals(propertyChangeEvent.getPropertyName(), "TickersInFilter")) {
            //noinspection unchecked
            Set<BossaAPI.NolTickerAPI> tickers = (Set<BossaAPI.NolTickerAPI>) propertyChangeEvent.getNewValue();
            tickerTable = new TickerTable(new ArrayList<>(tickers), "Tickers to collect");
            //tickerTable.getModel().fireTableDataChanged();
            //TODO fix bug in TickerTable - the line above does not update the mainModel and the parent view is not updated
        }
    }

    private TickerTable tickerTable;

    private JButton startSaving, stopSaving;

    protected SaveToCSVView(M saveToCSVController, K saveToCSVModel) {
        super(saveToCSVController, saveToCSVModel);
    }

    @Override
    public void createGUI() {
        dialog.setLayout(new BorderLayout());

        JPanel buttonPane = new JPanel();
        startSaving = new JButton("Start saving");
        startSaving.addActionListener((e) -> controller.startSaving());
        buttonPane.add(startSaving);

        stopSaving = new JButton("Stop saving");
        stopSaving.addActionListener((e) -> controller.stopSaving());
        buttonPane.add(stopSaving);

        tickerTable = new TickerTable(new ArrayList<>(model.getTickersInFilter()), "Tickers to collect");
        JPanel tickerPane = new JPanel();
        tickerPane.setLayout(new BoxLayout(tickerPane, BoxLayout.LINE_AXIS));
        tickerPane.add(tickerTable.getPane());

        dialog.add(tickerPane, BorderLayout.CENTER);
        dialog.add(buttonPane, BorderLayout.PAGE_END);
        dialog.pack();
    }

    void setStartSavingEnabled(boolean enabled) {
        startSaving.setEnabled(enabled);
    }

    void setStopSavingEnabled(boolean enabled) {
        stopSaving.setEnabled(enabled);
    }
}
