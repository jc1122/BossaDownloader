package app.gui.dialog.SaveToCSV;

import app.API.JNAinterface.NolTickerAPI;
import app.gui.dialog.GUIModel;

import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SaveToCSVModel extends GUIModel {
    private Set<NolTickerAPI> tickersInFilter;
    private CSVSaver saver;

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        System.out.println("im here");
        if (Objects.equals(propertyChangeEvent.getPropertyName(), "TickersInFilter")) {
            //noinspection unchecked
            this.tickersInFilter = (Set<NolTickerAPI>) propertyChangeEvent.getNewValue();
            this.propertyChangeSupport.firePropertyChange("TickersInFilter", propertyChangeEvent.getOldValue(), tickersInFilter);
        }
        if(Objects.equals(propertyChangeEvent.getPropertyName(), "Quotes")) {
            this.propertyChangeSupport.firePropertyChange("Quotes", propertyChangeEvent.getOldValue(), propertyChangeEvent.getNewValue());
        }
    }

    protected SaveToCSVModel(app.gui.Model model) {
        super(model);
        tickersInFilter = model.getTickersInFilter();
        saver = new CSVSaver();
        this.addPropertyChangeListener(saver);
    }

    public Set<NolTickerAPI> getTickersInFilter() {
        return tickersInFilter;
    }

    public void startSaving() {
        saver.startSaving(new HashSet<>(tickersInFilter));
    }

    public void stopSaving() {
        saver.stopSaving();
    }

    public void setCSVSaver(CSVSaver saver) {
        this.saver = saver;
    }
}
