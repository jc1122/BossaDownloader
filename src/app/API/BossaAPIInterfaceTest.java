package app.API;

import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DisplayName("BossaAPIIntergace tests")
class BossaAPIInterfaceTest {
    private static BossaAPIInterface INSTANCE;
    private static int code; //error code for each test

    private static void assertNonNegative(int code) {
        assertTrue(code >= 0, getMessage(code));
    }

    private static void assertNegative(int code) {
        assertFalse(code == -22, getMessage(code));
        assertFalse(code == -55, getMessage(code));
        assertTrue(code < 0, getMessage(code));
    }

    static boolean checkIfNolIsRunning() {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        System.out.println("Checking if NOL3 is running.");
        try {
            Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));


            while ((line = input.readLine()) != null) {
                //System.out.println(line);
                pidInfo.append(line);
            }
            input.close();

        } catch (Exception e) {
            System.out.println("IO exception " + e);
        }
        return pidInfo.toString().contains("NOL3.exe");
    }

    private static String getMessage(int code) {
        return INSTANCE.GetResultCodeDesc(code);
    }

    @BeforeAll
    //api will crash if initialization and shutdown is performed for each test
    static void setUp() {
        assumeTrue(checkIfNolIsRunning(), () -> "NOL is not running");
        INSTANCE = BossaAPI.INSTANCE;
        System.out.println("Init message: " + getMessage(INSTANCE.Initialize("BOS;BOS")));

    }

    @AfterAll
    static void tearDown() {
        System.out.println("Post test message: " + getMessage(code));
        System.out.println("Shutdown message: " + getMessage(INSTANCE.Shutdown()));
//                try {
//            Thread.sleep(100); //give some time to clean up, otherwise api may crash
//        } catch(InterruptedException e) {}
    }

    @Nested
    @DisplayName("Tests of AddToFilter function")
    class AddToFilterTests {

        @Test
        @DisplayName("add valid isin to empty filter")
        void addToFilterValid() {
            code = INSTANCE.AddToFilter("PLDEBCA00016", false);
            INSTANCE.ClearFilter();
            assertNonNegative(code);

        }

        @Test
        @DisplayName("add 2 valid isins to empty filter")
        void addToFilterMany() {
            code = INSTANCE.AddToFilter("PLDEBCA00016;PLDMLKR00023", false);
            INSTANCE.ClearFilter();
            assertNonNegative(code);

        }

        @Test
        @DisplayName("add valid isin to nonempty filter")
        void addToFilterNonEmpty() {
            INSTANCE.AddToFilter("PLDEBCA00016", false);
            code = INSTANCE.AddToFilter("PLDMLKR00023", false);
            INSTANCE.ClearFilter();
            assertNegative(code);

        }

        @Test
        @DisplayName("add invalid isin to empty filter")
        void addToFilterInvalid() {
            code = INSTANCE.AddToFilter("PLDMLKR00023invalid", false);
            INSTANCE.ClearFilter();
            assertNegative(code);

        }

        @Test
        @DisplayName("add invalid isin to nonempty filter")
        void addToFilterInvalidNonEmpty() {
            INSTANCE.AddToFilter("PLDEBCA00016", false);
            code = INSTANCE.AddToFilter("PLDMLKR00023invalid", false);
            INSTANCE.ClearFilter();
            assertNegative(code);

        }

        @Test
        @DisplayName("add null isin to nonempty filter")
        void addToFilterNullNonempty() {
            INSTANCE.AddToFilter("PLDEBCA00016", false);
            code = INSTANCE.AddToFilter(null, false);
            INSTANCE.ClearFilter();
            assertNegative(code);

        }

        @Test
        @DisplayName("add empty isin to nonempty filter")
        void addToFilterEmptyNonempty() {
            INSTANCE.AddToFilter("PLDEBCA00016", false);
            code = INSTANCE.AddToFilter("", false);
            INSTANCE.ClearFilter();
            assertNegative(code);

        }
    }


    @Nested
    @DisplayName("Tests of RemFromFilter function")
    class RemFromFilterTests {
        @Test
        @DisplayName("remove existing tickerSelector from filter and add valid isin")
        void remFromFilterValid() {
            INSTANCE.AddToFilter("PLDEBCA00016;", false);
            code = INSTANCE.RemFromFilter("PLDMLKR00023", false);
            INSTANCE.ClearFilter();
            assertNonNegative(code);

        }

        @Test
        @DisplayName("remove existing tickerSelector from filter and add invalid isin")
        void remFromFilterInalid() {
            INSTANCE.AddToFilter("PLDEBCA00016;", false);
            code = INSTANCE.RemFromFilter("PLDMLKR00023invalid", false);
            INSTANCE.ClearFilter();
            assertEquals(-34, code);

        }

        @Test
        @DisplayName("remove existing tickerSelector from filter and add empty isin")
        void remFromFilterEmpty() {
            INSTANCE.AddToFilter("PLDEBCA00016;", false);
            code = INSTANCE.RemFromFilter("", false);
            INSTANCE.ClearFilter();
            assertEquals(-56, code);

        }

        @Test
        @DisplayName("remove existing tickerSelector from filter and add null isin")
        void remFromFilterNull() {
            INSTANCE.AddToFilter("PLDEBCA00016;", false);
            code = INSTANCE.RemFromFilter(null, false);
            INSTANCE.ClearFilter();
            assertEquals(-56, code);

        }

        @Test
        @DisplayName("remove from empty filter and add null isin")
        void remFromFilterEmptyNull() {
            code = INSTANCE.RemFromFilter(null, false);
            INSTANCE.ClearFilter();
            assertEquals(-56, code);

        }

        @Test
        @DisplayName("remove from empty filter and add valid isin")
        void remFromFilterEmptyValid() {
            code = INSTANCE.RemFromFilter("PLDEBCA00016;", false);
            INSTANCE.ClearFilter();
            assertNonNegative(code);

        }

        @Test
        @DisplayName("remove from empty filter and add invalid isin")
        void remFromFilterEmptyInalid() {
            code = INSTANCE.RemFromFilter("PLDEBCA00016invalid;", false);
            INSTANCE.ClearFilter();
            assertEquals(-34, code);

        }
    }


    @Test
    @DisplayName("successfully clear filter")
    void clearFilter() {
        code = INSTANCE.ClearFilter();
        assertNonNegative(code);
    }

    @Test
    void getResultCodeDesc() {
        code = 8;
        assertEquals("clear filter", INSTANCE.GetResultCodeDesc(code));
    }


    @Test
    @Disabled
    void APIOrderRequest() {
        fail("");
    }

    @Test
    @Disabled
    void setTradingSess() {
        fail("");
    }


    @Nested
    @DisplayName("Callback tests")
    class CallbackTests {
        @Test
        @DisplayName("initialize delay callback")
        void setCallbackDelay() {
            code = INSTANCE.SetCallbackDelay(delay -> System.out.println("current delay is: " + delay));
            assertNonNegative(code);

        }

        @Test
        @DisplayName("reinitialize delay callback")
        void setCallbackDelayReinitialize() {
            INSTANCE.SetCallbackDelay(delay -> System.out.println("current delay is: " + delay));
            INSTANCE.SetCallbackDelay(null);
            code = INSTANCE.SetCallbackDelay(delay -> System.out.println("current delay is: " + delay + " after reinitializing"));
            assertEquals(13, code);
        }

        @Test
        @DisplayName("deactivate delay callback")
        void setCallbackDelayDeactivate() {
            INSTANCE.SetCallbackDelay(delay -> System.out.println("current delay is: " + delay));
            code = INSTANCE.SetCallbackDelay(null);
            assertEquals(16, code);

        }

        @Test
        @DisplayName("init callback for Accounts")
        void setCallbackAccount() {
            code = INSTANCE.SetCallbackAccount(null);
            code = INSTANCE.SetCallbackAccount(System.out::println);
            assertEquals(13, code);
        }

        @Test
        @DisplayName("reinit callback for Accounts")
        void setCallbackAccountReinit() {
            code = INSTANCE.SetCallbackAccount(System.out::println);
            code = INSTANCE.SetCallbackAccount(null);
            code = INSTANCE.SetCallbackAccount(System.out::println);
            assertEquals(13, code);
        }

        @Test
        @DisplayName("deactivate callback for Accounts")
        void setCallbackAccountDeactivate() {
            code = INSTANCE.SetCallbackAccount(System.out::println);
            code = INSTANCE.SetCallbackAccount(null);
            assertEquals(16, code);
        }

        @Test
        @DisplayName("initialize callback for orders")
        void setCallbackOrder() {
            code = INSTANCE.SetCallbackOrder(nolorderreport -> System.out.println("orders: " + nolorderreport));
            assertEquals(13, code);
            INSTANCE.SetCallbackOrder(null);
        }

        @Test
        @DisplayName("reinitialize callback for orders")
        void setCallbackOrderReinit() {
            code = INSTANCE.SetCallbackOrder(nolorderreport -> System.out.println("orders: " + nolorderreport));
            code = INSTANCE.SetCallbackOrder(null);
            code = INSTANCE.SetCallbackOrder(nolorderreport -> System.out.println("orders reinit: " + nolorderreport));
            assertEquals(13, code);
            INSTANCE.SetCallbackOrder(null);
        }

        @Test
        @DisplayName("deactivate callback for orders")
        void setCallbackOrderDisable() {
            code = INSTANCE.SetCallbackOrder(nolorderreport -> System.out.println("orders: " + nolorderreport));
            code = INSTANCE.SetCallbackOrder(null);
            assertEquals(16, code);
        }

        @Test
        @DisplayName("init callback for Outlook")
        void setCallbackOutlook() {
            code = INSTANCE.SetCallbackOutlook(System.out::println);
            assertEquals(13, code);
        }

        @Test
        @DisplayName("reinit callback for Outlook")
        void setCallbackOutlookReinit() {
            code = INSTANCE.SetCallbackOutlook(System.out::println);
            code = INSTANCE.SetCallbackOutlook(null);
            code = INSTANCE.SetCallbackOutlook(System.out::println);
            assertEquals(13, code);
        }

        @Test
        @DisplayName("disable callback for Outlook")
        void setCallbackOutlookDisable() {
            code = INSTANCE.SetCallbackOutlook(System.out::println);
            code = INSTANCE.SetCallbackOutlook(null);
            assertEquals(16, code);
        }

        @Test
        @DisplayName("set callback for quotes")
        void setCallback() throws InterruptedException {
            int[] invoked = new int[1];
            invoked[0] = 0;
            //INSTANCE.SetTradingSess(true);
            BossaAPIInterface.SetCallbackDummy callback = nolrecentinfo -> {
                synchronized (invoked) {
                    invoked[0]++;
                    if (invoked[0] > 4) {
                        invoked.notifyAll();
                    }
                }
            };

            System.out.println(INSTANCE.SetCallback(callback));
            String isins = "PLVCAOC00015;PLNFI0600010;PLNFI0800016";
            //callback should be called immediatelly after setting filter
            INSTANCE.AddToFilter(isins, false);
            synchronized (invoked) {
                invoked.wait(1000);
            }
            assertEquals(3, invoked[0], () -> "invoked count: " + invoked[0]);
            INSTANCE.ClearFilter();
            INSTANCE.SetCallback(null);
        }
    }

    @Nested
    @DisplayName("Tickers test")
    class TickersTest {
        @Test
        @DisplayName("release tickerSelector list memory")
        void releaseTickersList() {
            BossaAPIInterface.NolTickers tickers = INSTANCE.InitListTickers();
            code = INSTANCE.ReleaseTickersList(tickers);
            assertEquals(3, code);
        }

        @Test
        @DisplayName("get all tickerSelector")
        void getTickers() {
            BossaAPIInterface.NolTickers tickers = INSTANCE.InitListTickers();
            code = INSTANCE.GetTickers(tickers, TypeOfList.ALL, null);
            System.out.println(getMessage(code));
            assertTrue(tickers.size > 1000);
        }

        @Test
        @Disabled
        @DisplayName("get ticker by ISIN with null in_ticker")
            //unfortunately this test will cause the api to crash and send "Only one function may be called" message
        void getTickersNull() {
            BossaAPIInterface.NolTickers tickers = INSTANCE.InitListTickers();
            code = INSTANCE.GetTickers(tickers, TypeOfList.ISIN, null);
            System.out.println(getMessage(code));
            assertEquals(-93, code);
            INSTANCE.Shutdown();
            INSTANCE.Initialize("BOS;BOS");
        }
    }

    @Nested
    @DisplayName("Interface init and shutdown tests")
    class InitShutdownTests {
        @Test
        @DisplayName("Successful init")
        void initialize() {
            code = INSTANCE.Initialize("BOS;BOS");
            assertEquals(1, code);
        }

        @Test
        @DisplayName("Successful shutdown")
        void shutdown() {
            code = INSTANCE.Shutdown();
            assertNonNegative(code);
            INSTANCE.Initialize("BOS;BOS");
        }
    }
}