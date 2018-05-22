package app.gui.dialog.generifiedSaveToCSV;

import app.API.BossaAPI;
import app.gui.dialog.GUIModel;

import java.beans.PropertyChangeEvent;
import java.util.Objects;
import java.util.Set;

public class SaveToCSVModel extends GUIModel {
    private Set<BossaAPI.NolTickerAPI> tickersInFilter;

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (Objects.equals(propertyChangeEvent.getPropertyName(), "TickersInFilter")) {
            //noinspection unchecked
            this.tickersInFilter = (Set<BossaAPI.NolTickerAPI>) propertyChangeEvent.getNewValue();
            this.propertyChangeSupport.firePropertyChange("TickersInFilter", propertyChangeEvent.getOldValue(), tickersInFilter);
        }
    }

    SaveToCSVModel(app.gui.Model model) {
        super(model);
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
