package app.API.PublicAPI;

import app.API.JNAinterface.NolRecentInfoAPI;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

/**
 * Adds listener to quotes. Manages watched tickers. Generic was only used for testing. Remove generics in the future.
 *
 * @param <T>
 */
public class BaseFilter<T extends Ticker> implements FilterOperations<T>, PropertyChangeListener {
    /**
     * parent of this component, valid only for non-master nodes, top node should be a different implementation of
     * this class than the children
     */
    private final BaseFilter<T> parent;
    private final Set<BaseFilter<T>> childs;
    private final PropertyChangeSupport pcs;
    /**
     * map tickers in filter to the composites watching it
     */
    private final HashMap<T, Set<BaseFilter<T>>> tickerWatchers;

    protected BaseFilter() {
        this(null);
    }

    protected BaseFilter(BaseFilter<T> parent) {
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

    protected void addChild(BaseFilter<T> filter) {
        this.childs.add(filter);
    }

    private BaseFilter getParent() {
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
        Set<T> thisTickersOld = getTs();

        addToWatchers(tickers, this);

        Set<T> thisTickersNew = getTs();

        pcs.firePropertyChange("Filter", thisTickersOld, thisTickersNew);
        return null;
    }

    @NotNull
    private Set<T> getTs() {
        Set<T> thisTickers = new HashSet<>();
        for (Map.Entry<T, Set<BaseFilter<T>>> entry : tickerWatchers.entrySet()) {
            if (Set.class.cast(entry.getValue()).contains(this)) {
                thisTickers.add(entry.getKey());
            }
        }
        return thisTickers;
    }

    /**
     * For given set of tickers, add a new watcher of each of them. Propagate through all parents.
     *
     * @param tickers
     * @param watcher
     */
    protected void addToWatchers(Set<T> tickers, BaseFilter<T> watcher) {
        for (T ticker : tickers) {
            if (tickerWatchers.keySet().contains(ticker)) {
                tickerWatchers.get(ticker).add(watcher);
            } else {
                Set<BaseFilter<T>> watchers = new HashSet<>();
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
        Set<T> oldValue = getTs();
        removeFromParent(tickers, this);
        removeFromChilds(tickers);
        Set<T> newValue = getTs();
        pcs.firePropertyChange("Filter", oldValue, newValue);
        return null;
    }

    protected void removeFromParent(Set<T> tickers, BaseFilter<T> filter) {
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
        for (BaseFilter<T> filter : childs) {
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

    protected Set<BaseFilter<T>> getWatchers(T ticker) {
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

    protected void firePropertyChange(PropertyChangeEvent evt) {
        pcs.firePropertyChange(evt);
    }
}
