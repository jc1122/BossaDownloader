package app.gui;

import app.API.JNAinterface.DefaultFilter;
import app.API.PublicAPI.*;

import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Set;

/**
 * Communicates with NOL3.
 */
public class Model implements Properties<String, PropertyAPI<?,String>>, OnOffOperations {
    private final Properties<String, PropertyAPI<?,String>> properties;// = Properties.getProperties();

    private final FilterOperations<Ticker> filterOperations;
    private final OnOffOperations onOff;

    public DefaultFilter getModelFilter() {
        return modelFilter;
    }

    private final DefaultFilter modelFilter;

    public Model(Properties<String, PropertyAPI<?, String>> properties, OnOffOperations onOff) {

        this.properties = properties;
        this.onOff = onOff;

        modelFilter = new DefaultFilter();//Filter.getMasterFilter();
        this.filterOperations = modelFilter;
    }

    /**

     *
     * @return success or error message
     */
    @Override
    public String initialize() {
        return onOff.initialize();
    }

    /**
     *
     * @return success or error message
     */
    @Override
    public String shutdown() {
        return onOff.shutdown();
    }

    /**
     *
     * @return success or error message
     */
    @Override
    public String getVersion() {
        return onOff.getVersion();
    }

    @Override
    public Map<String, PropertyAPI<?, String>> getProperties() {
        return null;
    }

    /**
     * The {@code listener} will be added to each property which inherits from {@link PropertyAPI}
     * and is stored
     *
     * @param listener callback for property
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        properties.addPropertyChangeListener(listener);
    }

    /**
     * The {@code listener} will be removed from each property which inherits from {@link PropertyAPI}
     * and is stored
     *
     * @param listener callback for property
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        properties.removePropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        properties.addPropertyChangeListener(propertyName, listener);
    }

    @Override
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        properties.removePropertyChangeListener(propertyName, listener);
    }

    /**
     *
     * @param property any property of {@link PropertyAPI}
     * @return property, will need to be cast to appropriate class (class name should be the same as property name)
     */
    @Override
    public PropertyAPI<?,String> getProperty(String property) {
        return properties.getProperty(property);
    }

    /**
     *
     */
    public Set<Ticker> getTickers() {
        return (Set<Ticker>) getProperty("Tickers").getValue();
    }

    public Set<Ticker> getTickersInFilter() {return filterOperations.getTickersInFilter();}
}
