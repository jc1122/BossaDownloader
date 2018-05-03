package app.API;

import com.sun.jna.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.logging.Logger;

/**
 * This class is a wrapper for <a href="https://github.com/java-native-access/jna">JNA</a> mapping
 * of <a href="http://bossa.pl/notowania/narzedzia/bossapi/">BossaAPI</a> interface.
 *
 * <p> ALL methods of this class are <i>static</i></p>
 */
@SuppressWarnings({"unused", "Convert2MethodRef", "Convert2Lambda", "WeakerAccess"})
public enum BossaAPI {
    ;
    static final BossaAPIInterface INSTANCE;
    private static final Map<String, PropertyAPI> propertyMap = new HashMap<>();
    private static final Set<String> tickersInFilter = new HashSet<>();

    private static final Logger logger =
            Logger.getLogger(BossaAPI.class.getName());

    //initialize
    static {
        logger.finest("Initializing static");

        //add propertyMap to map
        propertyMap.put("Quotes", Quotes.getInstance());
        propertyMap.put("Status", Status.getInstance());
        propertyMap.put("Accounts", Accounts.getInstance());
        propertyMap.put("Delay", Delay.getInstance());
        propertyMap.put("Order", Order.getInstance());
        propertyMap.put("Outlook", Outlook.getInstance());

        Map<String, String> functionNames = new HashMap<>();

        //map method names to mangled dll library function names
        functionNames.put("APIOrderRequest", "_APIOrderRequest@12");
        functionNames.put("AddToFilter", "_AddToFilter@8");
        functionNames.put("ClearFilter", "_ClearFilter@0");
        functionNames.put("GetResultCodeDesc", "_GetResultCodeDesc@4");
        functionNames.put("GetTickers", "_GetTickers@12");
        functionNames.put("Get_Version", "_Get_Version@0");
        functionNames.put("InitListTickers", "_InitListTickers@0");
        functionNames.put("Initialize", "_Initialize@4");
        functionNames.put("ReleaseTickersList", "_ReleaseTickersList@4");
        functionNames.put("RemFromFilter", "_RemFromFilter@8");
        functionNames.put("SetCallback", "_SetCallback@4");
        functionNames.put("SetCallbackAccount", "_SetCallbackAccount@4");
        functionNames.put("SetCallbackDelay", "_SetCallbackDelay@4");
        functionNames.put("SetCallbackOrder", "_SetCallbackOrder@4");
        functionNames.put("SetCallbackOutlook", "_SetCallbackOutlook@4");
        functionNames.put("SetCallbackStatus", "_SetCallbackStatus@4");
        functionNames.put("SetTradingSess", "_SetTradingSess@4");
        functionNames.put("Shutdown", "_Shutdown@0");

        Map<String, Object> options = new HashMap<>();
        options.put(Library.OPTION_FUNCTION_MAPPER,
                (FunctionMapper) (library, method) -> functionNames.get(method.getName()));

        options.put(Library.OPTION_TYPE_MAPPER, new DefaultTypeMapper() {
            {
                addTypeConverter(JnaEnum.class, new EnumConverter());
            }
        });
        try {
            INSTANCE = (BossaAPIInterface)
                    Native.loadLibrary("nolclientapi",
                            BossaAPIInterface.class,
                            options);
        } catch (UnsatisfiedLinkError e) {
            StringBuilder stackTrace = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                stackTrace.append(element).append(System.lineSeparator());
            }
            logger.severe(e.getMessage()
                    + "\n please put nolclientapi.dll to your windows/SysWOW64 folder and use x86 Java runtime \n"
                    + stackTrace);
            throw e;
        }

        logger.finest("Finished initializing static");
    }


    /**
     * Method used to convert Java Native Access mapped pointer to array of given class,
     * to Java List of wrapper class. Pointer class must extend {@link Structure} or implement
     * {@link Structure.ByReference}.
     *
     * @param size         of array
     * @param pointer      to array
     * @param wrapperClass of pointer
     * @param <U>          pointer class
     * @param <T>          wrapper class
     * @return list of wrapper class
     */
    private static <U extends Structure,
            T extends BossaAPIClassWrapper<? extends BossaAPIClassWrapper, U>>
    List<T> convertPointerToListHelper(int size, U pointer, Class<T> wrapperClass) {
        Object[] params = {size, pointer, wrapperClass};
        logger.entering(BossaAPI.class.toString(), "convertPointerToListHelper", params);

        if (size == 0) {
            return Collections.emptyList();
        }
        Structure[] wrappeeList = pointer.toArray(size);
        List<T> wrappedAPIList = new ArrayList<>(size);

        //reflection will not find the right constructor if the pointer is ToReference extension of Structure
        Class pointerClass = pointer.getClass();
        if (pointer instanceof Structure.ByReference) {
            pointerClass = pointer.getClass().getSuperclass();
        }
        try {
            Constructor<T> wrapperConstructor = wrapperClass.getDeclaredConstructor(pointerClass);
            wrapperConstructor.setAccessible(true);
            for (Structure element : wrappeeList) {
                wrappedAPIList.add(wrapperConstructor.newInstance(element));
            }
        } catch (Exception e) {
            StringBuilder stackTrace = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                stackTrace.append(element).append(System.lineSeparator());
            }
            logger.severe("Pointer is of different class than the wrapper: " + e + "\n" + stackTrace);
            throw new RuntimeException(e);
        }

        logger.exiting(BossaAPI.class.toString(), "convertPointerToListHelper", wrappedAPIList);
        return wrappedAPIList;
    }

    /**
     * Initializes library, should be called before calling any other methods.
     * Will fail if called, when <a href="http://bossa.pl/oferta/internet/pomoc/nol">NOL3</a>
     * is not running. To check status of NOL3, use {@link Status}
     * before calling this method. This method initialized {@link Quotes}
     *
     * @return intialization comment
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
        INSTANCE.SetCallback(Quotes.getCallbackHelper());
        return output;
    }

    /**
     * Call to initialize callbacks of the api. Callbacks are stored in observables.
     * Quotes observable is initialized by {@link BossaAPI#initialize()}
     * otherwise API complains about lib not being initialized.
     */
    private static void InitializeObservers() {
        logger.entering(BossaAPI.class.getName(), "InitializeObservers");
        //this below is messy, and could easily be cleaned using generics, however JNA will complain about
        //custom type mapping of Object type if you try to use generics
        INSTANCE.SetCallbackStatus(Status.getCallbackHelper());
        INSTANCE.SetCallbackAccount(Accounts.getCallbackHelper());
        INSTANCE.SetCallbackDelay(Delay.getCallbackHelper());
        INSTANCE.SetCallbackOrder(Order.getCallbackHelper());
        INSTANCE.SetCallbackOutlook(Outlook.getCallbackHelper());
        logger.exiting(BossaAPI.class.getName(), "InitializeObservers");
    }

    /**
     * Adds tickers to watch (order and transactions).
     * To receive market data, this method should be called after {@link Quotes} is set up.
     * <p>
     * To receive market data, this method should be called after setting {@link BossaAPI#SetTradingSess(boolean)}
     * to {@code true}.
     * {@code isins} can be single ISIN obtained from ticker: {@link NolTickerAPI#getIsin()}
     * or may be multiple ISINs separated by {@code ";"} ex. {@code ISIN1;ISIN2 }.
     * If there are any tickers already in the filter, they will be removed.
     * </p>
     * <p>
     * {@link Quotes} will be updated after calling this method.
     * </p>
     *
     * @param isins ISIN or name (name doesn't seem to work as of showVersion 1.0.0.70 of native library)
     * @return success message
     * @throws IllegalStateException if failed
     */
    public static String addToFilter(Set<String> isins) throws IllegalStateException {
        Object[] params = {isins};
        logger.entering(BossaAPI.class.getName(), "addToFilter", params);
//        if(tickersInFilter.size() + isins.size() > 100) {
//            throw new IllegalStateException("Trying to exceed 100 tickers in filter! Use smaller amount of tickers.");
//        }
        clearFilter();
        tickersInFilter.addAll(isins);

        String tickerString = isinSetToString(tickersInFilter);
        int errorCode = INSTANCE.AddToFilter(tickerString, false);
        String output = GetResultCodeDesc(errorCode);
        if (errorCode < 0) {
            IllegalStateException e = new IllegalStateException(output);
            logger.finer(e.getMessage());
            throw e;
        }
        logger.exiting(BossaAPI.class.getName(), "addToFilter", output);
        return output;
    }

    public static String removeFromFilter(Set<String> isins) throws IllegalStateException {
        Object[] params = {isins};
        logger.entering(BossaAPI.class.getName(), "removeFromFilter", params);
        if (!tickersInFilter.containsAll(isins)) {
            throw new IllegalArgumentException(isins.removeAll(tickersInFilter) + " not in filter");
        }
        tickersInFilter.removeAll(isins);
        //must be a new set, because addToFilters clear mapping during assignment!
        addToFilter(new HashSet<>(tickersInFilter));
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


    /**
     * Returns the requested tickers.
     * <br>
     * Usage: <br>
     * {@code typeOfList.ALL} or {@code typeOfList.UNDEF_LIST} with any {@code in_ticker}
     * to get all tickers from server. <br>
     * {@code typeOfList.ISIN}, {@code typeOfList.CFI}, {@code typeOfList.MARKET_CODE}, {@code typeOfList.SYMBOL} with
     * non null {@code in_ticker} to get a list filtered by given field.
     *
     * @param typeOfList group filter
     * @param in_ticker  null or valid ticker
     * @return list of tickers
     */
    @NotNull
    public static List<NolTickerAPI> getTickers(TypeOfList typeOfList, NolTickerAPI in_ticker) {
        Object[] params = {typeOfList, in_ticker};
        logger.entering(BossaAPI.class.getName(), "getTickers", params);
        NolTickersAPI nolTickersAPI = new NolTickersAPI(typeOfList, in_ticker);
        logger.exiting(BossaAPI.class.getName(), "getTickers");
        return nolTickersAPI.getTickersList();
    }

    // function for describing errors
    private static String GetResultCodeDesc(int code) {    /* code returned by function */
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

    //clear filter before adding new papers
    @SuppressWarnings("UnusedReturnValue")
    public static String clearFilter() {
        logger.entering(BossaAPI.class.getName(), "clearFilter");
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

    public static Map<String, PropertyAPI> getPropertyMap() {
        return propertyMap;
    }

    //extracted to help composition
    private static abstract class BossaAPIClassWrapper<T, Q extends Structure> {
        Q wrappee;
    }

    /**
     * Stores market data.
     */
    public static final class NolBidAskTblAPI extends BossaAPIClassWrapper<NolBidAskTblAPI, BossaAPIInterface.NolBidAskTbl> {

        private NolBidAskTblAPI(BossaAPIInterface.NolBidAskTbl nolBidAskTbl) {
            this.wrappee = nolBidAskTbl;
        }

        /**
         * Position of offer level. <br>
         * Ex. If current offers on ask side are 100, 101, <b>102</b>, 103, then the offer in bold will have a depth of 3.
         *
         * @return position of offer, relative to best offer on given side
         */
        @Contract(pure = true)
        public int getDepth() {
            logger.exiting(NolBidAskTblAPI.class.getName(), "getDepth");
            return wrappee.depth;
        }

        /**
         * Returns the market side of offer. Bid or Ask.
         *
         * @return side
         */
        @Contract(pure = true)
        public MarketSide getSide() {
            logger.exiting(NolBidAskTblAPI.class.getName(), "getSide");
            if (wrappee.side > 2 || wrappee.side < 1) {
                throw new IllegalStateException("not initialized!");
            }
            return MarketSide.values[wrappee.side - 1];
        }

        /**
         * Price of offer on current level.
         *
         * @return price
         */
        @Contract(pure = true)
        public double getPrice() {
            logger.exiting(NolBidAskTblAPI.class.getName(), "getPrice");
            return wrappee.price;
        }

        /**
         * Volume of offers on current level.
         *
         * @return size
         */
        @Contract(pure = true)
        public int getSize() {
            logger.exiting(NolBidAskTblAPI.class.getName(), "getSize");
            return wrappee.size;
        }

        /**
         * Amount of offers on current level.
         *
         * @return amount
         */
        @Contract(pure = true)
        public int getAmount() {
            logger.exiting(NolBidAskTblAPI.class.getName(), "getAmount");
            return wrappee.amount;
        }

        @Override
        public String toString() {
            return "\nNolBidAskTblAPI \n" +
                    "Side: " + getSide() + "\n" +
                    "Depth: " + getDepth() + "\n" +
                    "Price: " + getPrice() + "\n" +
                    "Size: " + getSize() + "\n" +
                    "Count of offers: " + getAmount() + "\n";
        }
    }

    /**
     * Wrapper for pointer to offers.
     */
    private static final class NolBidAskStrAPI extends BossaAPIClassWrapper<NolBidAskStrAPI, BossaAPIInterface.NolBidAskStr> {

        List<NolBidAskTblAPI> bidAskList;

        private NolBidAskStrAPI(BossaAPIInterface.NolBidAskStr nolBidAskStr) {
            logger.entering(NolBidAskStrAPI.class.getName(), "Constructor");
            this.wrappee = nolBidAskStr;
            bidAskList = convertPointerToListHelper(wrappee.offersize, wrappee.bidask_table,
                    NolBidAskTblAPI.class);
        }

        @NotNull
        private List<NolBidAskTblAPI> getBidask_table() {
            logger.exiting(NolBidAskStrAPI.class.getName(), "getBidask_table", bidAskList);
            return bidAskList;
        }

    }

    /**
     * Stores data of single ticker.
     */
    public static final class NolTickerAPI extends BossaAPIClassWrapper<NolTickerAPI, BossaAPIInterface.NolTicker> {
        String isin, name, marketCode, cfi, group;

        private NolTickerAPI(BossaAPIInterface.NolTicker wrappee) {
            this.wrappee = wrappee;
            isin = new String(wrappee.Isin).trim();
            name = new String(wrappee.Name).trim();
            marketCode = new String(wrappee.MarketCode).trim();
            cfi = new String(wrappee.CFI).trim();
            group = new String(wrappee.Group).trim();
        }

        @NotNull
        public String getIsin() {
            logger.exiting(NolTickerAPI.class.getName(), "getIsin");
            return isin;
        }

        @NotNull
        public String getName() {
            logger.exiting(NolTickerAPI.class.getName(), "getName");
            return name;
        }

        @NotNull
        public String getMarketCode() {
            logger.exiting(NolTickerAPI.class.getName(), "getMarketCode");
            return marketCode;
        }

        @NotNull
        public String getCFI() {
            logger.exiting(NolTickerAPI.class.getName(), "getCFI");
            return cfi;
        }

        @NotNull
        public String getGroup() {
            logger.exiting(NolTickerAPI.class.getName(), "getGroup");
            return group;
        }

        @Override
        public String toString() {
            return "\nNolTickerAPI \n" +
                    "ISIN: " + getIsin() + "\n" +
                    "\nName: " + getName() +
                    "\nMarket code: " + getMarketCode() +
                    "\nCFI: " + getCFI() +
                    "\nGroup: " + getGroup() + "\n";
        }
    }

    /**
     * Stores list of tickers.
     * Needs to be closed manually using {@link NolTickersAPI#close()} after finished working with object to release
     * resources.
     */
    private static final class NolTickersAPI
            extends BossaAPIClassWrapper<NolTickersAPI, BossaAPIInterface.NolTickers>
            implements AutoCloseable {
        private List<NolTickerAPI> tickerListCache;

        private NolTickersAPI(TypeOfList typeOfList, NolTickerAPI in_ticker) {
            Object[] params = {typeOfList, in_ticker};
            logger.entering(NolTickersAPI.class.getName(), "Constructor", params);

            this.wrappee = INSTANCE.InitListTickers();
            if (in_ticker == null) {
                if (typeOfList.getIntValue() > 0) {
                    IllegalArgumentException e =
                            new IllegalArgumentException(typeOfList.name() + " cannot be used with null ticker!");
                    logger.finer(e.getMessage());
                    throw e;
                }
                INSTANCE.GetTickers(wrappee, typeOfList, null);
            } else {
                if (typeOfList.isTickerFieldEmpty(in_ticker)) {
                    IllegalArgumentException e =
                            new IllegalArgumentException("Ticker field " + typeOfList.name() + " is empty!");
                    logger.finer(e.getMessage());
                    throw e;
                }
                INSTANCE.GetTickers(wrappee, typeOfList, in_ticker.wrappee);
            }
            tickerListCache = convertPointerToListHelper(wrappee.size, wrappee.ptrtickerslist,
                    NolTickerAPI.class);
            logger.exiting(NolTickersAPI.class.getName(), "Constructor");
        }

        /**
         * Release resources after finished work with object.
         */
        @Override
        public void close() {
            int errorCode = INSTANCE.ReleaseTickersList(wrappee);
            wrappee = null;
            if (errorCode < 0) {
                String message = GetResultCodeDesc(errorCode);
                IllegalStateException e = new IllegalStateException(message);
                logger.finer(e.getMessage());
                throw e;
            }
        }

        /**
         * Returns list of tickers. Throws exception if trying to access if resources are released. <br>
         * See {@link NolTickersAPI#close()}.
         *
         * @return list of tickers
         */
        public List<NolTickerAPI> getTickersList() throws NullPointerException {
            if (wrappee == null) throw new NullPointerException("Tickers already closed!");
            return tickerListCache;

/*            List nolTickersList = Arrays.asList(wrappee.ptrtickerslist.toArray(wrappee.size));
            List<NolTickerAPI> nolTickersAPIList = new ArrayList<>(wrappee.size);

            for (Object ticker : nolTickersList) {
                nolTickersAPIList.add(new NolTickerAPI((BossaAPIInterface.NolTicker) ticker));
            }
            return nolTickersAPIList;*/
        }

    }

    /**
     * Stores market info about given ticker. <br>
     * This object is returned by {@link Quotes}
     * Data returned in this class is shattered. Not all fields are filled in each instance!
     * Throws {@link IllegalStateException} on construct on errornous received message.
     */
    public static final class NolRecentInfoAPI extends BossaAPIClassWrapper<NolRecentInfoAPI, BossaAPIInterface.NolRecentInfo> {

        NolBidAskStrAPI offers;
        NolTickerAPI ticker;
        String bitMask, toLT, phase, status;
        Map<String, Boolean> fieldInMessage;

        private NolRecentInfoAPI(BossaAPIInterface.NolRecentInfo nolRecentInfo) {
            logger.entering(NolRecentInfoAPI.class.getName(), "Constructor");
            this.wrappee = nolRecentInfo;
            offers = new NolBidAskStrAPI(wrappee.offers);
            ticker = new NolTickerAPI(wrappee.ticker);
            bitMask = Integer.toBinaryString(wrappee.BitMask);
            toLT = new String(wrappee.ToLT).trim();
            phase = new String(wrappee.Phase).trim();
            status = new String(wrappee.Status).trim();
            fieldInMessage = new HashMap<>();

            //fill with zeros to get 32 bits
            bitMask = new String(new char[32 - bitMask.length()]).replace("\0", "0").concat(bitMask);
            //reverse to get youngest bits at start
            bitMask = new StringBuilder(bitMask).reverse().toString();


            if (this.getError() < 0) {
                String message = GetResultCodeDesc(this.getError());
                IllegalStateException e = new IllegalStateException(message);
                logger.finer(e.getMessage());
                throw e;
            }
        }

        /**
         * Each bit in bitmask represents a field stored in this class.
         * {@code 1} for filled filled {@code 0} for unfilled.
         * Unfilled fields are set to 0.
         *
         * @return map of valid properties in this object
         */
        @Contract(pure = true)
        public Map<String, Boolean> getBitMask() {
            logger.exiting(NolRecentInfoAPI.class.getName(), "getBitMask", wrappee.BitMask);
            char[] arrayBitMask = bitMask.toCharArray();
            fieldInMessage.put("ValoLT", arrayBitMask[0] == '1');
            fieldInMessage.put("VoLT", arrayBitMask[1] == '1');
            fieldInMessage.put("ToLT", arrayBitMask[2] == '1');
            fieldInMessage.put("Open", arrayBitMask[3] == '1');
            fieldInMessage.put("High", arrayBitMask[4] == '1');
            fieldInMessage.put("Low", arrayBitMask[5] == '1');
            fieldInMessage.put("Close", arrayBitMask[6] == '1');
            fieldInMessage.put("Bid", arrayBitMask[7] == '1');
            fieldInMessage.put("Ask", arrayBitMask[8] == '1');
            fieldInMessage.put("BidSize", arrayBitMask[9] == '1');
            fieldInMessage.put("AskSize", arrayBitMask[10] == '1');
            fieldInMessage.put("TotalVolume", arrayBitMask[11] == '1');
            fieldInMessage.put("TotalValue", arrayBitMask[12] == '1');
            fieldInMessage.put("OpenInterest", arrayBitMask[13] == '1');
            fieldInMessage.put("Phase", arrayBitMask[14] == '1');
            fieldInMessage.put("Status", arrayBitMask[15] == '1');
            fieldInMessage.put("BidAmount", arrayBitMask[16] == '1');
            fieldInMessage.put("AskAmount", arrayBitMask[17] == '1');
            fieldInMessage.put("OpenValue", arrayBitMask[18] == '1');
            fieldInMessage.put("CloseValue", arrayBitMask[19] == '1');
            fieldInMessage.put("ReferPrice", arrayBitMask[20] == '1');
            fieldInMessage.put("Offers", arrayBitMask[21] == '1');
            fieldInMessage.put("Error", arrayBitMask[22] == '1');
            return fieldInMessage;
        }

        @NotNull
        public NolTickerAPI getTicker() {
            logger.exiting(NolTickerAPI.class.getName(), "getTicker");
            return ticker;
        }

        /**
         * Returns cash value of last transaction.
         *
         * @return cash value
         */
        @Contract(pure = true)
        public double getValoLT() {
            logger.exiting(NolTickerAPI.class.getName(), "getValoLT", wrappee.ValoLT);
            return wrappee.ValoLT;
        }

        /**
         * Returns volume of last transaction.
         *
         * @return volume
         */
        @Contract(pure = true)
        public int getVoLT() {
            logger.exiting(NolTickerAPI.class.getName(), "getVoLT", wrappee.VoLT);
            return wrappee.VoLT;
        }

        /**
         * Returns time of last transaction in hh:mm:ss format.
         *
         * @return time
         */
        @NotNull
        public String getToLT() {
            logger.exiting(NolTickerAPI.class.getName(), "getToLT");
            return toLT;
        }

        /**
         * Returns open price of current trading session.
         *
         * @return open
         */
        @Contract(pure = true)
        public double getOpen() {
            logger.exiting(NolTickerAPI.class.getName(), "getOpen");
            return wrappee.Open;
        }

        /**
         * Return high price of current trading session.
         *
         * @return high
         */
        @Contract(pure = true)
        public double getHigh() {
            logger.exiting(NolTickerAPI.class.getName(), "getHigh", wrappee.High);
            return wrappee.High;
        }

        /**
         * Return low price of current trading session.
         *
         * @return low
         */
        @Contract(pure = true)
        public double getLow() {
            logger.exiting(NolTickerAPI.class.getName(), "getLow", wrappee.Low);
            return wrappee.Low;
        }

        /**
         * Return close price of current trading session. TODO check if this is true!
         *
         * @return close
         */
        @Contract(pure = true)
        public double getClose() {
            logger.exiting(NolTickerAPI.class.getName(), "getClose", wrappee.Close);
            return wrappee.Close;
        }

        /**
         * Return current best bid price.
         *
         * @return bid price
         */
        @Contract(pure = true)
        public double getBid() {
            logger.exiting(NolTickerAPI.class.getName(), "getBid", wrappee.Bid);
            return wrappee.Bid;
        }

        /**
         * Return current best ask price.
         *
         * @return ask price
         */
        @Contract(pure = true)
        public double getAsk() {
            logger.exiting(NolTickerAPI.class.getName(), "getAsk", wrappee.Ask);
            return wrappee.Ask;
        }

        /**
         * Return size of offer of current best bid price.
         *
         * @return size
         */
        @Contract(pure = true)
        public int getBidSize() {
            logger.exiting(NolTickerAPI.class.getName(), "getBidSize", wrappee.BidSize);
            return wrappee.BidSize;
        }

        /**
         * Return size of offer of current best ask price.
         *
         * @return size
         */
        public int getAskSize() {
            logger.exiting(NolTickerAPI.class.getName(), "getAskSize", wrappee.AskSize);
            return wrappee.AskSize;
        }

        /**
         * Get cumulative volume of current trading session.
         *
         * @return cumulative volume
         */
        public int getTotalVolume() {
            logger.exiting(NolTickerAPI.class.getName(), "getTotalVolume", wrappee.TotalVolume);
            return wrappee.TotalVolume;
        }

        /**
         * Get cumulative turnover of current trading session.
         *
         * @return turnover
         */
        public double getTotalValue() {
            logger.exiting(NolTickerAPI.class.getName(), "getTotalValue", wrappee.TotalValue);
            return wrappee.TotalValue;
        }

        /**
         * Get open interest.
         *
         * @return open interest
         */
        public int getOpenInterest() {
            logger.exiting(NolTickerAPI.class.getName(), "getOpenInterest", wrappee.OpenInterest);
            return wrappee.OpenInterest;
        }

        /**
         * Phase of trading session.
         *
         * @return phase
         */
        public String getPhase() {
            logger.exiting(NolTickerAPI.class.getName(), "getPhase", wrappee.Phase);
            return phase;
        }

        /**
         * Status of trading session.
         *
         * @return status
         */
        public String getStatus() {
            logger.exiting(NolTickerAPI.class.getName(), "getStatus", wrappee.Status);
            return status;
        }

        /**
         * Get amount of offers of current best bid. May return 0 instead of real value.
         * Use {@link NolBidAskTblAPI#getAmount()} for selected best bid instead of this.
         *
         * @return amount of offers
         */
        public int getBidAmount() {
            logger.exiting(NolTickerAPI.class.getName(), "getBidAmount", wrappee.BidAmount);
            return wrappee.BidAmount;
        }

        /**
         * Get amount of offers of current best ask. May return 1 instead of real value.
         * Use {@link NolBidAskTblAPI#getAmount()} for selected best ask instead of this.
         *
         * @return amount of offers
         */
        public int getAskAmount() {
            logger.exiting(NolTickerAPI.class.getName(), "getAskAmount", wrappee.AskAmount);
            return wrappee.AskAmount;
        }

        /**
         * Returns turnover of session opening.
         *
         * @return turnover
         */
        public double getOpenValue() {
            logger.exiting(NolTickerAPI.class.getName(), "getOpenValue", wrappee.OpenValue);
            return wrappee.OpenValue;
        }

        /**
         * Returns turnover of session closing.
         *
         * @return turnover
         */
        public double getCloseValue() {
            logger.exiting(NolTickerAPI.class.getName(), "getCloseValue", wrappee.CloseValue);
            return wrappee.CloseValue;
        }

        /**
         * Return close price of previous trading session.
         *
         * @return reference price
         */
        public double getReferPrice() {
            logger.exiting(NolTickerAPI.class.getName(), "getReferPrice", wrappee.ReferPrice);
            return wrappee.ReferPrice;
        }

        /**
         * Return L2 market depth. Up to 5 price levels of bid and ask.
         *
         * @return list of offers
         * @see NolBidAskTblAPI
         */
        public List<NolBidAskTblAPI> getOffers() {
            logger.exiting(NolTickerAPI.class.getName(), "getOffers", wrappee.offers);
            return offers.getBidask_table();
        }

        /**
         * If <0 then errornous
         *
         * @return error code
         */
        private int getError() {
            return wrappee.Error;
        }

        @Override
        public String toString() {
            return "\nNolRecentInfoAPI" +
                    "\nBit mask: " + getBitMask() +
                    "\nTicker: " + getTicker() +
                    "\nValue of last transaction: " + getValoLT() +
                    "\nVolume of last transaction: " + getVoLT() +
                    "\nTime of last transaction: " + getToLT() +
                    "\nOpen: " + getOpen() +
                    "\nHigh: " + getHigh() +
                    "\nLow: " + getLow() +
                    "\nClose: " + getClose() +
                    "\nBid: " + getBid() +
                    "\nAsk: " + getAsk() +
                    "\nBid size: " + getBidSize() +
                    "\nAsk size: " + getAskSize() +
                    "\nTotal volume: " + getTotalVolume() +
                    "\nTotal value: " + getTotalValue() +
                    "\nOpen interest: " + getOpenInterest() +
                    "\nPhase: " + getPhase() +
                    "\nStatus: " + getStatus() +
                    "\nBid amount: " + getBidAmount() +
                    "\nAsk amount: " + getAskAmount() +
                    "\nOpen value: " + getOpenValue() +
                    "\nClose value: " + getCloseValue() +
                    "\nReference price: " + getReferPrice() +
                    "\nOffers: " + getOffers();
        }
    }

    /**
     * Contains information about funds in portfolio
     */
    private static final class NolFundAPI extends BossaAPIClassWrapper<NolFundAPI, BossaAPIInterface.NolFund> {

        String name, value;
        private NolFundAPI(BossaAPIInterface.NolFund nolFund) {
            this.wrappee = nolFund;
            name = new String(wrappee.name).trim();
            value = new String(wrappee.value).trim();
        }

        public String getName() {
            logger.exiting(NolFundAPI.class.getName(), "getName");
            return name;
        }

        public String getValue() {
            logger.exiting(NolFundAPI.class.getName(), "getValue");
            return value;
        }

    }

    /**
     * Contains information about position in portfolio.
     */
    public static final class NolPosAPI extends BossaAPIClassWrapper<NolPosAPI, BossaAPIInterface.NolPos> {
        NolTickerAPI ticker;

        private NolPosAPI(BossaAPIInterface.NolPos nolPos) {
            this.wrappee = nolPos;
            this.ticker = new NolTickerAPI(wrappee.ticker);
        }

        /**
         * Returns the ticker of portfolio position.
         *
         * @return ticker
         */
        @NotNull
        public NolTickerAPI getTicker() {
            logger.exiting(NolPosAPI.class.getName(), "getTicker");
            return ticker;
        }

        /**
         * Get amount of shares of position on default account. This amount is at users disposal.
         *
         * @return amount
         */
        public int getAcc110() {
            logger.exiting(NolPosAPI.class.getName(), "getAcc110", wrappee.acc110);
            return wrappee.acc110;
        }

        /**
         * Get amount of shares which are currently blocked. Ex. they may be blocked for a pending sell order.
         *
         * @return amount
         */
        public int getAcc120() {
            logger.exiting(NolPosAPI.class.getName(), "getAcc120", wrappee.acc120);
            return wrappee.acc120;
        }

        @Override
        public String toString() {
            return "\nNolPosAPI " +
                    "\nTicker: " + getTicker() +
                    "\nAcc110 shares free: " + getAcc110() +
                    "\nAcc120 shares blocked: " + getAcc120();
        }
    }

    /**
     * Stores information about available trading accounts.
     */
    public static final class NolStatementAPI extends BossaAPIClassWrapper<NolStatementAPI, BossaAPIInterface.NolStatement> {
        //private List<NolFundAPI> fundList;
        private Map<String, Double> fundMap;
        private List<NolPosAPI> positionList;

        String name, type;
        boolean ikeStatus;
        private NolStatementAPI(BossaAPIInterface.NolStatement nolStatement) {
            logger.entering(NolStatementAPI.class.getName(), "Constructor");
            this.wrappee = nolStatement;
            name = new String(wrappee.name).trim();
            ikeStatus = new String(wrappee.ike).trim().equals("T");
            type = new String(wrappee.type).trim();

            List<NolFundAPI> fundList = convertPointerToListHelper(wrappee.sizefund, wrappee.ptrfund, NolFundAPI.class);
            positionList = convertPointerToListHelper(wrappee.sizepos, wrappee.ptrpos, NolPosAPI.class);

            fundMap = new HashMap<>();
            for (NolFundAPI fund : fundList) {
                fundMap.put(fund.getName(), Double.parseDouble(fund.getValue()));
            }
            logger.exiting(NolStatementAPI.class.getName(), "Constructor");
        }

        /**
         * Account number of currently active account.
         *
         * @return account number
         */
        @NotNull
        public String getName() {
            logger.exiting(NolStatementAPI.class.getName(), "getName");
            return name;
        }


        /**
         * {@code True} if account is IKE (Indywidualne Konto Emerytalne), {@code false} if not.
         *
         * @return ike status
         */
        public boolean getIke() {
            logger.exiting(NolStatementAPI.class.getName(), "getIke");
            return ikeStatus;
        }

        /**
         * Returns account type:
         * {@code M} - cash market account <br>
         * {@code P} - derivates
         *
         * @return account type
         */
        @NotNull
        public String getType() {
            logger.exiting(NolStatementAPI.class.getName(), "getType");
            return type;
        }

        /**
         * Returns map of funds associated with account.
         *
         * @return map
         */
        @Contract(pure = true)
        public Map<String, Double> getFundMap() {
            logger.exiting(NolStatementAPI.class.getName(), "getFundMap", fundMap);
            return fundMap;
        }

        /**
         * Return list of papers in portfolio.
         *
         * @return positions
         */
        @Contract(pure = true)
        public List<NolPosAPI> getPositions() {
            logger.exiting(NolStatementAPI.class.getName(), "getPositions", fundMap);
            return positionList;
        }

        @Override
        public String toString() {
            return "NolStatementAPI \n" +
                    "\nName: " + getName() +
                    "\nIKE: " + getIke() +
                    "\nType: " + getType() +
                    "\nFunds: " + getFundMap() +
                    "\nPositions: " + getPositions();
        }
    }

    /**
     * Contains list of statements for all accessible accounts.
     */
    private static final class NolAggrStatementAPI extends BossaAPIClassWrapper<NolAggrStatementAPI, BossaAPIInterface.NolAggrStatement> {

        private List<NolStatementAPI> statementList;

        private NolAggrStatementAPI(BossaAPIInterface.NolAggrStatement nolAggrStatement) {
            logger.entering(NolAggrStatementAPI.class.getName(), "Constructor");
            this.wrappee = nolAggrStatement;
            this.statementList = convertPointerToListHelper(wrappee.size, wrappee.ptrstate, NolStatementAPI.class);
            logger.exiting(NolAggrStatementAPI.class.getName(), "Constructor");
        }

        /**
         * Get list of statements for all accessible accounts
         *
         * @return list of statements
         */
        @Contract(pure = true)
        public List<NolStatementAPI> getStatements() {
            logger.exiting(NolAggrStatementAPI.class.getName(), "getProperty", statementList);
            return statementList;
        }

    }

    /**
     * TODO check behavior of this class
     */
    public static final class NolOrderReportAPI extends BossaAPIClassWrapper<NolOrderReportAPI, BossaAPIInterface.NolOrderReport> {
        String ordID, ordID2, statReqID, execID, execTyp, stat, acct, side, ordTyp, ccy, tmInForce, expireDt, txnTm;
        String defPayTyp, getBizRejRsn, txt, expireTm;
        NolTickerAPI ticker;
        private NolOrderReportAPI(BossaAPIInterface.NolOrderReport nolOrderReport) {
            this.wrappee = nolOrderReport;
            ordID = new String(wrappee.OrdID).trim();
            ordID2 = new String(wrappee.OrdID2).trim();
            statReqID = new String(wrappee.StatReqID).trim();
            execID = new String(wrappee.ExecID).trim();
            execTyp = Byte.toString(wrappee.ExecTyp).trim();
            stat = Byte.toString(wrappee.Stat).trim();
            acct = new String(wrappee.Acct).trim();
            ticker = new NolTickerAPI(wrappee.ticker);
            side = new String(wrappee.Side).trim();
            ordTyp = new String(wrappee.OrdTyp).trim();
            ccy = new String(wrappee.Ccy).trim();
            tmInForce = new String(wrappee.TmInForce).trim();
            expireDt = new String(wrappee.ExpireDt).trim();
            txnTm = new String(wrappee.TxnTm).trim();
            defPayTyp = new String(wrappee.DefPayTyp).trim();
            getBizRejRsn = Byte.toString(wrappee.BizRejRsn);
            txt = new String(wrappee.Txt).trim();
            expireTm = new String(wrappee.ExpireTm).trim();
        }

        public long getBitMask() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getBitMask");
            return wrappee.BitMask;
        }

        public int getID() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getID");
            return wrappee.ID;
        }

        @NotNull
        public String getOrdID() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getOrdID");
            return ordID;
        }

        @NotNull
        public String getOrdID2() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getOrdID2");
            return ordID2;
        }

        @NotNull
        public String getStatReqID() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getStatReqID");
            return statReqID;
        }

        @NotNull
        public String getExecID() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getExecID");
            return execID;
        }

        @NotNull
        public String getExecTyp() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getExecTyp");
            return execTyp;
        }

        @NotNull
        public String getStat() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getStat");
            return stat;
        }

        public int getRejRsn() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getRejRsn");
            return wrappee.RejRsn;
        }

        @NotNull
        public String getAcct() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getAcct");
            return acct;
        }

        @NotNull
        public NolTickerAPI getTicker() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getTicker");
            return ticker;
        }

        @NotNull
        public String getSide() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getSide");
            return side;
        }

        public int getQty() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getQty");
            return wrappee.Qty;
        }

        @NotNull
        public String getOrdTyp() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getOrdTyp");
            return ordTyp;
        }

        public float getPx() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getPx");
            return wrappee.Px;
        }

        public float getStopPx() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getStopPx");
            return wrappee.StopPx;
        }

        @NotNull
        public String getCcy() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getCcy");
            return ccy;
        }

        @NotNull
        public String getTmInForce() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getTmInForce");
            return tmInForce;
        }

        @NotNull
        public String getExpireDt() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getExpireDt");
            return expireDt;
        }

        public float getLastPx() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getLastPx");
            return wrappee.LastPx;
        }

        public int getLastQty() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getLastQty");
            return wrappee.LastQty;
        }

        public int getLeavesQty() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getLeavesQty");
            return wrappee.LeavesQty;
        }

        public int getCumQty() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getCumQty");
            return wrappee.CumQty;
        }


        @NotNull
        public String getTxnTm() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getTxnTm");
            return txnTm;
        }

        public float getComm() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getComm");
            return wrappee.Comm;
        }

        public float getNetMny() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getNetMny");
            return wrappee.NetMny;
        }

        public int getMinQty() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getMinQty");
            return wrappee.MinQty;
        }


        public int getDisplayQty() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getDisplayQty");
            return wrappee.DisplayQty;
        }

        public float getTrgrPx() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getTrgrPx");
            return wrappee.TrgrPx;
        }


        @NotNull
        public String getDefPayTyp() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getDefPayTyp");
            return defPayTyp;
        }

        @NotNull
        public String getBizRejRsn() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getBizRejRsn");
            return getBizRejRsn;
        }


        @NotNull
        public String getTxt() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getTxt");
            return txt;
        }


        @NotNull
        public String getExpireTm() {
            logger.exiting(NolOrderReportAPI.class.getName(), "getExpireTm");
            return expireTm;
        }

    }

    public static final class NolOrderRequestAPI extends BossaAPIClassWrapper<NolOrderRequestAPI, BossaAPIInterface.NolOrderRequest> {
        String origID, origID2, acct, side, ordTyp, tmInForce, expireDt, defPayTyp, sessionDt, expireTm;
        NolTickerAPI ticker;
        private NolOrderRequestAPI(BossaAPIInterface.NolOrderRequest nolOrderRequest) {
            logger.exiting(NolOrderRequestAPI.class.getName(), "Constructor");
            this.wrappee = nolOrderRequest;

            origID = new String(wrappee.OrigID).trim();
            origID2 = new String(wrappee.OrdID2).trim();
            acct = new String(wrappee.Acct).trim();
            side = new String(wrappee.Side).trim();
            ordTyp = new String(wrappee.OrdTyp).trim();
            tmInForce = new String(wrappee.TmInForce).trim();
            expireDt = new String(wrappee.ExpireDt).trim();
            defPayTyp = new String(wrappee.DefPayTyp).trim();
            sessionDt = new String(wrappee.SessionDt).trim();
            expireTm = new String(wrappee.ExpireTm).trim();
            ticker = new NolTickerAPI(wrappee.ticker);
        }

        public int getBitMask() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getBitMask", wrappee.BitMask);
            return wrappee.BitMask;
        }

        @NotNull
        public String getOrigID() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getOrigID");
            return origID;
        }

        @NotNull
        public String getOrdID() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getOrdID");
            return new String(wrappee.OrdID).trim();
        }

        @NotNull
        public String getOrdID2() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getOrdID2");
            return origID2;
        }

        @NotNull
        public String getAcct() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getAcct");
            return acct;
        }

        public int getMinQty() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getMinQty", wrappee.MinQty);
            return wrappee.MinQty;
        }

        public int getDisplayQty() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getDisplayQty", wrappee.DisplayQty);
            return wrappee.DisplayQty;
        }

        @NotNull
        public NolTickerAPI getTicker() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getTicker");
            return ticker;
        }

        @NotNull
        public String getSide() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getSide");
            return side;
        }

        public int getQty() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getQty");
            return wrappee.Qty;
        }

        @NotNull
        public String getOrdTyp() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getOrdTyp");
            return ordTyp;
        }

        public float getPx() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getPx");
            return wrappee.Px;
        }

        public float getStopPx() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getStopPx");
            return wrappee.StopPx;
        }

        @NotNull
        public String getCcy() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getCcy");
            return new String(wrappee.Ccy).trim();
        }

        @NotNull
        public String getTmInForce() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getTmInForce");
            return tmInForce;
        }

        @NotNull
        public String getExpireDt() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getExpireDt");
            return expireDt;
        }

        public float getTrgrPx() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getTrgrPx");
            return wrappee.TrgrPx;
        }

        @NotNull
        public String getDefPayTyp() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getDefPayTyp");
            return defPayTyp;
        }

        @NotNull
        public String getSessionDt() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getSessionDt");
            return sessionDt;
        }

        @NotNull
        public String getExpireTm() {
            logger.exiting(NolOrderRequestAPI.class.getName(), "getExpireTm");
            return expireTm;
        }

    }

    //refactoring CallbackHelpers to generic class impossible due to type erasure...
    //cannot map Object type in JNA to multiple classes with typemapper, it would break ordinary type mapping
    public static class PropertyAPI<T> {
        protected final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
        protected T property;

        private PropertyAPI() {
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.removePropertyChangeListener(listener);
        }

        public T getProperty() {
            logger.exiting(this.getClass().getName(), "getProperty");
            if (property == null) {
                throw new NullPointerException(
                        "property "
                                + this.getClass()
                                + " has not been initialized yet, access through listener!");
            }
            return property;
        }
    }

    /**
     * Stores market data: info about price levels and trades.
     */
    public static final class Quotes extends PropertyAPI<NolRecentInfoAPI> {

        private static final Quotes INSTANCE = new Quotes();
        private static final CallbackHelper CALLBACK_HELPER = INSTANCE.new CallbackHelper();
        //private NolRecentInfoAPI nolRecentInfoAPI;

        private Quotes() {
        }

        public static Quotes getInstance() {
            logger.exiting(Quotes.class.getName(), "getInstance");
            return INSTANCE;
        }

        private static CallbackHelper getCallbackHelper() {
            return CALLBACK_HELPER;
        }

        private class CallbackHelper implements BossaAPIInterface.SetCallbackDummy {
            @Override
            public void invoke(BossaAPIInterface.NolRecentInfo nolrecentinfo) {
                logger.entering(CallbackHelper.class.getName(), "invoke");
                NolRecentInfoAPI oldValue = Quotes.this.property;
                Quotes.this.property = new NolRecentInfoAPI(nolrecentinfo);
                Quotes
                        .this
                        .propertyChangeSupport
                        .firePropertyChange("Quotes", oldValue, property);
                logger.exiting(CallbackHelper.class.getName(), "invoke", property);
            }
        }
    }

    /**
     * Stores info about current NOL3 app status.
     */
    public static final class Status extends PropertyAPI<Nol3State> {

        private static final Status INSTANCE = new Status();
        private static final Status.CallbackHelper CALLBACK_HELPER = INSTANCE.new CallbackHelper();

        private Status() {
        }

        public static Status getInstance() {
            logger.exiting(Status.class.getName(), "getInstance");
            return INSTANCE;
        }

        private static CallbackHelper getCallbackHelper() {
            return CALLBACK_HELPER;
        }

        private class CallbackHelper implements BossaAPIInterface.SetCallbackStatusDummy {
            @Override
            public void invoke(Nol3State nol3State) {
                logger.exiting(CallbackHelper.class.getName(), "invoke");
                Nol3State oldVal = Status.this.property;
                Status.this.property = nol3State;
                Status.this.propertyChangeSupport.firePropertyChange("Status", oldVal, property);
            }
        }
    }

    /**
     * Updates account statement information once received data from NOL3.
     */
    public static final class Accounts extends PropertyAPI<List<NolStatementAPI>> {
        //private NolAggrStatementAPI nolAggrStatementAPI;
        private static final Accounts INSTANCE = new Accounts();
        private static final CallbackHelper CALLBACK_HELPER = INSTANCE.new CallbackHelper();

        private Accounts() {
        }

        public static Accounts getInstance() {
            logger.exiting(Accounts.class.getName(), "getInstance");
            return INSTANCE;
        }

        private static CallbackHelper getCallbackHelper() {
            return CALLBACK_HELPER;
        }

        private class CallbackHelper implements BossaAPIInterface.SetCallbackAccountDummy {
            @Override
            public void invoke(BossaAPIInterface.NolAggrStatement nolAggrStatement) {
                logger.exiting(CallbackHelper.class.getName(), "invoke");
                List<NolStatementAPI> oldVal = Accounts.this.property;
                Accounts.this.property = new NolAggrStatementAPI(nolAggrStatement).getStatements();
                List<NolStatementAPI> statementList = (oldVal == null) ? Collections.emptyList() : oldVal;
                Accounts
                        .this
                        .propertyChangeSupport
                        .firePropertyChange(
                                "Accounts", statementList, Accounts.this.property);
            }
        }
    }

    /**
     * Stores delay time to server.
     */
    public static final class Delay extends PropertyAPI<Float> {

        private static final Delay INSTANCE = new Delay();
        private static final CallbackHelper CALLBACK_HELPER = INSTANCE.new CallbackHelper();

        private Delay() {
        }

        public static Delay getInstance() {
            logger.exiting(Delay.class.getName(), "getInstance");
            return INSTANCE;
        }

        private static CallbackHelper getCallbackHelper() {
            return CALLBACK_HELPER;
        }

        private class CallbackHelper implements BossaAPIInterface.SetCallbackDelayDummy {
            @Override
            public void invoke(float delay) {
                logger.exiting(CallbackHelper.class.getName(), "invoke");
                Float oldVal = Delay.this.property; //must be boxed type or InvocationTargetException by JNA callback
                Delay.this.property = delay;
                Delay.this.propertyChangeSupport.firePropertyChange("Delay", oldVal, delay);
            }
        }
    }

    /**
     * Stores information about current orders.
     */
    public static final class Order extends PropertyAPI<NolOrderReportAPI> {
        //private NolOrderReportAPI nolOrderReportAPI;
        private static final Order INSTANCE = new Order();
        private static final CallbackHelper CALLBACK_HELPER = INSTANCE.new CallbackHelper();

        private Order() {
        }

        public static Order getInstance() {
            logger.exiting(Order.class.getName(), "getInstance");
            return INSTANCE;
        }

        private static CallbackHelper getCallbackHelper() {
            return CALLBACK_HELPER;
        }

        private class CallbackHelper implements BossaAPIInterface.SetCallbackOrderDummy {
            @Override
            public void invoke(BossaAPIInterface.NolOrderReport nolOrderReport) {
                logger.exiting(CallbackHelper.class.getName(), "invoke");
                NolOrderReportAPI oldVal = Order.this.property;
                Order.this.property = new NolOrderReportAPI(nolOrderReport);
                Order
                        .this
                        .propertyChangeSupport
                        .firePropertyChange("Order", oldVal, Order.this.property);
            }
        }
    }

    /**
     * Stores diagnostic data from NOL3
     */
    public static final class Outlook extends PropertyAPI<String> {
        //private String outlook;
        private static final Outlook INSTANCE = new Outlook();
        private static final CallbackHelper CALLBACK_HELPER = INSTANCE.new CallbackHelper();

        private Outlook() {
        }

        public static Outlook getInstance() {
            logger.exiting(Outlook.class.getName(), "getInstance");
            return INSTANCE;
        }

        static CallbackHelper getCallbackHelper() {
            return CALLBACK_HELPER;
        }

        private class CallbackHelper implements BossaAPIInterface.SetCallbackOutlookDummy {
            @Override
            public void invoke(String outlook) {
                logger.exiting(CallbackHelper.class.getName(), "invoke");
                String oldVal = Outlook.this.property;
                Outlook.this.property = outlook;
                Outlook.this.propertyChangeSupport.firePropertyChange("Outlook", oldVal, outlook);
            }
        }
    }

    private static String isinSetToString(Set<String> isins) {
        StringBuilder filterFormat = new StringBuilder();
        for (String isin : isins) {
            filterFormat.append(isin);
            filterFormat.append(";");
        }
        return filterFormat.toString();
    }
}
