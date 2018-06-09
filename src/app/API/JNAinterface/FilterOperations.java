package app.API.JNAinterface;

import java.util.Set;

public interface FilterOperations {
    String addToFilter(Set<String> isins) throws IllegalArgumentException;

    String addTickersToFilter(Set<NolTickerAPI> tickers);

    @SuppressWarnings("SameReturnValue")
    String removeFromFilter(Set<String> isins) throws IllegalStateException;

    //clear filter before adding new papers
    @SuppressWarnings("UnusedReturnValue")
    String clearFilter();

    Set<NolTickerAPI> getTickersInFilter();
}
