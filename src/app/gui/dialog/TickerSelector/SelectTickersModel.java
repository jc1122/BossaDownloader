package app.gui.dialog.TickerSelector;

import app.API.JNAinterface.DefaultFilter;
import app.API.PublicAPI.Ticker;
import app.gui.Model;
import app.gui.dialog.GUIModel;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SelectTickersModel extends GUIModel {

    private final DefaultFilter filter;
    SelectTickersModel(Model mainModel) {
        super(mainModel);
        filter = new DefaultFilter();
        filter.addPropertyChangeListener(this);
    }
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        System.out.println("in select tickers model " + propertyChangeEvent.getPropertyName());
    }

    public void clearFilter() {
        filter.clearFilter();
    }

    public void addTickersToFilter(Set<Ticker> isins) {
        filter.addTickersToFilter(isins);
    }

    public List<Ticker> getTickersInFilter() {
        return new ArrayList<>(filter.getTickersInFilter());
    }

    public DefaultFilter getFilter() {
        return filter;
    }
    public List<Ticker> getTickers() {
        return new ArrayList<>(mainModel.getTickers());
    }
}
