package app.API.PublicAPI;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Filter extends PropertyAPI<Set<Ticker>, String> {
    private static final Filter INSTANCE = new Filter();

    public static AbstractFilter<Ticker> getMasterFilter() {
        return MASTER_FILTER;
    }

    private static final AbstractFilter<Ticker> MASTER_FILTER = new DefaultFilter();

    private Filter() {
        super("Filter");
        this.property = new HashSet<>();
    }

    public static Filter getInstance() {
        return INSTANCE;
    }

    public void update(Set<Ticker> tickersInFilter) {
        Set<Ticker> oldValue = this.property;
        this.property = Collections.unmodifiableSet(tickersInFilter);
        this.propertyChangeSupport.firePropertyChange("Filter", oldValue, this.property);
    }


    public static void addChild(AbstractFilter<Ticker> filter) {
        MASTER_FILTER.addChild(filter);
    }

    public static Set<AbstractFilter<Ticker>> getWatchers(Ticker ticker) {
        return MASTER_FILTER.getWatchers(ticker);
    }
}
