package app.API;

import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class BossaAPIInterfaceTest {
    private static BossaAPIInterface INSTANCE;

    private static boolean checkIfNolIsRunning() {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        System.out.println("Checking if NOL3 is running.");
        try {
            Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));


            while ((line = input.readLine()) != null) {
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
    static void setUp() {
        assumeTrue(checkIfNolIsRunning(), () -> "NOL is not running");
        INSTANCE = BossaAPI.INSTANCE;
        INSTANCE.Initialize("BOS;BOS");
    }

    @AfterAll
    static void tearDown() {
        INSTANCE.Shutdown();
    }

    @Test
    @DisplayName("Successful init")
    void initialize() {
        int result = INSTANCE.Initialize("BOS;BOS");
        String message = INSTANCE.GetResultCodeDesc(result);
        assertTrue(result >= 0, message);
    }

    @Test
    @Disabled
    void addToFilter() {
        int code = INSTANCE.AddToFilter("PLDEBCA00016", false);
        INSTANCE.ClearFilter();
        assertTrue(code >= 0, getMessage(code));

    }

    @Test
    @Disabled
    void remFromFilter() {
        INSTANCE.AddToFilter("PLDEBCA00016;", false);
        int code = INSTANCE.RemFromFilter("PLDMLKR00023", false);
        INSTANCE.ClearFilter();
        assertTrue(code >= 0, getMessage(code));
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
                if (invoked[0] > 10) {
                    invoked.notifyAll();
                }
            }
        };

        System.out.println(INSTANCE.SetCallback(callback));
        String[] isins = {
                "PLVCAOC00015",
                "PLNFI0600010",
                "PLNFI0800016",
                "PL11BTS00015",
                "PL2CPRT00030",
                "PL2INTC00018",
                "PL4FNMD00013",
                "PLESLTN00010",
                "PL5AVNH00013",
                "PL71MDA00018",
                "PLTRCPS00016",
                "PLDEBCA00016",
                "PLDMLKR00023"};
        //callback should be called immediatelly after setting filter
        int repeats = 3;
        for (int i = 0; i < repeats; i++) {
            for (String isin : isins) {
                INSTANCE.ClearFilter();
            }
        }
        synchronized (invoked) {
            invoked.wait(1000);
        }
        assertEquals(invoked[0], isins.length * repeats, () -> "invoked count: " + invoked[0]);
        INSTANCE.ClearFilter();
        INSTANCE.SetCallback(null);
    }

    @Test
    @Disabled
    void clearFilter() {
        int code = INSTANCE.ClearFilter();
        assertTrue(code >= 0, getMessage(code));
    }

    @Test
    void getResultCodeDesc() {
        assertEquals("clear filter", INSTANCE.GetResultCodeDesc(8));
    }

    @Test
    void shutdown() {
        int code = INSTANCE.Shutdown();
        assertTrue(code >= 0, getMessage(code));
    }

    @Test
    void setCallbackAccount() {
        fail("");
    }

    @Test
    void setCallbackOrder() {
        fail("");
    }

    @Test
    void setCallbackOutlook() {
        fail("");
    }

    @Test
    void APIOrderRequest() {
        fail("");
    }

    @Test
    void setTradingSess() {
        fail("");
    }

    @Test
    void setCallbackDelay() {
        fail("");
    }

    @Test
    void getTickers() {
        fail("");
    }

    @Test
    void initListTickers() {
        fail("");
    }

    @Test
    void releaseTickersList() {
        fail("");
    }

    @Test
    void setCallbackStatus() {
        fail("");
    }
}