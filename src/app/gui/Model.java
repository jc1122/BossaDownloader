package app.gui;

import app.API.BossaAPI;
import app.API.TypeOfList;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Model {
    private Map<String, BossaAPI.PropertyAPI> propertyMap = BossaAPI.getPropertyMap();


    public void addToFilter(Set<String> isins) {
        BossaAPI.addToFilter(isins);
    }

    public void addTickersToFilter(Set<BossaAPI.NolTickerAPI> tickers) {
        BossaAPI.addTickersToFilter(tickers);
    }

    public Set<String> getTickerISINSInFilter() {
        return BossaAPI.getTickerISINSInFilter();
    }

    public Set<BossaAPI.NolTickerAPI> getTickersInFilter() {
        return BossaAPI.getTickersInFilter();
    }
    public void clearFilter() {
        BossaAPI.clearFilter();
    }
    public void removeFromFilter(Set<String> isins) {
        BossaAPI.removeFromFilter(isins);
    }

    public void startAPI() {
        BossaAPI.initialize();
    }

    public void stopAPI() {
        System.out.println(BossaAPI.shutdown());
    }

    public String getAPIversion() {
        return BossaAPI.getVersion();
    }

    public void addPropertyListener(PropertyChangeListener listener) {
        for (String property : propertyMap.keySet()) {
            propertyMap.get(property).addPropertyChangeListener(listener);
        }
    }

    public Object getProperty(String property) {
        return propertyMap.get(property).getProperty();
    }

    public List<BossaAPI.NolTickerAPI> getTickers(TypeOfList typeOfList, BossaAPI.NolTickerAPI in_ticker) {
        return BossaAPI.getTickers(typeOfList, in_ticker);
    }
}
