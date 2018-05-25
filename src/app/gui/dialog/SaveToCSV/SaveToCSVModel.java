package app.gui.dialog.SaveToCSV;

import app.API.BossaAPI;
import app.gui.dialog.GUIModel;

import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SaveToCSVModel extends GUIModel {
    private Set<BossaAPI.NolTickerAPI> tickersInFilter;
    private CSVSaver saver;

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        System.out.println("im here");
        if (Objects.equals(propertyChangeEvent.getPropertyName(), "TickersInFilter")) {
            //noinspection unchecked
            this.tickersInFilter = (Set<BossaAPI.NolTickerAPI>) propertyChangeEvent.getNewValue();
            this.propertyChangeSupport.firePropertyChange("TickersInFilter", propertyChangeEvent.getOldValue(), tickersInFilter);
        }
        if(Objects.equals(propertyChangeEvent.getPropertyName(), "Quotes")) {
            this.propertyChangeSupport.firePropertyChange("Quotes", propertyChangeEvent.getOldValue(), propertyChangeEvent.getNewValue());
        }
    }

    protected SaveToCSVModel(app.gui.Model model, CSVSaver saver) {
        super(model);
        tickersInFilter = model.getTickersInFilter();

        this.saver = saver;
        this.addPropertyChangeListener(saver);
    }

    public Set<BossaAPI.NolTickerAPI> getTickersInFilter() {
        return tickersInFilter;
    }

    public void startSaving() {

        saver.startSaving(new HashSet<>(tickersInFilter));
    }

    public void stopSaving() {
        saver.stopSaving();
    }
}
