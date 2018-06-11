package app.gui.thingsToBeImplemented;

import app.API.FilterOperations;
import app.API.JNAinterface.NolTickerAPI;
import app.API.JNAinterface.NolTickersAPI;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

//TODO implement this class, this should be fun as it is recurrence
public abstract class AbstractFilter implements FilterOperations {
    /**
     * map tickers in filter to the composites watching it
     */
    HashMap<NolTickerAPI, Set<AbstractFilter>> tickerWatchers;
    /**
     * children of this composite
     */
    HashSet<AbstractFilter> composites;
    /**
     * parent of this component, valid only for non-master nodes, top node should be a different implementation of
     * this class than the children
     */
    AbstractFilter parent;
    /**
     * tickers currently in filter, this should be indentical to keyset of tickerWatchers
     */
    HashSet<NolTickersAPI> tickers;

    /**
     * Add tickers to this filter and all of its parent filters
     *
     * @param tickers
     * @return
     */
    @Override
    public String addTickersToFilter(Set<NolTickerAPI> tickers) {
        addToWatchers(tickers, this);
        parent.addToWatchers(tickers, this);
        return null;
    }

    /**
     * For given set of tickers, add a new watcher of each of them.
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
        Set<NolTickerAPI> intersection = new HashSet<>();
        intersection.addAll(tickerWatchers.keySet());
        intersection.retainAll(tickers);

        //now remove tickers from each watcher child
        for (NolTickerAPI ticker : intersection) {
            tickerWatchers.get(ticker).forEach(e -> removeTickersFromFilter(tickers));
        }
        removeFromParentFilters(tickers, this);
        return null;
    }

    protected void removeFromParentFilters(Set<NolTickerAPI> tickers, AbstractFilter child) {
        Set<NolTickerAPI> intersection = new HashSet<>();
        intersection.addAll(tickerWatchers.keySet());
        intersection.retainAll(tickers);

        //now remove tickers from each watcher child
        for (NolTickerAPI ticker : intersection) {
            tickerWatchers.get(ticker).remove(child);
        }
        parent.removeFromParentFilters(tickers, child);
    }

    @Override
    public String clearFilter() {
        return null;
    }

    @Override
    public Set<NolTickerAPI> getTickersInFilter() {
        return null;
    }
}
