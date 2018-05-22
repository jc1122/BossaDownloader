package app.gui.dialog.generifiedSaveToCSV;

import app.API.BossaAPI;
import app.gui.dialog.GUIView;
import app.gui.tickerSelector.TickerTable;

import javax.swing.*;
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
        }
    }

    private TickerTable tickerTable;

    private JButton startSaving, stopSaving;

    SaveToCSVView(M saveToCSVController, K saveToCSVModel) {
        super(saveToCSVController, saveToCSVModel);
    }

    @Override
    public void createGUI() {
        JPanel buttonPane = new JPanel();
        startSaving = new JButton("Start saving");
        startSaving.addActionListener((e) -> controller.startSaving());

        stopSaving = new JButton("Stop saving");
        stopSaving.addActionListener((e) -> controller.stopSaving());

        tickerTable = new TickerTable(new ArrayList<>(model.getTickersInFilter()), "Tickers to collect");
        dialog.add(tickerTable.getPane());
        dialog.add(buttonPane);
    }

    void setStartSavingEnabled(boolean enabled) {
        startSaving.setEnabled(enabled);
    }

    void setStopSavingEnabled(boolean enabled) {
        stopSaving.setEnabled(enabled);
    }
}
