package app.gui.dialog.TickerSelector;

import app.API.PublicAPI.DefaultFilter;
import app.API.PublicAPI.Ticker;
import app.gui.Model;
import app.gui.dialog.GUIModel;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectTickersModel extends GUIModel {

    private final DefaultFilter<Ticker> filter;
    SelectTickersModel(Model mainModel) {
        super(mainModel);
        filter = new DefaultFilter<>(mainModel.getModelFilter());
    }
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        System.out.println("in select tickers model");
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
    public List<Ticker> getTickers() {
        return new ArrayList<>(mainModel.getTickers());
    }
}
