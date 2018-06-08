package app.API;

import app.API.JNAinterface.BossaAPIInstance;
import app.API.JNAinterface.BossaAPIInterface;
import app.API.enums.OrderType;
import app.API.enums.TypeOfList;
import app.API.nolObjects.*;
import app.API.properties.*;

import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

//TODO check if everything is documented well

/**
 * This class is a wrapper for <a href="https://github.com/java-native-access/jna">JNA</a> mapping
 * of <a href="http://bossa.pl/notowania/narzedzia/bossapi/">BossaAPI</a> interface.
 *
 * <p> ALL methods of this class are <i>static</i></p>
 */
@SuppressWarnings({"unused", "Convert2MethodRef", "Convert2Lambda", "WeakerAccess"})
public class BossaAPI {

    private static final BossaAPIInterface INSTANCE; //TODO make private
    private static final Map<String, PropertyAPI> propertyMap = new HashMap<>();
    private static final Map<String, NolTickerAPI> tickersMap = new HashMap<>();
    private static Set<String> tickerISINSInFilter = new HashSet<>();
    private static Set<NolTickerAPI> tickersInFilter = new HashSet<>();

    private static final Logger logger =
            Logger.getLogger(BossaAPI.class.getName());

    //initialize
    static {
        logger.finest("Initializing static");
        INSTANCE = BossaAPIInstance.INSTANCE;
        //add propertyMap to map
        propertyMap.put("Quotes", Quotes.getInstance());
        propertyMap.put("Status", Status.getInstance());
        propertyMap.put("Accounts", Accounts.getInstance());
        propertyMap.put("Delay", Delay.getInstance());
        propertyMap.put("Order", Order.getInstance());
        propertyMap.put("Outlook", Outlook.getInstance());
        propertyMap.put("TickersInFilter", TickersInFilter.getInstance());

//        Map<String, String> functionNames = new HashMap<>();
//
//        //map method names to mangled dll library function names
//        functionNames.put("APIOrderRequest", "_APIOrderRequest@12");
//        functionNames.put("AddToFilter", "_AddToFilter@8");
//        functionNames.put("ClearFilter", "_ClearFilter@0");
//        functionNames.put("GetResultCodeDesc", "_GetResultCodeDesc@4");
//        functionNames.put("GetTickers", "_GetTickers@12");
//        functionNames.put("Get_Version", "_Get_Version@0");
//        functionNames.put("InitListTickers", "_InitListTickers@0");
//        functionNames.put("Initialize", "_Initialize@4");
//        functionNames.put("ReleaseTickersList", "_ReleaseTickersList@4");
//        functionNames.put("RemFromFilter", "_RemFromFilter@8");
//        functionNames.put("SetCallback", "_SetCallback@4");
//        functionNames.put("SetCallbackAccount", "_SetCallbackAccount@4");
//        functionNames.put("SetCallbackDelay", "_SetCallbackDelay@4");
//        functionNames.put("SetCallbackOrder", "_SetCallbackOrder@4");
//        functionNames.put("SetCallbackOutlook", "_SetCallbackOutlook@4");
//        functionNames.put("SetCallbackStatus", "_SetCallbackStatus@4");
//        functionNames.put("SetTradingSess", "_SetTradingSess@4");
//        functionNames.put("Shutdown", "_Shutdown@0");

//        Map<String, Object> options = new HashMap<>();
//        options.put(Library.OPTION_FUNCTION_MAPPER,
//                (FunctionMapper) (library, method) -> functionNames.get(method.getName()));
//
//        options.put(Library.OPTION_TYPE_MAPPER, new DefaultTypeMapper() {
//            {
//                addTypeConverter(JnaEnum.class, new EnumConverter());
//            }
//        });
//        try {
//            INSTANCE = (BossaAPIInterface)
//                    Native.loadLibrary("nolclientapi",
//                            BossaAPIInterface.class,
//                            options);
//        } catch (UnsatisfiedLinkError e) {
//            StringBuilder stackTrace = new StringBuilder();
//            for (StackTraceElement element : e.getStackTrace()) {
//                stackTrace.append(element).append(System.lineSeparator());
//            }
//            logger.severe(e.getMessage()
//                    + "\n please put nolclientapi.dll to your windows/SysWOW64 folder and use x86 Java runtime \n"
//                    + stackTrace);
//            throw e;
//        }

        logger.finest("Finished initializing static");
    }


    /**
     * Initializes library, should be called before calling any other methods.
     * Will fail if called, when <a href="http://bossa.pl/oferta/internet/pomoc/nol">NOL3</a>
     * is not running. To check status of NOL3, use {@link Status}
     * before calling this method. This method initialized {@link Quotes}
     *
     * @return intialization comment, will return only commentary about successful init
     * @throws IllegalStateException if unsuccessful
     */
    public static String initialize() throws IllegalStateException {
        logger.entering(BossaAPI.class.toString(), "initialize");
        InitializeObservers();
        int errorCode = INSTANCE.Initialize("BOS;BOS"); //the only accepted AppId by server
        String output = GetResultCodeDesc(errorCode);
        if (errorCode < 0) {
            IllegalStateException e = new IllegalStateException(output);
            logger.fine(e.toString());
            throw e;
        }
        SetTradingSess(true);
        //the observable below must be initialized after the lib is initialized, otherwise "lib is not inicialized" error
        INSTANCE.SetCallback(Quotes.getInstance());

        List<NolTickerAPI> tickers = NolTickersAPI.getTickers(TypeOfList.ALL, null);
        for (NolTickerAPI ticker : tickers) {
            tickersMap.put(ticker.getIsin(), ticker);
        }

        return output;
    }

    /**
     * Call to initialize callbacks of the api. Callbacks are accessible using {@link PropertyChangeListener}.
     * Quotes property is initialized by {@link BossaAPI#initialize()}
     * otherwise API complains about lib not being initialized.
     */
    private static void InitializeObservers() {
        logger.entering(BossaAPI.class.getName(), "InitializeObservers");
        //this below is messy, and could easily be cleaned using generics, however JNA will complain about
        //custom type mapping of Object type if you try to use generics
        INSTANCE.SetCallbackStatus(Status.getInstance());
        INSTANCE.SetCallbackAccount(Accounts.getInstance());
        INSTANCE.SetCallbackDelay(Delay.getInstance());
        INSTANCE.SetCallbackOrder(Order.getInstance());
        INSTANCE.SetCallbackOutlook(Outlook.getInstance());
        logger.exiting(BossaAPI.class.getName(), "InitializeObservers");
    }

    /**
     * Adds refactoredTickerSelector to watch (order and transactions).
     * To receive market data, this method should be called after {@link Quotes} is set up.
     * <p>
     * To receive market data, this method should be called after setting {@link BossaAPI#SetTradingSess(boolean)}
     * to {@code true}.
     * {@code isins} can be single ISIN obtained from ticker: {@link NolTickerAPI#getIsin()}
     * or may be multiple ISINs separated by {@code ";"} ex. {@code ISIN1;ISIN2 }.
     * If there are any refactoredTickerSelector already in the filter, they will remain there. New refactoredTickerSelector will be appended to filter.
     * </p>
     * <p>
     * {@link Quotes} property will be updated after calling this method.
     * </p>
     *
     * @param isins ISIN or name of ticker added to filter (name doesn't seem to work as of showVersion 1.0.0.70 of native library)
     * @return success message
     * @throws IllegalStateException if failed
     */
    public static String addToFilter(Set<String> isins) throws IllegalArgumentException {
        Object[] params = {isins};
        logger.entering(BossaAPI.class.getName(), "addToFilter", params);

        //need to be saved, or will be cleared by clearFilter
        Set<String> saveTickerISINSInFilter = new HashSet<>(tickerISINSInFilter);
        Set<NolTickerAPI> saveTickersInFilter = new HashSet<>(tickersInFilter);

        clearFilter();

        tickerISINSInFilter = saveTickerISINSInFilter;
        tickersInFilter = saveTickersInFilter;

        //TODO this logic is potentially buggy, if tickers are added to filter first, then exception occurs,
        // TODO effectively no tickers will be in filter but present in the maps
        tickerISINSInFilter.addAll(isins);

        for (String isin : isins) {
            tickersInFilter.add(tickersMap.get(isin));
        }

        String tickerString = isinSetToString(tickerISINSInFilter);
        int errorCode = INSTANCE.AddToFilter(tickerString, false);
        String output = GetResultCodeDesc(errorCode);
        if (errorCode < 0) {
            IllegalArgumentException e = new IllegalArgumentException(output);
            logger.finer(e.getMessage());
            throw e;
        }

        logger.exiting(BossaAPI.class.getName(), "addToFilter", output);
        TickersInFilter.getInstance().update(tickersInFilter);
        return output;
    }

    /**
     * @param tickers a set of refactoredTickerSelector to be appended to filter
     * @see BossaAPI#addToFilter(Set)
     */
    public static String addTickersToFilter(Set<NolTickerAPI> tickers) {
        tickers.remove(null);
        Set<String> isins = tickers
                .stream()
                .map(NolTickerAPI::getIsin)
                .collect(Collectors.toSet());
        return addToFilter(isins);
    }

    /**
     * Removes given refactoredTickerSelector from filter.
     *
     * @param isins of refactoredTickerSelector to be removed from filter
     * @return success message
     * @throws IllegalStateException if any of given refactoredTickerSelector are not in filter
     * @see BossaAPI#addToFilter(Set)
     */
    @SuppressWarnings("SameReturnValue")
    public static String removeFromFilter(Set<String> isins) throws IllegalStateException {
        Object[] params = {isins};
        logger.entering(BossaAPI.class.getName(), "removeFromFilter", params);
        if (!tickerISINSInFilter.containsAll(isins)) {
            throw new IllegalArgumentException(isins.removeAll(tickerISINSInFilter) + " not in filter");
        }
        tickerISINSInFilter.removeAll(isins);
        tickersInFilter.removeIf(ticker -> isins.contains(ticker.getIsin()));

        if (tickerISINSInFilter.size() > 0) {
            addToFilter(tickerISINSInFilter);
        } else {
            clearFilter();
        }

        return "remove from filter"; //safe, exception in addToFilter guards this
    }

    /**
     * TODO test this method, add exception and add javadoc
     *
     * @param nolorderrequest parameters of order
     * @param nolorderreport  this will be set and returned by this functio
     * @param Typ             type
     * @return error description
     * @throws IllegalStateException if failed
     */
    public static String APIOrderRequest(
            NolOrderRequestAPI nolorderrequest,
            NolOrderReportAPI nolorderreport,
            OrderType Typ) {
        logger.entering(BossaAPI.class.getName(), "APIOrderRequest");
        int errorCode = INSTANCE.APIOrderRequest(
                nolorderrequest.wrappee,
                nolorderreport.wrappee,
                Typ);
        String message = GetResultCodeDesc(errorCode);
        if (errorCode < 0) {
            IllegalStateException e = new IllegalStateException(message);
            logger.finer(e.getMessage());
            throw e;
        }
        logger.exiting(BossaAPI.class.getName(), "APIOrderRequest", message);
        return message;
    }

    /**
     * This method should be invoked before adding papers to filter {@link BossaAPI#addToFilter(Set)}.
     * Sets the request for trading session phase and status update.
     *
     * @param val true for update request, false for no request
     * @return success message
     * @throws IllegalStateException if unsuccessful
     */
    private static String SetTradingSess(boolean val) throws IllegalStateException {
        logger.entering(BossaAPI.class.getName(), "SetTradingSess", val);
        int errorCode = INSTANCE.SetTradingSess(val);
        String output = GetResultCodeDesc(errorCode);
        if (errorCode < 0) {
            IllegalStateException e = new IllegalStateException(output);
            logger.finer(e.getMessage());
            throw e;
        }

        logger.exiting(BossaAPI.class.getName(), "SetTradingSess", output);
        return output;
    }


    // function for describing errors
    public static String GetResultCodeDesc(int code) {    /* code returned by function */
        logger.entering(BossaAPI.class.getName(), "GetResultCodeDesc", code);
        String message = INSTANCE.GetResultCodeDesc(code);
        logger.exiting(BossaAPI.class.getName(), "GetResultCodeDesc", message);
        return message;
    }

    /**
     * Returns the showVersion of Bossa API dll.
     *
     * @return showVersion
     */
    // function for getting the information about verson of dll
    public static String getVersion() {
        logger.entering(BossaAPI.class.getName(), "getVersion");
        String version = INSTANCE.Get_Version();
        logger.exiting(BossaAPI.class.getName(), "getVersion", version);
        return version;
    }

    /**
     * Stops tracking quotes of all refactoredTickerSelector.
     * Removes all refactoredTickerSelector from tracking filter.
     *
     * @return success or error message
     */
    //clear filter before adding new papers
    @SuppressWarnings("UnusedReturnValue")
    public static String clearFilter() {
        logger.entering(BossaAPI.class.getName(), "clearFilter");
        tickerISINSInFilter.clear();
        tickersInFilter.clear();
        int errorCode = INSTANCE.ClearFilter();
        String message = GetResultCodeDesc(errorCode);
        if (errorCode < 0) {
            IllegalStateException e = new IllegalStateException(message);
            logger.finer(e.getMessage());
            throw e;
        }
        logger.exiting(BossaAPI.class.getName(), "clearFilter", message);
        return message;
    }

    /**
     * Call this method to clean up memory after finished working with dll API.
     *
     * @return message
     */
    public static String shutdown() {
        logger.entering(BossaAPI.class.getName(), "shutdown");
        int errorCode = INSTANCE.Shutdown();
        String message = GetResultCodeDesc(INSTANCE.Shutdown());
        if (errorCode < 0) throw new IllegalStateException(message);
        logger.exiting(BossaAPI.class.getName(), "shutdown", message);
        return message;
    }

    /**
     * The keys of the map are names of properties. The names should also be class names of properties ex. <i>Outlook</i>
     *
     * @return property names associated with instance of property class
     * @see PropertyAPI
     */
    public static Map<String, PropertyAPI> getPropertyMap() {
        return propertyMap;
    }

    /**
     * Returns a set of ISINS of refactoredTickerSelector which are currently in filter. Adding elements to the received set will not affect
     * the elements in filter.
     *
     * @return ISINs of refactoredTickerSelector in filter
     */
    public static Set<String> getTickerISINSInFilter() {
        return new HashSet<>(tickerISINSInFilter); //this should be immutable
    }

    /**
     * Returns a set of refactoredTickerSelector which are currently in filter. Modyfing the returned set will not affect the filter.
     *
     * @return set of refactoredTickerSelector currently in filter
     */
    public static Set<NolTickerAPI> getTickersInFilter() {
        return new HashSet<>(tickersInFilter); //this should be immutable
    }

    //makes a semicolon separated string from given set of strings
    private static String isinSetToString(Set<String> isins) {
        StringBuilder filterFormat = new StringBuilder();
        for (String isin : isins) {
            filterFormat.append(isin);
            filterFormat.append(";");
        }
        return filterFormat.toString();
    }
}
