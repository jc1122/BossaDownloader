package app.gui.saveToCSV;

import app.API.BossaAPI;
import app.gui.tickerSelector.TickerTableModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;
import java.util.Set;
import java.util.ArrayList;

public class SaveToCSVModel implements PropertyChangeListener  {
    private Set<BossaAPI.NolTickerAPI> tickersInFilter;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    void removePropertyChangeListener(PropertyChangeListener listaner) {
        propertyChangeSupport.removePropertyChangeListener(listaner);
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if(Objects.equals(propertyChangeEvent.getPropertyName(), "TickersInFilter")) {
            //noinspection unchecked
            this.tickersInFilter = (Set<BossaAPI.NolTickerAPI>) propertyChangeEvent.getNewValue();
            this.propertyChangeSupport.firePropertyChange("TickersInFilter",propertyChangeEvent.getOldValue(), tickersInFilter);
        }
    }

    SaveToCSVModel(app.gui.Model model) {
        model.addPropertyListener(this);
        tickersInFilter = model.getTickersInFilter();
    }

    public Set<BossaAPI.NolTickerAPI> getTickersInFilter() {
        return tickersInFilter;
    }

    public void startSaving() {
        //TODO add functionality for opening and writing to csv files
    }

    public void stopSaving() {
        //TODO add functionality for closing csv files
    }
}
