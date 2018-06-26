package app.gui.dialog.SaveToCSV;

import app.API.PublicAPI.Ticker;
import app.gui.dialog.GUIModel;

import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

//TODO decouple this from main model, use separate filter for this one
public class SaveToCSVModel extends GUIModel {
    private Set<Ticker> tickersInFilter;
    private CSVSaver saver;

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (Objects.equals(propertyChangeEvent.getPropertyName(), "Filter")) {
            //noinspection unchecked
            this.tickersInFilter = (Set<Ticker>) propertyChangeEvent.getNewValue();
            this.propertyChangeSupport.firePropertyChange("Filter", propertyChangeEvent.getOldValue(), tickersInFilter);
        }
        if(Objects.equals(propertyChangeEvent.getPropertyName(), "Quotes")) {
            this.propertyChangeSupport.firePropertyChange("Quotes", propertyChangeEvent.getOldValue(), propertyChangeEvent.getNewValue());
        }
    }

    protected SaveToCSVModel(app.gui.Model model) {
        super(model);
        tickersInFilter = model.getTickersInFilter();
        saver = new CSVSaver();
        //this.addPropertyChangeListener(saver);
    }

    public Set<Ticker> getTickersInFilter() {
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
