package app.API.PublicAPI;

import app.API.JNAinterface.BossaAPI;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Filter extends PropertyAPI<Set<Ticker>, String> {
    private static final Filter INSTANCE = new Filter();

    public static DefaultFilter<Ticker> getMasterFilter() {
        return MASTER_FILTER;
    }

    private static final DefaultFilter<Ticker> MASTER_FILTER = BossaAPI.MasterFilter.getInstance();

    private Filter() {
        super("Filter");
        this.property = new HashSet<>();
    }

    public static Filter getInstance() {
        return INSTANCE;
    }

    public void update(Set<Ticker> tickersInFilter) {
        this.propertyChangeSupport
                .firePropertyChange("Filter", this.property, Collections.unmodifiableSet(tickersInFilter));
        this.property = new HashSet<>(tickersInFilter);
    }


    public static void addChild(DefaultFilter<Ticker> filter) {
        MASTER_FILTER.addChild(filter);
    }

    public static Set<DefaultFilter<Ticker>> getWatchers(Ticker ticker) {
        return MASTER_FILTER.getWatchers(ticker);
    }
}
