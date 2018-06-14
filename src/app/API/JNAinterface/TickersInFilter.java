package app.API.JNAinterface;

import app.API.PropertyAPI;
import app.API.Ticker;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

final class TickersInFilter extends PropertyAPI<Set<Ticker>, String> {
    private static final TickersInFilter INSTANCE = new TickersInFilter();

    private TickersInFilter() {
        super("TickersInFilter");
        this.property = new HashSet<>();
    }

    static TickersInFilter getInstance() {
        return INSTANCE;
    }

    public void update(Set<Ticker> tickersInFilter) {
        Set<Ticker> oldValue = this.property;
        this.property = Collections.unmodifiableSet(tickersInFilter);
        this.propertyChangeSupport.firePropertyChange("TickersInFilter", oldValue, this.property);
    }

}
