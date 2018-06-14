package app.gui.thingsToBeImplemented;

import app.API.FilterOperations;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractFilter<T> implements FilterOperations<T> {
    /**
     * parent of this component, valid only for non-master nodes, top node should be a different implementation of
     * this class than the children
     */
    private final AbstractFilter<T> parent;

    private final Set<AbstractFilter<T>> childs;
    /**
     * map tickers in filter to the composites watching it
     */
    private final HashMap<T, Set<AbstractFilter<T>>> tickerWatchers;

    AbstractFilter(AbstractFilter<T> parent) {
        this.parent = parent;
        tickerWatchers = new HashMap<>();
        childs = new HashSet<>();
        if(parent != null) {
            parent.addChild(this);
        }
    }

    public void addChild(AbstractFilter<T> filter) {
        this.childs.add(filter);
    }
    public AbstractFilter getParent() {
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
    private void addToWatchers(Set<T> tickers, AbstractFilter<T> watcher) {
        for (T ticker : tickers) {
            if (tickerWatchers.keySet().contains(ticker)) {
                tickerWatchers.get(ticker).add(watcher);
            } else {
                Set<AbstractFilter<T>> watchers = new HashSet<>();
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

    private void removeFromParent(Set<T> tickers, AbstractFilter<T> filter) {
        for(T ticker : tickers) {
            if(this.tickerWatchers.get(ticker) != null) {
                this.tickerWatchers.get(ticker).remove(filter);
            }
        }
        if(parent != null) {
            parent.removeFromParent(tickers, filter);
        }
    }

    private void removeFromChilds(Set<T> tickers) {
        for(AbstractFilter<T> filter : childs) {
            filter.removeFromParent(tickers, filter);
            filter.removeFromChilds(tickers);
        }
        for(T ticker : tickers) {
            this.tickerWatchers.remove(ticker);
        }
    }
    @Override
    public String clearFilter() {
        removeTickersFromFilter(this.tickerWatchers.keySet());
        return null;
    }

    @Override
    public Set<T> getTickersInFilter() {
        return Collections.unmodifiableSet(this.tickerWatchers.keySet());
    }

    public Set<AbstractFilter<T>> getWatchers(T ticker) {
        return tickerWatchers.get(ticker);
    }

    @Override
    public void finalize() {
        removeTickersFromFilter(getTickersInFilter());
    }
}
