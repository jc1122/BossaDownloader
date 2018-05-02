package app.API;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class BossaAPITest {

    @BeforeEach
    void setUp() {
        assumeTrue(BossaAPIInterfaceTest.checkIfNolIsRunning(), "NOL is not running");
        BossaAPI.initialize();
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

    Set<String> prepareIsins(int numberOfIsins) {
        List<BossaAPI.NolTickerAPI> tickers = BossaAPI.getTickers(TypeOfList.ALL, null);
        assumeTrue(tickers.size() > numberOfIsins, "ticker count is below 101!");
        Set<String> isins = new HashSet<>();
        for (int i = 0; i < numberOfIsins; i++) {
            isins.add(tickers.get(i).getIsin());
        }
        return isins;
    }

    @Test
    @DisplayName("exceed limit (150) of valid tickers to filter")
    void addToFilterTooManyTickers() {
        BossaAPI.clearFilter();
        Executable test = () -> BossaAPI.addToFilter(prepareIsins(300));

        assertThrows(IllegalStateException.class, test, "should complain about too much tickers in filter!");

    }

    @Test
    @DisplayName("check quotes callback for 150 tickers in filter")
    void callbackTest() throws InterruptedException {
        BossaAPI.clearFilter();
        int numberOfTickers = 150;
        BossaAPI.addToFilter(prepareIsins(numberOfTickers));
        int[] counter = {0};
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName() == "Quotes") {
                    counter[0]++;
                    BossaAPI.NolRecentInfoAPI quote = BossaAPI.NolRecentInfoAPI.class.cast(evt.getNewValue());
//                    System.out.println(quote.getTicker().getIsin());
//                    System.out.println(Integer.toBinaryString(quote.getBitMask()));
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
            listener.wait(2000);
        }
        assertTrue(counter[0] > 2 * numberOfTickers - 5,
                "callback count too small, increase timeout or check code!");
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
        assertThrows(IllegalStateException.class, test, "error with attribute action or ticker");
    }

    @Test
    @DisplayName("add empty isin set to filter")
    void addToFilterEmptyIsin() {
        Set<String> isins = new HashSet<>();

        Executable test = () -> BossaAPI.addToFilter(isins);
        assertThrows(IllegalStateException.class, test, "error with attribute action or ticker");
    }
    @Test
    @DisplayName("remove invalid isin from filter or isin not contained in filter")
    void removeFromFilterInvalid() {
        Set<String> isins = new HashSet<>();
        isins.add("PLVCAOC00015");
        isins.add("PLNFI0600010");
        isins.add("PLNFI0800016");
        isins.add("PL11BTS00015");
        BossaAPI.addToFilter(isins);

        Set<String> isinsToRemove = new HashSet<>();
        isinsToRemove.add("invalid");

        Executable test = () -> BossaAPI.removeFromFilter(isinsToRemove);
        assertThrows(IllegalArgumentException.class, test, "nothing to add/remove");
        //fail("");
    }

    @Test
    @DisplayName("remove valid existing isin from filter ")
    void removeFromFilterValid() {
        Set<String> isins = new HashSet<>();
        isins.add("PLVCAOC00015");
        isins.add("PLNFI0600010");
        isins.add("PLNFI0800016");
        isins.add("PL11BTS00015");
        BossaAPI.addToFilter(isins);

        Set<String> isinsToRemove = new HashSet<>();
        isinsToRemove.add("PLNFI0800016");
        isinsToRemove.add("PL11BTS00015");

        assertEquals("remove from filter", BossaAPI.removeFromFilter(isinsToRemove));
    }
    @Test
    @Disabled
    void APIOrderRequest() {
        fail("");
    }

    @Test
    @DisplayName("get default list of tickers")
    void getTickers() {
        List<BossaAPI.NolTickerAPI> tickers = BossaAPI.getTickers(TypeOfList.ALL, null);
        System.out.println("number of tickers: " + tickers.size());
        //fail("");
    }

    @Test
    @DisplayName("get list of tickers with wrong type of list for in_ticker")
    void getTickersWrongParameter() {
        Executable test = () -> BossaAPI.getTickers(TypeOfList.ISIN, null);
        assertThrows(IllegalArgumentException.class, test);
    }

    private List<BossaAPI.NolTickerAPI> prepareAllTickers(TypeOfList typeOfList) {
        List<BossaAPI.NolTickerAPI> tickers = BossaAPI.getTickers(TypeOfList.ALL, null);
        assumeFalse(tickers.isEmpty());
        List<BossaAPI.NolTickerAPI> singleTicker = BossaAPI.getTickers(TypeOfList.SYMBOL, tickers.get(0));
        System.out.println("number of tickers: " + tickers.size());
        System.out.println("Symbol: " + tickers.get(0).getName());
        System.out.println("number of filtered tickers: " + singleTicker.size());
        return singleTicker;
    }

    @Test
    @DisplayName("get list of SYMBOL tickers with single ticker symbol")
    void getTickersSymbol() {
        List<BossaAPI.NolTickerAPI> tickers = prepareAllTickers(TypeOfList.SYMBOL);
        assertTrue(tickers.size() == 1);
    }

    @Test
    @DisplayName("get list of ALL tickers with single ticker symbol")
    void getTickersAll() {
        List<BossaAPI.NolTickerAPI> tickers = prepareAllTickers(TypeOfList.ALL);
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
}