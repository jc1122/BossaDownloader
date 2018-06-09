package app.gui;

import app.API.FilterOperations;
import app.API.JNAenums.TypeOfList;
import app.API.JNAinterface.NolTickerAPI;
import app.API.JNAinterface.NolTickersAPI;
import app.API.OnOffOperations;
import app.API.Properties;
import app.API.PropertyAPI;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Communicates with NOL3.
 */
public class Model {
    private final Map<String, PropertyAPI> propertyMap;// = Properties.getPropertyMap();

    private final FilterOperations filterOperations;
    private final OnOffOperations onOff;

    public Model(FilterOperations filterOperations, Properties properties, OnOffOperations onOff) {
        this.filterOperations = filterOperations;
        this.propertyMap = properties.getPropertyMap();
        this.onOff = onOff;
    }


    /**
     *
     * @param tickers to be tracked
     */
    public String addTickersToFilter(Set<NolTickerAPI> tickers) {
        return filterOperations.addTickersToFilter(tickers);
    }

    /**
     *
     * @return currently tracked refactoredTickerSelector
     */
    public Set<NolTickerAPI> getTickersInFilter() {
        return filterOperations.getTickersInFilter();
    }

    /**
     *
     * @return message of success or failure
     */
    public String clearFilter() {
        return filterOperations.clearFilter();
    }

    /**
     *
     *
     * @param tickers stop tracking the refactoredTickerSelector with given tickers
     */
    public String removeTickersFromFilter(Set<NolTickerAPI> tickers) {
        return filterOperations.removeTickersFromFilter(tickers);
    }

    /**

     *
     * @return success or error message
     */
    public String initialize() {
        return onOff.initialize();
    }

    /**
     *
     * @return success or error message
     */
    public String shutdown() {
        return onOff.shutdown();
    }

    /**
     *
     * @return success or error message
     */
    public String getVersion() {
        return onOff.getVersion();
    }

    /**
     * The {@code listener} will be added to each property which inherits from {@link PropertyAPI}
     * and is stored
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
     * and is stored
     *
     * @param listener callback for property
     */
    public void removePropertyListener(PropertyChangeListener listener) {
        for (String property : propertyMap.keySet()) {
            propertyMap.get(property).removePropertyChangeListener(listener);
        }
    }

    /**
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
