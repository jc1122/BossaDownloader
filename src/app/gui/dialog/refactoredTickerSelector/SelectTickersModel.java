package app.gui.dialog.refactoredTickerSelector;

import app.API.BossaAPI;
import app.API.TypeOfList;
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
        Set<BossaAPI.NolTickerAPI> allTickers = new HashSet<>(mainModel.getTickers(TypeOfList.ALL, null));
        allTickers.removeAll(mainModel.getTickersInFilter());

        List<BossaAPI.NolTickerAPI> tickers = new ArrayList<>(allTickers);
        List<BossaAPI.NolTickerAPI> tickersInFilter = new ArrayList<>(mainModel.getTickersInFilter());

    }
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

    }

    public void clearFilter() {
        mainModel.clearFilter();
    }

    public void addTickersToFilter(Set<BossaAPI.NolTickerAPI> isins) {
        mainModel.addTickersToFilter(isins);
    }

    public List<BossaAPI.NolTickerAPI> getTickersInFilter() {
        return new ArrayList<>(mainModel.getTickersInFilter());
    }
    public List<BossaAPI.NolTickerAPI> getTickers() {
        return mainModel.getTickers(TypeOfList.ALL, null);
    }
}
