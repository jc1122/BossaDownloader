package app.API.JNAinterface;

import app.API.PropertyAPI;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

final class TickersInFilter extends PropertyAPI<Set<NolTickerAPI>> {
    private static final TickersInFilter INSTANCE = new TickersInFilter();

    private TickersInFilter() {
        this.property = new HashSet<>();
    }

    static TickersInFilter getInstance() {
        return INSTANCE;
    }

    public void update(Set<NolTickerAPI> tickersInFilter) {
        Set<NolTickerAPI> oldValue = this.property;
        this.property = Collections.unmodifiableSet(tickersInFilter);
        this.propertyChangeSupport.firePropertyChange("TickersInFilter", oldValue, this.property);
    }

}
