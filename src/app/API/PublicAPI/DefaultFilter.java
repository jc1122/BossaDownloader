package app.API.PublicAPI;

import app.API.JNAinterface.NolRecentInfoAPI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Adds listener to quotes. Manages watched tickers. Generic was only used for testing. Remove generics in the future.
 * TODO remove generics
 *
 * @param <T>
 */
public class DefaultFilter<T extends Ticker> implements FilterOperations<T>, PropertyChangeListener {
    /**
     * parent of this component, valid only for non-master nodes, top node should be a different implementation of
     * this class than the children
     */
    private final DefaultFilter<T> parent;
    private final Set<DefaultFilter<T>> childs;
    private final PropertyChangeSupport pcs;
    /**
     * map tickers in filter to the composites watching it
     */
    private final HashMap<T, Set<DefaultFilter<T>>> tickerWatchers;

    public DefaultFilter() {
        this(null);
    }

    public DefaultFilter(DefaultFilter<T> parent) {
        this.parent = parent;
        pcs = new PropertyChangeSupport(this);
        tickerWatchers = new HashMap<>();
        childs = new HashSet<>();

        if (parent != null) {
            parent.addChild(this);
            parent.addPropertyChangeListener(this);
        }

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void addChild(DefaultFilter<T> filter) {
        this.childs.add(filter);
    }

    public DefaultFilter getParent() {
        return parent;
    }

    /**
     * Add tickers to this filter and all of its parent filters
     *
     * @param tickers
     * @return
     */
    @Override
    public String addTickersToFilter(Set<T> tickers) {
        addToWatchers(tickers, this);
        return null;
    }

    /**
     * For given set of tickers, add a new watcher of each of them. Propagate through all parents.
     *
     * @param tickers
     * @param watcher
     */
    protected void addToWatchers(Set<T> tickers, DefaultFilter<T> watcher) {
        for (T ticker : tickers) {
            if (tickerWatchers.keySet().contains(ticker)) {
                tickerWatchers.get(ticker).add(watcher);
            } else {
                Set<DefaultFilter<T>> watchers = new HashSet<>();
                watchers.add(watcher);
                tickerWatchers.put(ticker, watchers);
            }
        }
        if (parent != null) {
            parent.addToWatchers(tickers, watcher);
        }
    }

    /**
     * Stops watching all given tickers. All child filters will stop watching these tickers too.
     * //TODO test this method. This function should remove tickers from child filters, then removeFromParentFilters should propagate the removal to parents
     *
     * @param tickers
     * @return
     * @throws IllegalStateException
     */
    @Override
    public String removeTickersFromFilter(Set<T> tickers) throws IllegalStateException {
        removeFromParent(tickers, this);
        removeFromChilds(tickers);
        return null;
    }

    protected void removeFromParent(Set<T> tickers, DefaultFilter<T> filter) {
        for (T ticker : tickers) {
            if (this.tickerWatchers.get(ticker) != null) {
                this.tickerWatchers.get(ticker).remove(filter);
                if (tickerWatchers.get(ticker).isEmpty()) {
                    tickerWatchers.remove(ticker);
                }
            }
        }
        if (parent != null) {
            parent.removeFromParent(tickers, filter);
        }
    }

    private void removeFromChilds(Set<T> tickers) {
        for (DefaultFilter<T> filter : childs) {
            filter.removeFromParent(tickers, filter);
            filter.removeFromChilds(tickers);
        }
        for (T ticker : tickers) {
            this.tickerWatchers.remove(ticker);
        }
    }

    @Override
    public String clearFilter() {
        removeTickersFromFilter(new HashSet<>(this.tickerWatchers.keySet()));
        return null;
    }

    @Override
    public Set<T> getTickersInFilter() {
        return Collections.unmodifiableSet(this.tickerWatchers.keySet());
    }

    public Set<DefaultFilter<T>> getWatchers(T ticker) {
        return tickerWatchers.get(ticker);
    }

    @Override
    public void finalize() {
        removeTickersFromFilter(getTickersInFilter());

        if (parent != null) {
            parent.removePropertyChangeListener(this);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("Quotes")) {
            NolRecentInfoAPI info = (NolRecentInfoAPI) evt.getNewValue();
            if (tickerWatchers.keySet().contains(info.getTicker())) {
                pcs.firePropertyChange("Quotes", evt.getOldValue(), evt.getNewValue());
            }
        }
    }
}
