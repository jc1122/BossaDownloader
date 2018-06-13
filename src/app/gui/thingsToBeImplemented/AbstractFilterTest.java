package app.gui.thingsToBeImplemented;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AbstractFilterTest {

    AbstractFilter<String> test;

    AbstractFilter<String> test1;
    AbstractFilter<String> test2;

    AbstractFilter<String> test11;
    AbstractFilter<String> test12;
    AbstractFilter<String> test21;

    AbstractFilter<String> test111;
    AbstractFilter<String> test112;

    @BeforeEach
    void prepare() {
        //build a tree of filters:
        //                    test
        //                  /        \
        //              test1           test2
        //             /    \                 \
        //          test11   test12          test21
        //          /     \
        //      test111   test112

        test = new AbstractFilter<String>(null) {
        };

        test1 = new AbstractFilter<String>(test) {
        };
        test2 = new AbstractFilter<String>(test) {
        };

        test11 = new AbstractFilter<String>(test1) {
        };
        test12 = new AbstractFilter<String>(test1) {
        };
        test21 = new AbstractFilter<String>(test2) {
        };

        test111 = new AbstractFilter<String>(test11) {
        };
        test112 = new AbstractFilter<String>(test11) {
        };
    }


    @Test
    void getParent() {
        assertNull(test.getParent());

        assertEquals(test, test1.getParent());
        assertEquals(test, test2.getParent());

        assertEquals(test1, test11.getParent());
        assertEquals(test1, test12.getParent());
        assertEquals(test2, test21.getParent());

        assertEquals(test11, test111.getParent());
        assertEquals(test11, test112.getParent());
    }

    @Test
    @DisplayName("add tickers to master")
    void addTickersToMaster() {
        Set<String> tickers = new HashSet<>();
        tickers.add("ticker1");
        tickers.add("ticker2");
        test.addTickersToFilter(tickers);

        assertEquals(tickers, test.getTickersInFilter());

        assertEquals(Collections.emptySet(), test1.getTickersInFilter());
        assertEquals(Collections.emptySet(), test2.getTickersInFilter());

        assertEquals(Collections.emptySet(), test11.getTickersInFilter());
        assertEquals(Collections.emptySet(), test12.getTickersInFilter());
        assertEquals(Collections.emptySet(), test21.getTickersInFilter());

        assertEquals(Collections.emptySet(), test111.getTickersInFilter());
        assertEquals(Collections.emptySet(), test112.getTickersInFilter());

        assertEquals(new HashSet<>(Arrays.asList(test)), test.getWatchers("ticker1"));
        assertNull(test1.getWatchers("ticker1"));
        assertNull(test2.getWatchers("ticker1"));

        assertNull(test11.getWatchers("ticker1"));
        assertNull(test12.getWatchers("ticker1"));
        assertNull(test21.getWatchers("ticker1"));

        assertNull(test111.getWatchers("ticker1"));
        assertNull(test112.getWatchers("ticker1"));

        assertEquals(new HashSet<>(Arrays.asList(test)), test.getWatchers("ticker2"));
        assertNull(test1.getWatchers("ticker2"));
        assertNull(test2.getWatchers("ticker2"));

        assertNull(test11.getWatchers("ticker2"));
        assertNull(test12.getWatchers("ticker2"));
        assertNull(test21.getWatchers("ticker2"));

        assertNull(test111.getWatchers("ticker2"));
        assertNull(test112.getWatchers("ticker2"));
    }

    @Test
    @DisplayName("add tickers to child 12")
    void addTickersToChild112() {
        Set<String> tickers = new HashSet<>();
        tickers.add("ticker1");
        test112.addTickersToFilter(tickers);

        assertEquals(tickers, test.getTickersInFilter());

        assertEquals(tickers, test1.getTickersInFilter());
        assertEquals(Collections.emptySet(), test2.getTickersInFilter());

        assertEquals(tickers, test11.getTickersInFilter());
        assertEquals(Collections.emptySet(), test12.getTickersInFilter());
        assertEquals(Collections.emptySet(), test21.getTickersInFilter());

        assertEquals(Collections.emptySet(), test111.getTickersInFilter());
        assertEquals(tickers, test112.getTickersInFilter());

        assertEquals(new HashSet<>(Arrays.asList(test112)), test.getWatchers("ticker1"));

        assertEquals(new HashSet<>(Arrays.asList(test112)), test1.getWatchers("ticker1"));
        assertNull(test2.getWatchers("ticker1"));

        assertEquals(new HashSet<>(Arrays.asList(test112)), test11.getWatchers("ticker1"));
        assertNull(test12.getWatchers("ticker1"));
        assertNull(test21.getWatchers("ticker1"));

        assertNull(test111.getWatchers("ticker1"));
        assertEquals(new HashSet<>(Arrays.asList(test112)), test112.getWatchers("ticker1"));
    }

    @Test
    @DisplayName("remove tickers from master")
    void removeTickersFromFilter() {
        addTickersToMaster();
        Set<String> tickers = new HashSet<>(test.getTickersInFilter());
        test.removeTickersFromFilter(tickers);
        assertEquals(Collections.emptySet(), test.getTickersInFilter());
    }

    @Test
    @DisplayName("remove tickers from childs")
    void removeTickersFromChilds() {
        addTickersToChild112();
        test21.addTickersToFilter(Collections.singleton("ticker1"));
        Set<String> tickers = new HashSet<>(test.getTickersInFilter());
        test.removeTickersFromFilter(tickers);

        assertEquals(Collections.emptySet(), test.getTickersInFilter());

        assertEquals(Collections.emptySet(), test1.getTickersInFilter());
        assertEquals(Collections.emptySet(), test2.getTickersInFilter());

        assertEquals(Collections.emptySet(), test11.getTickersInFilter());
        assertEquals(Collections.emptySet(), test12.getTickersInFilter());
        assertEquals(Collections.emptySet(), test21.getTickersInFilter());

        assertEquals(Collections.emptySet(), test111.getTickersInFilter());
        assertEquals(Collections.emptySet(), test112.getTickersInFilter());
    }
}