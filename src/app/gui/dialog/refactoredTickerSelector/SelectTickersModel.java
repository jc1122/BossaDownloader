package app.gui.dialog.refactoredTickerSelector;

import app.API.JNAenums.TypeOfList;
import app.API.Ticker;
import app.gui.Model;
import app.gui.dialog.GUIModel;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectTickersModel extends GUIModel {

    SelectTickersModel(Model mainModel) {
        super(mainModel);

        //TODO refactor this, possibly in BossaAPI, too many unnecessary conversions
        Set<Ticker> allTickers = new HashSet<>(mainModel.getTickers(TypeOfList.ALL, null));
        allTickers.removeAll(mainModel.getTickersInFilter());

        List<Ticker> tickers = new ArrayList<>(allTickers);
        List<Ticker> tickersInFilter = new ArrayList<>(mainModel.getTickersInFilter());

    }
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

    }

    public void clearFilter() {
        mainModel.clearFilter();
    }

    public void addTickersToFilter(Set<Ticker> isins) {
        mainModel.addTickersToFilter(isins);
    }

    public List<Ticker> getTickersInFilter() {
        return new ArrayList<>(mainModel.getTickersInFilter());
    }
    public List<Ticker> getTickers() {
        return mainModel.getTickers(TypeOfList.ALL, null);
    }
}
