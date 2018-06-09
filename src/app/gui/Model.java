package app.gui;

import app.API.JNAinterface.*;
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

    private FilterOperations filterOperations;

    public Model(FilterOperations filterOperations) {
        this.filterOperations = filterOperations;
    }


    /**
     * {@link BossaAPI#addToFilter(Set)}
     *
     * @param isins of refactoredTickerSelector to be tracked
     */
//    public String addToFilter(Set<String> isins) {
//        return filterOperations.addToFilter(isins);
//    }

    /**
     * {@link BossaAPI#addTickersToFilter(Set)}
     *
     * @param tickers to be tracked
     */
    public String addTickersToFilter(Set<NolTickerAPI> tickers) {
        return filterOperations.addTickersToFilter(tickers);
    }

    /**
     * {@link BossaAPI#getTickersInFilter()}
     *
     * @return currently tracked refactoredTickerSelector
     */
    public Set<NolTickerAPI> getTickersInFilter() {
        return filterOperations.getTickersInFilter();
    }

    /**
     * {@link BossaAPI#clearFilter()}
     *
     * @return message of success or failure
     */
    public String clearFilter() {
        return filterOperations.clearFilter();
    }

    /**
     * {@link BossaAPI#removeFromFilter(Set)}
     *
     * @param tickers stop tracking the refactoredTickerSelector with given tickers
     */
    public String removeTickersFromFilter(Set<NolTickerAPI> tickers) {
        return filterOperations.removeTickersFromFilter(tickers);
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
