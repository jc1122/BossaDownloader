package app.API.properties;

import app.API.nolObjects.NolTickerAPI;

import java.util.HashSet;
import java.util.Set;

public final class TickersInFilter extends PropertyAPI<Set<NolTickerAPI>> {
    private static final TickersInFilter INSTANCE = new TickersInFilter();

    private TickersInFilter() {
        this.property = new HashSet<>();
    }

    public static TickersInFilter getInstance() {
        return INSTANCE;
    }

    public void update(Set<NolTickerAPI> tickersInFilter) {
        Set<NolTickerAPI> oldValue = this.property;
        this.property = tickersInFilter;
        this.propertyChangeSupport.firePropertyChange("TickersInFilter", oldValue, this.property);
    }

}
