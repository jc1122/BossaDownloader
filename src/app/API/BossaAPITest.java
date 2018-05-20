package app.API;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

//TODO test nolorderreport and nolorderrequest

class BossaAPITest {

    @BeforeEach
    void setUp() {
        assumeTrue(BossaAPIInterfaceTest.checkIfNolIsRunning(), "NOL is not running");
        System.out.println("init message: " + BossaAPI.initialize());
        //Thread.sleep(100);
    }

    @AfterAll
    static void tearDown() {
        BossaAPI.shutdown();
    }

    @Test
    void initialize() {
        BossaAPI.initialize();
    }

    @Nested
    @DisplayName("addTickersToFilter tests")
    class AddTickersToFilterTests {

        private Set<BossaAPI.NolTickerAPI> addNumberOfTickers(int number) {
            List<BossaAPI.NolTickerAPI> tickers = BossaAPI.getTickers(TypeOfList.ALL, null);
            Set<BossaAPI.NolTickerAPI> tickerSet = new HashSet<>();

            IntStream.range(0, number).forEach(value -> tickerSet.add(tickers.get(value)));

            return tickerSet;
        }

        @Test
        @DisplayName("add 10 valid tickerSelector to filter")
        void addTickersToFilterTest10() {
            String message = BossaAPI.addTickersToFilter(addNumberOfTickers(10));
            assertEquals("add to filter", message);
        }

        @Test
        @DisplayName("add 200 valid tickerSelector to filter (limit is 150)")
        void addTickersToFilterTest200() {
            Executable test = () -> BossaAPI.addTickersToFilter(addNumberOfTickers(200));
            assertThrows(IllegalArgumentException.class, test, "should complain about too much tickerSelector in filter!");
        }

        @Test
        @DisplayName("add single null ticker to filter")
        void addTickersToFilterNull() {
            Set<BossaAPI.NolTickerAPI> tickers = new HashSet<>();
            tickers.add(null);
            Executable test = () -> BossaAPI.addTickersToFilter(tickers);
            assertThrows(IllegalArgumentException.class, test);
            //BossaAPI.addTickersToFilter(tickerSelector);
        }

        @Test
        @DisplayName("add single null ticker with 10 valid tickerSelector to filter")
        void addTickersToFilterValidNull() {
            Set<BossaAPI.NolTickerAPI> tickers = addNumberOfTickers(10);
            tickers.add(null);
            String message = BossaAPI.addTickersToFilter(tickers);
            assertEquals("add to filter", message);
        }

        @Test
        @DisplayName("add invalid ticker to filter")
        void addTickersToFilterInvalid() {
            Set<BossaAPI.NolTickerAPI> tickers = addNumberOfTickers(10);
            tickers.add(null);
            String message = BossaAPI.addTickersToFilter(tickers);
            assertEquals("add to filter", message);
        }
    }

    @Test
    @DisplayName("successfully add single isin to filter")
    void addToFilterSingleIsin() {
        Set<String> isins = new HashSet<>();
        isins.add("PL4FNMD00013");

        assertEquals("add to filter", BossaAPI.addToFilter(isins), BossaAPI.addToFilter(isins));
    }

    @Test
    @DisplayName("successfully add multiple isins to filter")
    void addToFilterMultipleIsins() {
        Set<String> isins = new HashSet<>();
        isins.add("PLVCAOC00015");
        isins.add("PLNFI0600010");
        isins.add("PLNFI0800016");
        isins.add("PL11BTS00015");

        assertEquals("add to filter", BossaAPI.addToFilter(isins), BossaAPI.addToFilter(isins));
    }

    private Set<String> prepareIsins(int numberOfIsins) {
        List<BossaAPI.NolTickerAPI> tickers = BossaAPI.getTickers(TypeOfList.ALL, null);
        assumeTrue(tickers.size() > numberOfIsins, "ticker count is below 101!");
        Set<String> isins = new HashSet<>();
        for (int i = 0; i < numberOfIsins; i++) {
            isins.add(tickers.get(i).getIsin());
        }
        return isins;
    }

    @Test
    @DisplayName("exceed limit (150) of valid tickerSelector to filter")
    void addToFilterTooManyTickers() {
        BossaAPI.clearFilter();
        Executable test = () -> BossaAPI.addToFilter(prepareIsins(300));

        assertThrows(IllegalArgumentException.class, test, "should complain about too much tickerSelector in filter!");

    }

    @Test
    @DisplayName("check quotes callback for 150 tickerSelector in filter")
    void callbackTest() throws InterruptedException {
        BossaAPI.clearFilter();
        int numberOfTickers = 150;
        BossaAPI.addToFilter(prepareIsins(numberOfTickers));
        int[] counter = {0};
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (Objects.equals(evt.getPropertyName(), "Quotes")) {
                    counter[0]++;
                    synchronized (this) {
                        if (counter[0] == 2 * numberOfTickers) {
                            this.notifyAll();
                        }
                    }
                }
            }
        };
        BossaAPI.Quotes.getInstance().addPropertyChangeListener(listener);
        synchronized (listener) {
            listener.wait(6000);
        }
        assertTrue(counter[0] > 2 * numberOfTickers - 5,
                "callback count is "
                        + Integer.toString(counter[0])
                        + " should be at least"
                        + Integer.toString(2 * numberOfTickers - 5)
                        + ", increase timeout or check code!");
    }

    @Test
    @DisplayName("add invalid multiple isins to filter")
    void addToFilterInvalidISIN() {
        Set<String> isins = new HashSet<>();
        isins.add("PLVCAOC00015");
        isins.add("PLNFI0600010");
        isins.add("PLNFI0800016");
        isins.add("invalid4");

        Executable test = () -> BossaAPI.addToFilter(isins);
        assertThrows(IllegalArgumentException.class, test, "error with attribute action or ticker");
    }

    @Test
    @DisplayName("add empty isin set to filter")
    void addToFilterEmptyIsin() {
        Set<String> isins = new HashSet<>();

        Executable test = () -> BossaAPI.addToFilter(isins);
        assertThrows(IllegalArgumentException.class, test, "error with attribute action or ticker");
    }

    @Test
    @Disabled
    void APIOrderRequest() {
        fail("");
    }


    @Test
    @DisplayName("get api version")
    void get_Version() {
        assertNotNull(BossaAPI.getVersion());
    }

    @Test
    @DisplayName("successfully clear filter")
    void clearFilter() {
        assertEquals("clear filter", BossaAPI.clearFilter());
    }

    @Test
    @DisplayName("successfully shutdown API")
    void shutdown() {
        BossaAPI.shutdown();
    }

    @Test
    @DisplayName("get properties after initialization")
        //watch out, properties are initialized only after callback has returned their values!
    void getPropertyMap() {
        Map<String, BossaAPI.PropertyAPI> propertyMap = BossaAPI.getPropertyMap();
        for (String propertyName : propertyMap.keySet()) {
            try {
                Object property = propertyMap.get(propertyName).getProperty();
                assertNotNull(property, propertyName);
                System.out.println(property.getClass().toString() + " : " + property);
            } catch (NullPointerException e) {
                assertEquals(e.getClass(), NullPointerException.class);
                System.out.println(e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("removeFromFilter tests")
    class RemoveFromFilterTests {
        Set<String> isinsToRemove;

        @BeforeEach
        void prepareFilter() {
            Set<String> isins = new HashSet<>();
            isins.add("PLVCAOC00015");
            isins.add("PLNFI0600010");
            isins.add("PLNFI0800016");
            isins.add("PL11BTS00015");
            BossaAPI.addToFilter(isins);
            isinsToRemove = new HashSet<>();
        }

        @Test
        @DisplayName("remove invalid isin from filter or isin not contained in filter")
        void removeFromFilterInvalid() {
            isinsToRemove.add("invalid");
            Executable test = () -> BossaAPI.removeFromFilter(isinsToRemove);
            assertThrows(IllegalArgumentException.class, test, "nothing to add/remove");
        }

        @Test
        @DisplayName("remove valid existing isin from filter ")
        void removeFromFilterValid() {
            isinsToRemove.add("PLNFI0800016");
            isinsToRemove.add("PL11BTS00015");
            assertEquals("remove from filter", BossaAPI.removeFromFilter(isinsToRemove));
        }

        @Test
        @DisplayName("remove invalid isin and valid existing isin from filter ")
        void removeFromFilterValidInvalid() {
            isinsToRemove.add("PLNFI0800016");
            isinsToRemove.add("PL11BTS00015");
            isinsToRemove.add("invalid");
            Executable test = () -> BossaAPI.removeFromFilter(isinsToRemove);
            assertThrows(IllegalArgumentException.class, test);
        }

        @Test
        @DisplayName("remove null isin from filter ")
        void removeFromFilterNull() {
            isinsToRemove.add(null);
            Executable test = () -> BossaAPI.removeFromFilter(isinsToRemove);
            assertThrows(IllegalArgumentException.class, test);
        }

        @Test
        @DisplayName("remove empty isin from filter ")
        void removeFromFilterEmpty() {
            isinsToRemove.add("");
            Executable test = () -> BossaAPI.removeFromFilter(isinsToRemove);
            assertThrows(IllegalArgumentException.class, test);
        }

        @Test
        @DisplayName("remove empty isin and valid existing isin from filter ")
        void removeFromFilterValidEmpty() {
            isinsToRemove.add("PLNFI0800016");
            isinsToRemove.add("PL11BTS00015");
            isinsToRemove.add("");
            Executable test = () -> BossaAPI.removeFromFilter(isinsToRemove);
            assertThrows(IllegalArgumentException.class, test);
        }
    }

    @Nested
    @DisplayName("getTickers test")
    class GetTickersTest {
        @Test
        @DisplayName("get list of tickerSelector with wrong type of list for in_ticker")
        void getTickersWrongParameter() {
            Executable test = () -> BossaAPI.getTickers(TypeOfList.ISIN, null);
            assertThrows(IllegalArgumentException.class, test);
        }

        @Test
        @DisplayName("get list of SYMBOL tickerSelector with single ticker symbol")
        void getTickersSymbol() {
            List<BossaAPI.NolTickerAPI> tickers = prepareAllTickers(TypeOfList.SYMBOL);
            System.out.println(tickers);
            assertEquals(1, tickers.size());
        }

        @Test
        @DisplayName("get list of ALL tickerSelector with single ticker symbol")
        void getTickersAll() {
            List<BossaAPI.NolTickerAPI> tickers = prepareAllTickers(TypeOfList.ALL);
        }

        @Test
        @DisplayName("get default list of tickerSelector")
        void getTickers() {
            List<BossaAPI.NolTickerAPI> tickers = BossaAPI.getTickers(TypeOfList.ALL, null);
            System.out.println("number of tickerSelector: " + tickers.size());
            assertTrue(tickers.size() > 1000);
        }

        private List<BossaAPI.NolTickerAPI> prepareAllTickers(TypeOfList typeOfList) {
            List<BossaAPI.NolTickerAPI> tickers = BossaAPI.getTickers(TypeOfList.ALL, null);
            assumeFalse(tickers.isEmpty());
            //List<BossaAPI.NolTickerAPI> singleTicker = BossaAPI.getTickers(typeOfList, tickerSelector.get(0));
            List<BossaAPI.NolTickerAPI> singleTicker = BossaAPI.getTickers(typeOfList, tickers.get(0));
            System.out.println("number of tickerSelector: " + tickers.size());
            System.out.println("Symbol: " + tickers.get(0).getName());
            System.out.println("number of filtered tickerSelector: " + singleTicker.size());
            return singleTicker;
        }
    }
}