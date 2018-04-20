package bossaAPIpackage;

import com.sun.jna.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * This class is a wrapper for <a href="https://github.com/java-native-access/jna">JNA</a> mapping
 * of <a href="http://bossa.pl/notowania/narzedzia/bossapi/">BossaAPI</a> interface.
 *
 * <p> All methods of this class are <i>static</i></p>
 */
@SuppressWarnings({"WeakerAccess", "unused", "Convert2MethodRef", "Convert2Lambda"})
public enum BossaAPI {
    ;
    private static BossaAPIInterface INSTANCE;

    static {
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
        options.put(Library.OPTION_FUNCTION_MAPPER, new FunctionMapper() {
            public String getFunctionName(NativeLibrary library, Method method) {
                return functionNames.get(method.getName());
            }
        });

        options.put(Library.OPTION_TYPE_MAPPER, new DefaultTypeMapper() {
            {
                addTypeConverter(JnaEnum.class, new EnumConverter());
            }
        });

        INSTANCE = (BossaAPIInterface)
                Native.loadLibrary("nolclientapi",
                        BossaAPIInterface.class,
                        options);
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
            System.err.println("Pointer is of different class than the wrapper: " + e);
        }


        return wrappedAPIList;
    }

    /**
     * Initializes library, should be called before calling any other methods.
     * Will fail if called, when <a href="http://bossa.pl/oferta/internet/pomoc/nol">NOL3</a>
     * is not running. To check status of NOL3, use {@link BossaAPI.StatusObservable}
     * before calling this method.
     *
     * @return intialization comment
     * @throws IllegalStateException if unsuccessful
     */
    public static String Initialize() throws IllegalStateException {
        int errorCode = INSTANCE.Initialize("BOS;BOS"); //the only accepted AppId by server
        String output = GetResultCodeDesc(errorCode);
        if (errorCode < 0) {
            throw new IllegalStateException(output);
        }
        //SetTradingSess(true);
        return output;
    }

    public static List<String> InitializeObservables() {
        List<String> results = new ArrayList<>();
        results.add("Quotes callback: " +
                INSTANCE.GetResultCodeDesc(INSTANCE.SetCallback(QuotesObservable.getInstance().new CallbackHelper())));
        results.add("Status callback: " +
                INSTANCE.GetResultCodeDesc(INSTANCE.SetCallbackStatus(StatusObservable.getInstance().new CallbackHelper())));
        results.add("Accounts callback: " +
                INSTANCE.GetResultCodeDesc(INSTANCE.SetCallbackAccount(AccountsObservable.getInstance().new CallbackHelper())));
        results.add("Delay callback: " +
                INSTANCE.GetResultCodeDesc(INSTANCE.SetCallbackDelay(DelayObservable.getInstance().new CallbackHelper())));
        results.add("Order callback: " +
                INSTANCE.GetResultCodeDesc(INSTANCE.SetCallbackOrder(OrderObservable.getInstance().new CallbackHelper())));
        results.add("Outlook callback: " +
                INSTANCE.GetResultCodeDesc(INSTANCE.SetCallbackOutlook(OutlookObservable.getInstance().new CallbackHelper())));
        return results;
    }

    /**
     * Adds tickers to watch (order and transactions).
     * To receive market data, this method should be called after {@link BossaAPI.QuotesObservable} is set up.
     * <p>
     * To receive market data, this method should be called after setting {@link BossaAPI#SetTradingSess(boolean)}
     * to {@code true}.
     * {@code TickersToAdd} can be single ISIN obtained from ticker: {@link NolTickerAPI#getIsin()}
     * or may be multiple ISINs separated by {@code ";"} ex. {@code ISIN1;ISIN2 }.
     * If there are any tickers already in the filter, they will be removed.
     * </p>
     * <p>
     * {@link BossaAPI.QuotesObservable} will be updated after calling this method.
     * </p>
     *
     * @param TickersToAdd ISIN or name (name doesn't seem to work as of version 1.0.0.70 of native library)
     * @param Flush        {@code false} for ISIN, {@code true} for name
     * @return success message
     * @throws IllegalStateException if failed
     */
    public static String AddToFilter(String TickersToAdd, boolean Flush) throws IllegalStateException {
        ClearFilter();
        int errorCode = INSTANCE.AddToFilter(TickersToAdd, Flush);
        String output = GetResultCodeDesc(errorCode);
        if (errorCode < 0) throw new IllegalStateException(output);
        return output;
    }


    /**
     * TODO test this method, add exception and add javadoc
     *
     * @param nolorderrequest parameters of order
     * @param nolorderreport  this will be set and returned by this functio
     * @param Typ             type
     * @return error code
     */
    public static int APIOrderRequest(
            NolOrderRequestAPI nolorderrequest,
            NolOrderReportAPI nolorderreport,
            OrderType Typ) {

        return INSTANCE.APIOrderRequest(
                nolorderrequest.wrappee,
                nolorderreport.wrappee,
                Typ);
    }

    /**
     * This method should be invoked before adding papers to filter {@link BossaAPI#AddToFilter(String, boolean)}.
     * Sets the request for trading session phase and status update.
     *
     * @param val true for update request, false for no request
     * @return success message
     * @throws IllegalStateException if unsuccessful
     */
    private static String SetTradingSess(boolean val) throws IllegalStateException {
        int errorCode = INSTANCE.SetTradingSess(val);
        String output = GetResultCodeDesc(errorCode);
        if (errorCode < 0) throw new IllegalStateException(output);
        return output;
    }


    /**
     * Returns the requested tickers.
     * <br>
     * Usage: <br>
     * {@code typeofList.ALL} or {@code typeofList.UndefList} with any {@code in_ticker}
     * to get all tickers from server. <br>
     * {@code typeofList.ISIN}, {@code typeofList.CFI}, {@code typeofList.MarketCode}, {@code typeofList.Symbol} with
     * non null {@code in_ticker} to get a list filtered by given field.
     *
     * @param typeofList group filter
     * @param in_ticker  null or valid ticker
     * @return group of tickers
     */
    @NotNull
    public static NolTickersAPI GetTickers(TypeofList typeofList, NolTickerAPI in_ticker) {
        return new NolTickersAPI(typeofList, in_ticker);
    }

    // function for describing errors
    private static String GetResultCodeDesc(int code) {    /* code returned by function */
        return INSTANCE.GetResultCodeDesc(code);
    }

    /**
     * Returns the version of Bossa API dll.
     *
     * @return version
     */
    // function for getting the information about verson of dll
    public static String Get_Version() {
        return INSTANCE.Get_Version();
    }

    //clear filter before adding new papers
    @SuppressWarnings("UnusedReturnValue")
    private static int ClearFilter() {
        return INSTANCE.ClearFilter();
    }

    /**
     * Call this method to clean up memory after finished working with dll API.
     *
     * @return message
     */
    public static String Shutdown() {
        int errorCode = INSTANCE.Shutdown();
        String message = GetResultCodeDesc(INSTANCE.Shutdown());
        if (errorCode < 0) throw new IllegalStateException(message);

        return message;
    }

    //extracted to help composition
    private static abstract class BossaAPIClassWrapper<T, Q extends Structure> {
        protected Q wrappee;
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
            return wrappee.depth;
        }

        /**
         * Returns the market side of offer. Bid or Ask.
         *
         * @return side
         */
        @Contract(pure = true)
        public MarketSide getSide() {
            return MarketSide.values[wrappee.side - 1];
        }

        /**
         * Price of offer on current level.
         *
         * @return price
         */
        @Contract(pure = true)
        public double getPrice() {
            return wrappee.price;
        }

        /**
         * Volume of offers on current level.
         *
         * @return size
         */
        @Contract(pure = true)
        public int getSize() {
            return wrappee.size;
        }

        /**
         * Amount of offers on current level.
         *
         * @return amount
         */
        @Contract(pure = true)
        public int getAmount() {
            return wrappee.amount;
        }


    }

    /**
     * Wrapper for pointer to offers.
     */
    private static final class NolBidAskStrAPI extends BossaAPIClassWrapper<NolBidAskStrAPI, BossaAPIInterface.NolBidAskStr> {

        List<NolBidAskTblAPI> bidAskList;

        private NolBidAskStrAPI(BossaAPIInterface.NolBidAskStr nolBidAskStr) {

            this.wrappee = nolBidAskStr;
            bidAskList = convertPointerToListHelper(wrappee.offersize, wrappee.bidask_table,
                    NolBidAskTblAPI.class);
        }

        @NotNull
        private List<NolBidAskTblAPI> getBidask_table() {
            return bidAskList;
            //return new NolBidAskTblAPI(wrappee.bidask_table);
        }

    }

    /**
     * Stores data of single ticker.
     */
    public static final class NolTickerAPI extends BossaAPIClassWrapper<NolTickerAPI, BossaAPIInterface.NolTicker> {

        private NolTickerAPI(BossaAPIInterface.NolTicker wrappee) {
            this.wrappee = wrappee;
        }

        @NotNull
        public String getIsin() {
            return new String(wrappee.Isin).trim();
        }

        @NotNull
        public String getName() {
            return new String(wrappee.Name).trim();
        }

        @NotNull
        public String getMarketCode() {
            return new String(wrappee.MarketCode).trim();
        }

        @NotNull
        public String getCFI() {
            return new String(wrappee.CFI).trim();
        }

        @NotNull
        public String getGroup() {
            return new String(wrappee.Group).trim();
        }

    }

    /**
     * Stores list of tickers.
     * Needs to be closed manually using {@link NolTickersAPI#close()} after finished working with object to release
     * resources.
     */
    public static final class NolTickersAPI extends BossaAPIClassWrapper<NolTickersAPI, BossaAPIInterface.NolTickers> {
        private List<NolTickerAPI> tickerListCache;

        private NolTickersAPI(TypeofList typeofList, NolTickerAPI in_ticker) {
            this.wrappee = INSTANCE.InitListTickers();
            if (in_ticker == null) {
                if (typeofList.getIntValue() > 0)
                    throw new IllegalArgumentException(typeofList.name() + " cannot be used with null ticker!");
                INSTANCE.GetTickers(wrappee, typeofList, null);
            } else {
                if (typeofList.isTickerFieldEmpty(in_ticker)) {
                    throw new IllegalArgumentException("Ticker field " + typeofList.name() + "is empty!");
                }
                INSTANCE.GetTickers(wrappee, typeofList, in_ticker.wrappee);
            }
            tickerListCache = convertPointerToListHelper(wrappee.size, wrappee.ptrtickerslist,
                    NolTickerAPI.class);
        }

        /**
         * Release resources after finished work with object.
         *
         * @return message
         */
        public String close() {
            int errorCode = INSTANCE.ReleaseTickersList(wrappee);
            wrappee = null;
            return BossaAPI.GetResultCodeDesc(errorCode);
        }

        @Override
        protected void finalize() {
            close();
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
     * This object is returned by {@link BossaAPI.QuotesObservable}
     * Data returned in this class is shattered. Not all fields are filled in each instance!
     */
    public static final class NolRecentInfoAPI extends BossaAPIClassWrapper<NolRecentInfoAPI, BossaAPIInterface.NolRecentInfo> {

        private NolRecentInfoAPI(BossaAPIInterface.NolRecentInfo nolRecentInfo) {
            this.wrappee = nolRecentInfo;
        }

        /**
         * Each bit in bitmask represents a field stored in this class.
         * {@code 1} for filled filled {@code 0} for unfilled.
         * Unfilled fields are set to 0.
         *
         * @return bit mask
         */
        @Contract(pure = true)
        public int getBitMask() {
            return wrappee.BitMask;
        }

        @NotNull
        public NolTickerAPI getTicker() {
            return new NolTickerAPI(wrappee.ticker);
        }

        /**
         * Returns cash value of last transaction.
         *
         * @return cash value
         */
        @Contract(pure = true)
        public double getValoLT() {
            return wrappee.ValoLT;
        }

        /**
         * Returns volume of last transaction.
         *
         * @return volume
         */
        @Contract(pure = true)
        public int getVoLT() {
            return wrappee.VoLT;
        }

        /**
         * Returns time of last transaction in hh:mm:ss format.
         *
         * @return time
         */
        @NotNull
        public String getToLT() {
            return new String(wrappee.ToLT).trim();
        }

        /**
         * Returns open price of current trading session.
         *
         * @return open
         */
        @Contract(pure = true)
        public double getOpen() {
            return wrappee.Open;
        }

        /**
         * Return high price of current trading session.
         *
         * @return high
         */
        @Contract(pure = true)
        public double getHigh() {
            return wrappee.High;
        }

        /**
         * Return low price of current trading session.
         *
         * @return low
         */
        @Contract(pure = true)
        public double getLow() {
            return wrappee.Low;
        }

        /**
         * Return close price of current trading session. TODO check if this is true!
         *
         * @return close
         */
        @Contract(pure = true)
        public double getClose() {
            return wrappee.Close;
        }

        /**
         * Return current best bid price.
         *
         * @return bid price
         */
        @Contract(pure = true)
        public double getBid() {
            return wrappee.Bid;
        }

        /**
         * Return current best ask price.
         *
         * @return ask price
         */
        @Contract(pure = true)
        public double getAsk() {
            return wrappee.Ask;
        }

        /**
         * Return size of offer of current best bid price.
         *
         * @return size
         */
        @Contract(pure = true)
        public int getBidSize() {
            return wrappee.BidSize;
        }

        /**
         * Return size of offer of current best ask price.
         *
         * @return size
         */
        public int getAskSize() {
            return wrappee.AskSize;
        }

        /**
         * Get cumulative volume of current trading session.
         *
         * @return cumulative volume
         */
        public int getTotalVolume() {
            return wrappee.TotalVolume;
        }

        /**
         * Get cumulative turnover of current trading session.
         *
         * @return turnover
         */
        public double getTotalValue() {
            return wrappee.TotalValue;
        }

        /**
         * Get open interest.
         *
         * @return open interest
         */
        public int getOpenInterest() {
            return wrappee.OpenInterest;
        }

        /**
         * Phase of trading session.
         *
         * @return phase
         */
        public String getPhase() {
            return new String(wrappee.Phase).trim();
        }

        /**
         * Status of trading session.
         *
         * @return status
         */
        public String getStatus() {
            return new String(wrappee.Status).trim();
        }

        /**
         * Get amount of offers of current best bid. May return 0 instead of real value.
         * Use {@link NolBidAskTblAPI#getAmount()} for selected best bid instead of this.
         *
         * @return amount of offers
         */
        @Deprecated
        public int getBidAmount() {
            return wrappee.BidAmount;
        }

        /**
         * Get amount of offers of current best ask. May return 1 instead of real value.
         * Use {@link NolBidAskTblAPI#getAmount()} for selected best ask instead of this.
         *
         * @return amount of offers
         */
        @Deprecated
        public int getAskAmount() {
            return wrappee.AskAmount;
        }

        /**
         * Returns turnover of session opening.
         *
         * @return turnover
         */
        public double getOpenValue() {
            return wrappee.OpenValue;
        }

        /**
         * Returns turnover of session closing.
         *
         * @return turnover
         */
        public double getCloseValue() {
            return wrappee.CloseValue;
        }

        /**
         * Return close price of previous trading session.
         *
         * @return reference price
         */
        public double getReferPrice() {
            return wrappee.ReferPrice;
        }

        /**
         * Return l2 market depth. Up to 5 price levels of bid and ask.
         *
         * @return list of offers
         * @see NolBidAskTblAPI
         */
        public List<NolBidAskTblAPI> getOffers() {
            return new NolBidAskStrAPI(wrappee.offers).getBidask_table();
        }

        /**
         * No idea TODO check behavior of this method
         *
         * @return error code
         */
        public int getError() {
            return wrappee.Error;
        }

    }

    /**
     * Contains information about funds in portfolio
     */
    private static final class NolFundAPI extends BossaAPIClassWrapper<NolFundAPI, BossaAPIInterface.NolFund> {

        private NolFundAPI(BossaAPIInterface.NolFund nolFund) {
            this.wrappee = nolFund;
        }

        public String getName() {
            return new String(wrappee.name).trim();
        }

        public String getValue() {
            return new String(wrappee.value).trim();
        }

    }

    /**
     * Contains information about position in portfolio.
     */
    public static final class NolPosAPI extends BossaAPIClassWrapper<NolPosAPI, BossaAPIInterface.NolPos> {

        private NolPosAPI(BossaAPIInterface.NolPos nolPos) {
            this.wrappee = nolPos;
        }

        /**
         * Returns the ticker of portfolio position.
         *
         * @return ticker
         */
        public NolTickerAPI getTicker() {
            return new NolTickerAPI(wrappee.ticker);
        }

        /**
         * Get amount of shares of position on default account. This amount is at users disposal.
         *
         * @return amount
         */
        public int getAcc110() {
            return wrappee.acc110;
        }

        /**
         * Get amount of shares which are currently blocked. Ex. they may be blocked for a pending sell order.
         *
         * @return amount
         */
        public int getAcc120() {
            return wrappee.acc120;
        }

    }

    /**
     * Stores information about available trading accounts.
     */
    public static final class NolStatementAPI extends BossaAPIClassWrapper<NolStatementAPI, BossaAPIInterface.NolStatement> {
        //private List<NolFundAPI> fundList;
        private Map<String, Double> fundMap;
        private List<NolPosAPI> positionList;

        private NolStatementAPI(BossaAPIInterface.NolStatement nolStatement) {

            this.wrappee = nolStatement;
            List<NolFundAPI> fundList = convertPointerToListHelper(wrappee.sizefund, wrappee.ptrfund, NolFundAPI.class);
            positionList = convertPointerToListHelper(wrappee.sizepos, wrappee.ptrpos, NolPosAPI.class);

            fundMap = new HashMap<>();
            for (NolFundAPI fund : fundList) {
                fundMap.put(fund.getName(), Double.parseDouble(fund.getValue()));
            }
        }

        /**
         * Account number of currently active account.
         *
         * @return account number
         */
        public String getName() {
            return new String(wrappee.name).trim();
        }


        /**
         * {@code True} if account is IKE (Indywidualne Konto Emerytalne), {@code false} if not.
         *
         * @return ike status
         */
        public boolean getIke() {
            return new String(wrappee.ike).trim().equals("T");
        }

        /**
         * Returns account type:
         * {@code M} - cash market account <br>
         * {@code P} - derivates
         *
         * @return account type
         */
        public String getType() {
            return new String(wrappee.type).trim();
        }

        /**
         * Returns map of funds associated with account.
         *
         * @return map
         */
        public Map<String, Double> getFundMap() {
            return fundMap;
/*            if(wrappee.sizefund == 0) {
                return Collections.emptyList();
            }
            List<Structure> nolFundList = Arrays.asList(wrappee.ptrfund.toArray(wrappee.sizefund));
            List<NolFundAPI> NolFundAPIList = new ArrayList<>(wrappee.sizefund);

            for (Object fund : nolFundList) {
                NolFundAPIList.add(new NolFundAPI((BossaAPIInterface.NolFund) fund));
            }
            return NolFundAPIList;*/
        }

        /**
         * Return list of papers in portfolio.
         *
         * @return positions
         */
        public List<NolPosAPI> getPositions() {
            return positionList;
/*            if(wrappee.sizepos == 0) {
                return Collections.emptyList();
            }
            List<Structure> nolPosList = Arrays.asList(wrappee.ptrpos.toArray(wrappee.sizepos));
            List<NolPosAPI> NolPosAPIList = new ArrayList<>(wrappee.sizepos);

            for (Object position : nolPosList) {
                NolPosAPIList.add(new NolPosAPI((BossaAPIInterface.NolPos) position));
            }
            return NolPosAPIList;*/
        }

    }

    /**
     * Contains list of statements for all accessible accounts.
     */
    public static final class NolAggrStatementAPI extends BossaAPIClassWrapper<NolAggrStatementAPI, BossaAPIInterface.NolAggrStatement> {

        private List<NolStatementAPI> statementList;

        private NolAggrStatementAPI(BossaAPIInterface.NolAggrStatement nolAggrStatement) {
            this.wrappee = nolAggrStatement;
            this.statementList = convertPointerToListHelper(wrappee.size, wrappee.ptrstate, NolStatementAPI.class);
        }

        /**
         * Get list of statements for all accessible accounts
         *
         * @return list of statements
         */
        public List<NolStatementAPI> getStatements() {
            return statementList;
/*            if(wrappee.size == 0) {
                return Collections.emptyList();
            }
            List<Structure> nolStatementList = Arrays.asList(wrappee.ptrstate.toArray(wrappee.size));
            List<NolStatementAPI> nolStatementAPIList = new ArrayList<>(wrappee.size);

            for (Object statement : nolStatementList) {
                nolStatementAPIList.add(new NolStatementAPI((BossaAPIInterface.NolStatement) statement));
            }
            return nolStatementAPIList;*/
        }

    }

    /**
     * TODO check behavior of this class
     */
    public static final class NolOrderReportAPI extends BossaAPIClassWrapper<NolOrderReportAPI, BossaAPIInterface.NolOrderReport> {

        private NolOrderReportAPI(BossaAPIInterface.NolOrderReport nolOrderReport) {
            this.wrappee = nolOrderReport;
        }

        public long getBitMask() {
            return wrappee.BitMask;
        }

        public int getID() {
            return wrappee.ID;
        }

        public String getOrdID() {
            return new String(wrappee.OrdID).trim();
        }

        public String getOrdID2() {
            return new String(wrappee.OrdID2).trim();
        }

        public String getStatReqID() {
            return new String(wrappee.StatReqID).trim();
        }

        public String getExecID() {
            return new String(wrappee.ExecID).trim();
        }

        public String getExecTyp() {
            return Byte.toString(wrappee.ExecTyp).trim();
        }

        public String getStat() {
            return Byte.toString(wrappee.Stat).trim();
        }

        public int getRejRsn() {
            return wrappee.RejRsn;
        }

        public String getAcct() {
            return new String(wrappee.Acct).trim();
        }

        public NolTickerAPI getTicker() {
            return new NolTickerAPI(wrappee.ticker);
        }

        public String getSide() {
            return new String(wrappee.Side).trim();
        }

        public int getQty() {
            return wrappee.Qty;
        }

        public String getOrdTyp() {
            return new String(wrappee.OrdTyp).trim();
        }

        public float getPx() {
            return wrappee.Px;
        }

        public float getStopPx() {
            return wrappee.StopPx;
        }

        public String getCcy() {
            return new String(wrappee.Ccy).trim();
        }

        public String getTmInForce() {
            return new String(wrappee.TmInForce).trim();
        }

        public String getExpireDt() {
            return new String(wrappee.ExpireDt).trim();
        }

        public float getLastPx() {
            return wrappee.LastPx;
        }

        public int getLastQty() {
            return wrappee.LastQty;
        }

        public int getLeavesQty() {
            return wrappee.LeavesQty;
        }

        public int getCumQty() {
            return wrappee.CumQty;
        }


        public String getTxnTm() {
            return new String(wrappee.TxnTm).trim();
        }

        public float getComm() {
            return wrappee.Comm;
        }

        public float getNetMny() {
            return wrappee.NetMny;
        }

        public int getMinQty() {
            return wrappee.MinQty;
        }


        public int getDisplayQty() {
            return wrappee.DisplayQty;
        }

        public float getTrgrPx() {
            return wrappee.TrgrPx;
        }


        public String getDefPayTyp() {
            return new String(wrappee.DefPayTyp).trim();
        }

        public String getBizRejRsn() {
            return Byte.toString(wrappee.BizRejRsn);
        }


        public String getTxt() {
            return new String(wrappee.Txt).trim();
        }


        public String getExpireTm() {
            return new String(wrappee.ExpireTm).trim();
        }

    }

    public static final class NolOrderRequestAPI extends BossaAPIClassWrapper<NolOrderRequestAPI, BossaAPIInterface.NolOrderRequest> {

        private NolOrderRequestAPI(BossaAPIInterface.NolOrderRequest nolOrderRequest) {
            this.wrappee = nolOrderRequest;
        }

        public int getBitMask() {
            return wrappee.BitMask;
        }

        public String getOrigID() {
            return new String(wrappee.OrigID).trim();
        }

        public String getOrdID() {
            return new String(wrappee.OrdID).trim();
        }

        public String getOrdID2() {
            return new String(wrappee.OrdID2).trim();
        }

        public String getAcct() {
            return new String(wrappee.Acct).trim();
        }

        public int getMinQty() {
            return wrappee.MinQty;
        }

        public int getDisplayQty() {
            return wrappee.DisplayQty;
        }

        public NolTickerAPI getTicker() {
            return new NolTickerAPI(wrappee.ticker);
        }

        public String getSide() {
            return new String(wrappee.Side).trim();
        }

        public int getQty() {
            return wrappee.Qty;
        }

        public String getOrdTyp() {
            return new String(wrappee.OrdTyp).trim();
        }

        public float getPx() {
            return wrappee.Px;
        }

        public float getStopPx() {
            return wrappee.StopPx;
        }

        public String getCcy() {
            return new String(wrappee.Ccy).trim();
        }

        public String getTmInForce() {
            return new String(wrappee.TmInForce).trim();
        }

        public String getExpireDt() {
            return new String(wrappee.ExpireDt).trim();
        }

        public float getTrgrPx() {
            return wrappee.TrgrPx;
        }

        public String getDefPayTyp() {
            return new String(wrappee.DefPayTyp).trim();
        }

        public String getSessionDt() {
            return new String(wrappee.SessionDt).trim();
        }

        public String getExpireTm() {
            return new String(wrappee.ExpireTm).trim();
        }

    }

    /**
     * Stores market data: info about price levels and trades.
     */
    public static final class QuotesObservable extends Observable {

        private static final QuotesObservable INSTANCE = new QuotesObservable();
        private NolRecentInfoAPI nolRecentInfoAPI;

        private QuotesObservable() {
        }

        public static QuotesObservable getInstance() {
            return INSTANCE;
        }

        /**
         * Call to get current info on update.
         *
         * @return
         */
        public NolRecentInfoAPI getNolRecentInfoAPI() {
            return nolRecentInfoAPI;
        }

        class CallbackHelper implements BossaAPIInterface.SetCallbackDummy {
            @Override
            public void invoke(BossaAPIInterface.NolRecentInfo nolrecentinfo) {
                nolRecentInfoAPI = new NolRecentInfoAPI(nolrecentinfo);
                setChanged();
                notifyObservers();
            }
        }
    }

    /**
     * Stores info about current NOL3 app status.
     */
    public static final class StatusObservable extends Observable {
        private Nol3State nol3State;
        private static StatusObservable INSTANCE = new StatusObservable();

        private StatusObservable() {
        }

        public static StatusObservable getInstance() {
            return INSTANCE;
        }

        /**
         * Get NOL3 state on update.
         *
         * @return
         */
        public Nol3State getNol3State() {
            return nol3State;
        }

        private class CallbackHelper implements BossaAPIInterface.SetCallbackStatusDummy {
            @Override
            public void invoke(Nol3State nol3State) {
                StatusObservable.this.nol3State = nol3State;
                setChanged();
                notifyObservers();
            }
        }
    }

    /**
     * Updates account statement information once received data from NOL3.
     */
    public static final class AccountsObservable extends Observable {
        private NolAggrStatementAPI nolAggrStatementAPI;
        private static AccountsObservable INSTANCE = new AccountsObservable();

        private AccountsObservable() {
        }

        public static AccountsObservable getInstance() {
            return INSTANCE;
        }

        /**
         * Call this method by observer, once updated, to get fresh data.
         *
         * @return accounts statements
         */
        public NolAggrStatementAPI getNolAggrStatementAPI() {
            return nolAggrStatementAPI;
        }

        private class CallbackHelper implements BossaAPIInterface.SetCallbackAccountDummy {
            @Override
            public void invoke(BossaAPIInterface.NolAggrStatement nolAggrStatement) {
                AccountsObservable.this.nolAggrStatementAPI = new NolAggrStatementAPI(nolAggrStatement);
                setChanged();
                notifyObservers();
            }
        }
    }

    /**
     * Stores delsy time to server.
     */
    public static final class DelayObservable extends Observable {
        private float delay;
        private static DelayObservable INSTANCE = new DelayObservable();

        private DelayObservable() {
        }

        public static DelayObservable getInstance() {
            return INSTANCE;
        }

        /**
         * Call this method by observer to get current delay.
         *
         * @return time to server
         */
        public float getDelay() {
            return delay;
        }

        private class CallbackHelper implements BossaAPIInterface.SetCallbackDelayDummy {
            @Override
            public void invoke(float delay) {
                DelayObservable.this.delay = delay;
                setChanged();
                notifyObservers();
            }
        }
    }

    /**
     * Stores information about current orders.
     */
    public static final class OrderObservable extends Observable {
        private NolOrderReportAPI nolOrderReportAPI;
        private static OrderObservable INSTANCE = new OrderObservable();

        private OrderObservable() {
        }

        public static OrderObservable getInstance() {
            return INSTANCE;
        }

        /**
         * Call to get order info by observer.
         *
         * @return order info
         */
        public NolOrderReportAPI getNolOrderReportAPI() {
            return nolOrderReportAPI;
        }

        private class CallbackHelper implements BossaAPIInterface.SetCallbackOrderDummy {
            @Override
            public void invoke(BossaAPIInterface.NolOrderReport nolOrderReport) {
                OrderObservable.this.nolOrderReportAPI = new NolOrderReportAPI(nolOrderReport);
                setChanged();
                notifyObservers();
            }
        }
    }

    /**
     * Stores diagnostic data from NOL3
     */
    public static final class OutlookObservable extends Observable {
        private String outlook;
        private static OutlookObservable INSTANCE = new OutlookObservable();

        private OutlookObservable() {
        }

        public static OutlookObservable getInstance() {
            return INSTANCE;
        }

        /**
         * Call to get current info on update.
         *
         * @return
         */
        public String getOutlook() {
            return outlook;
        }

        private class CallbackHelper implements BossaAPIInterface.SetCallbackOutlookDummy {
            @Override
            public void invoke(String outlook) {
                OutlookObservable.this.outlook = outlook;
                setChanged();
                notifyObservers();
            }
        }
    }
}
