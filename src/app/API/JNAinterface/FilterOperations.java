package app.API.JNAinterface;

import java.util.Set;

public interface FilterOperations {
    //@Deprecated
    //String addToFilter(Set<String> isins) throws IllegalArgumentException;

    String addTickersToFilter(Set<NolTickerAPI> tickers);

    @SuppressWarnings("SameReturnValue")
    String removeTickersFromFilter(Set<NolTickerAPI> isins) throws IllegalStateException;

    //clear filter before adding new papers
    @SuppressWarnings("UnusedReturnValue")
    String clearFilter();

    Set<NolTickerAPI> getTickersInFilter();
}
