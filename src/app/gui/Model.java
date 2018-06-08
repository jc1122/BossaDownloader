package app.gui;

import app.API.BossaAPI;
import app.API.nolObjects.NolTickerAPI;
import app.API.nolObjects.NolTickersAPI;
import app.API.properties.PropertyAPI;
import app.API.enums.TypeOfList;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Communicates with NOL3 using {@link BossaAPI}.
 */
public class Model {
    private final Map<String, PropertyAPI> propertyMap = BossaAPI.getPropertyMap();

    /**
     * {@link BossaAPI#addToFilter(Set)}
     *
     * @param isins of refactoredTickerSelector to be tracked
     */
    public String addToFilter(Set<String> isins) {
        return BossaAPI.addToFilter(isins);
    }

    /**
     * {@link BossaAPI#addTickersToFilter(Set)}
     *
     * @param tickers to be tracked
     */
    public String addTickersToFilter(Set<NolTickerAPI> tickers) {
        return BossaAPI.addTickersToFilter(tickers);
    }

    /**
     * {@link BossaAPI#getTickerISINSInFilter()}
     *
     * @return isins of currently tracked refactoredTickerSelector
     */
    public Set<String> getTickerISINSInFilter() {
        return BossaAPI.getTickerISINSInFilter();
    }

    /**
     * {@link BossaAPI#getTickersInFilter()}
     *
     * @return currently tracked refactoredTickerSelector
     */
    public Set<NolTickerAPI> getTickersInFilter() {
        return BossaAPI.getTickersInFilter();
    }

    /**
     * {@link BossaAPI#clearFilter()}
     *
     * @return message of success or failure
     */
    public String clearFilter() {
        return BossaAPI.clearFilter();
    }

    /**
     * {@link BossaAPI#removeFromFilter(Set)}
     *
     * @param isins stop tracking the refactoredTickerSelector with given isins
     */
    public String removeFromFilter(Set<String> isins) {
        return BossaAPI.removeFromFilter(isins);
    }

    /**
     * {@link BossaAPI#initialize()}
     *
     * @return success or error message
     */
    public String initialize() {
        return BossaAPI.initialize();
    }

    /**
     * {@link BossaAPI#shutdown()}
     *
     * @return success or error message
     */
    public String shutdown() {
        return BossaAPI.shutdown();
    }

    /**
     * {@link BossaAPI#getVersion()}
     *
     * @return success or error message
     */
    public String getVersion() {
        return BossaAPI.getVersion();
    }

    /**
     * The {@code listener} will be added to each property which inherits from {@link PropertyAPI}
     * and is stored in {@link BossaAPI#getPropertyMap()}.
     *
     * @param listener callback for property
     */
    public void addPropertyListener(PropertyChangeListener listener) {
        for (String property : propertyMap.keySet()) {
            propertyMap.get(property).addPropertyChangeListener(listener);
        }
    }

    /**
     * The {@code listener} will be removed from each property which inherits from {@link PropertyAPI}
     * and is stored in {@link BossaAPI#getPropertyMap()}.
     *
     * @param listener callback for property
     */
    public void removePropertyListener(PropertyChangeListener listener) {
        for (String property : propertyMap.keySet()) {
            propertyMap.get(property).removePropertyChangeListener(listener);
        }
    }

    /**
     * {@link PropertyAPI#getProperty()}
     *
     * @param property any property of {@link PropertyAPI}
     * @return property, will need to be cast to appropriate class (class name should be the same as property name)
     */
    public Object getProperty(String property) {
        return propertyMap.get(property).getProperty();
    }

    /**
     * {@link NolTickersAPI#getTickers(TypeOfList, NolTickerAPI)}
     *
     * @param typeOfList {@link TypeOfList}
     * @param in_ticker  {@link NolTickersAPI#getTickers(TypeOfList, NolTickerAPI)}
     * @return {@link NolTickersAPI#getTickers(TypeOfList, NolTickerAPI)}
     */
    public List<NolTickerAPI> getTickers(TypeOfList typeOfList, NolTickerAPI in_ticker) {
        return NolTickersAPI.getTickers(typeOfList, in_ticker);
    }
}
