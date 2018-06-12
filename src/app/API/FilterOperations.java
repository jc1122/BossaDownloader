package app.API;

import app.API.JNAinterface.NolTickerAPI;

import java.util.Set;

public interface FilterOperations<T> {
    //@Deprecated
    //String addToFilter(Set<String> isins) throws IllegalArgumentException;

    String addTickersToFilter(Set<T> tickers);

    @SuppressWarnings("SameReturnValue")
    String removeTickersFromFilter(Set<T> tickers) throws IllegalStateException;

    //clear filter before adding new papers
    @SuppressWarnings("UnusedReturnValue")
    String clearFilter();

    Set<T> getTickersInFilter();
}
