package app.gui.dialog.saveToCSV;

import app.API.BossaAPI;
import app.gui.tickerSelector.TickerTable;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class SaveToCSVView implements PropertyChangeListener {
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (Objects.equals(propertyChangeEvent.getPropertyName(), "TickersInFilter")) {
            //noinspection unchecked
            Set<BossaAPI.NolTickerAPI> tickers = (Set<BossaAPI.NolTickerAPI>) propertyChangeEvent.getNewValue();
            tickerTable = new TickerTable(new ArrayList<>(tickers), "Tickers to collect");
        }
    }

    private final JDialog dialog;
    private final SaveToCSVController controller;
    private final SaveToCSVModel model;
    private TickerTable tickerTable;

    private JButton startSaving, stopSaving;

    SaveToCSVView(SaveToCSVController saveToCSVController, SaveToCSVModel saveToCSVModel) {
        controller = saveToCSVController;
        model = saveToCSVModel;
        dialog = new JDialog();
        model.addPropertyChangeListener(this);
    }

    void createGUI() {
        JPanel buttonPane = new JPanel();
        startSaving = new JButton("Start saving");
        startSaving.addActionListener((e) -> controller.startSaving());

        stopSaving = new JButton("Stop saving");
        stopSaving.addActionListener((e) -> controller.stopSaving());

        tickerTable = new TickerTable(new ArrayList<>(model.getTickersInFilter()), "Tickers to collect");
        dialog.add(tickerTable.getPane());
        dialog.add(buttonPane);
    }

    JDialog getDialog() {
        return dialog;
    }

    void setStartSavingEnabled(boolean enabled) {
        startSaving.setEnabled(enabled);
    }

    void setStopSavingEnabled(boolean enabled) {
        stopSaving.setEnabled(enabled);
    }
}
