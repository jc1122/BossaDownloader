package app.gui;

import app.API.BossaAPI;
import app.API.TypeOfList;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Communicates with NOL3 using {@link BossaAPI}.
 */
public class Model {
    private Map<String, BossaAPI.PropertyAPI> propertyMap = BossaAPI.getPropertyMap();

    /**
     * {@link BossaAPI#addToFilter(Set)}
     * @param isins of tickers to be tracked
     */
    public String addToFilter(Set<String> isins) {
        return BossaAPI.addToFilter(isins);
    }

    /**
     * {@link BossaAPI#addTickersToFilter(Set)}
     * @param tickers to be tracked
     */
    public String addTickersToFilter(Set<BossaAPI.NolTickerAPI> tickers) {
        return BossaAPI.addTickersToFilter(tickers);
    }

    /**
     * {@link BossaAPI#getTickerISINSInFilter()}
     * @return isins of currently tracked tickers
     */
    public Set<String> getTickerISINSInFilter() {
        return BossaAPI.getTickerISINSInFilter();
    }

    /**
     * {@link BossaAPI#getTickersInFilter()}
     * @return currently tracked tickers
     */
    public Set<BossaAPI.NolTickerAPI> getTickersInFilter() {
        return BossaAPI.getTickersInFilter();
    }

    /**
     * {@link BossaAPI#clearFilter()}
     * @return message of success or failure
     */
    public String clearFilter() {
        return BossaAPI.clearFilter();
    }

    /**
     * {@link BossaAPI#removeFromFilter(Set)}
     * @param isins stop tracking the tickers with given isins
     */
    public String removeFromFilter(Set<String> isins) {
        return BossaAPI.removeFromFilter(isins);
    }

    /**
     * {@link BossaAPI#initialize()}
     * @return success or error message
     */
    public String initialize() {
        return BossaAPI.initialize();
    }

    /**
     * {@link BossaAPI#shutdown()}
     * @return success or error message
     */
    public String shutdown() {
        return BossaAPI.shutdown();
    }

    /**
     * {@link BossaAPI#getVersion()}
     * @return success or error message
     */
    public String getVersion() {
        return BossaAPI.getVersion();
    }

    /**
     * The {@code listener} will be added to each property which inherits from {@link BossaAPI.PropertyAPI}
     * and is stored in {@link BossaAPI#getPropertyMap()}.
     * @param listener callback for property
     */
    public void addPropertyListener(PropertyChangeListener listener) {
        for (String property : propertyMap.keySet()) {
            propertyMap.get(property).addPropertyChangeListener(listener);
        }
    }

    /**
     * {@link BossaAPI.PropertyAPI#getProperty()}
     * @param property any property of {@link BossaAPI.PropertyAPI}
     * @return property, will need to be cast to appropriate class (class name should be the same as property name)
     */
    public Object getProperty(String property) {
        return propertyMap.get(property).getProperty();
    }

    /**
     * {@link BossaAPI#getTickers(TypeOfList, BossaAPI.NolTickerAPI)}
     * @param typeOfList {@link TypeOfList}
     * @param in_ticker {@link BossaAPI#getTickers(TypeOfList, BossaAPI.NolTickerAPI)}
     * @return {@link BossaAPI#getTickers(TypeOfList, BossaAPI.NolTickerAPI)}
     */
    public List<BossaAPI.NolTickerAPI> getTickers(TypeOfList typeOfList, BossaAPI.NolTickerAPI in_ticker) {
        return BossaAPI.getTickers(typeOfList, in_ticker);
    }
}
