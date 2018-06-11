package app.gui.thingsToBeImplemented;

import app.API.FilterOperations;
import app.API.JNAinterface.NolTickerAPI;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

//TODO implement this class, this should be fun as it is recurrence
public abstract class AbstractFilter implements FilterOperations {
    /**
     * map tickers in filter to the composites watching it
     */
    private final HashMap<NolTickerAPI, Set<AbstractFilter>> tickerWatchers;

    public AbstractFilter getParent() {
        return parent;
    }

    protected AbstractFilter(AbstractFilter parent) {
        this.parent = parent;
        tickerWatchers = new HashMap<>();
    }

    /**
     * parent of this component, valid only for non-master nodes, top node should be a different implementation of
     * this class than the children
     */
    protected final AbstractFilter parent;

    /**
     * Add tickers to this filter and all of its parent filters
     *
     * @param tickers
     * @return
     */
    @Override
    public String addTickersToFilter(Set<NolTickerAPI> tickers) {
        addToWatchers(tickers, this);
        return null;
    }

    /**
     * For given set of tickers, add a new watcher of each of them. Propagate through all parents.
     *
     * @param tickers
     * @param watcher
     */
    private void addToWatchers(Set<NolTickerAPI> tickers, AbstractFilter watcher) {
        for (NolTickerAPI ticker : tickers) {
            if (tickerWatchers.keySet().contains(ticker)) {
                tickerWatchers.get(ticker).add(watcher);
            } else {
                Set<AbstractFilter> watchers = new HashSet<>();
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
    public String removeTickersFromFilter(Set<NolTickerAPI> tickers) throws IllegalStateException {
        //need to copy the keys, or they will get lost during intersection
        Set<NolTickerAPI> intersection = new HashSet<>(tickerWatchers.keySet());
        intersection.retainAll(tickers);

        //now remove tickers from each watcher child;
        for (NolTickerAPI ticker : intersection) {
            tickerWatchers.get(ticker).remove(this); //without this will be infinite loop
            tickerWatchers.get(ticker).forEach(e -> removeTickersFromFilter(tickers));
        }
        removeFromParentFilters(tickers, this);
        return null;
    }

    protected void removeFromParentFilters(Set<NolTickerAPI> tickers, AbstractFilter child) {
        Set<NolTickerAPI> intersection = new HashSet<>(tickerWatchers.keySet());
        intersection.retainAll(tickers);

        //now remove tickers from each watcher child
        for (NolTickerAPI ticker : intersection) {
            tickerWatchers.get(ticker).remove(child);
        }
        parent.removeFromParentFilters(tickers, child);
    }

    @Override
    public String clearFilter() {
        removeTickersFromFilter(this.tickerWatchers.keySet());
        return null;
    }

    @Override
    public Set<NolTickerAPI> getTickersInFilter() {
        return null;
    }
}
