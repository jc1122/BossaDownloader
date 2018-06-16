package app.API.JNAinterface;

import app.API.JNAenums.OrderType;
import app.API.PublicAPI.*;
import app.API.PublicAPI.Properties;

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
public enum BossaAPI implements OrderOperations, Properties<String, PropertyAPI<?, String>>, OnOffOperations {
    API;

    private static final BossaAPIInterface INSTANCE; //TODO make private
    private static final Map<String, PropertyAPI<?, String>> propertyMap = new HashMap<>();
    private static Set<Ticker> tickersInFilter = new HashSet<>();

    private static final Logger logger =
            Logger.getLogger(BossaAPI.class.getName());


    //initialize
    static {
        logger.finest("Initializing static");
        INSTANCE = BossaAPIInstance.INSTANCE;
        //add propertyMap to map
        propertyMap.put(Quotes.getInstance().getName(), Quotes.getInstance());
        propertyMap.put(Status.getInstance().getName(), Status.getInstance());
        propertyMap.put(Accounts.getInstance().getName(), Accounts.getInstance());
        propertyMap.put(Delay.getInstance().getName(), Delay.getInstance());
        propertyMap.put(Order.getInstance().getName(), Order.getInstance());
        propertyMap.put(Outlook.getInstance().getName(), Outlook.getInstance());
        propertyMap.put(Filter.getInstance().getName(), Filter.getInstance());
        propertyMap.put(Tickers.getInstance().getName(), Tickers.getInstance());
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
    @Override
    public String initialize() throws IllegalStateException {
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
     * Call to initialize callbacks of the api. Callbacks are accessible using {@link PropertyChangeListener}.
     * Quotes property is initialized by {@link BossaAPI#initialize()}
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
     * @param tickers a set of TickerSelector to be appended to filter
     */
    private String addTickersToFilter(Set<Ticker> tickers) {
        tickers.remove(null);
        Set<String> isins = tickers
                .stream()
                .map(Ticker::getIsin)
                .collect(Collectors.toSet());
        Object[] params = {isins};
        logger.entering(BossaAPI.class.getName(), "addToFilter", params);

        Set<Ticker> saveTickersInFilter = new HashSet<>(tickersInFilter);

        clearFilter();

        tickersInFilter = saveTickersInFilter;

        //TODO this logic is potentially buggy, if tickers are added to filter first, then exception occurs,
        // TODO effectively no tickers will be in filter but present in the maps
        tickersInFilter.addAll(tickers);

        String tickerString = tickerSetToString(tickersInFilter);
        int errorCode = INSTANCE.AddToFilter(tickerString, false);
        String output = GetResultCodeDesc(errorCode);
        if (errorCode < 0) {
            IllegalArgumentException e = new IllegalArgumentException(output);
            logger.finer(e.getMessage());
            throw e;
        }

        logger.exiting(BossaAPI.class.getName(), "addToFilter", output);
        //noinspection LawOfDemeter
        return output;
    }

    private String removeTickersFromFilter(Set<Ticker> tickers) throws IllegalStateException {
        logger.entering(BossaAPI.class.getName(), "removeTickersFromFilter", tickers);
        if (!tickersInFilter.containsAll(tickers)) {
            Set<Ticker> tmp = new HashSet<>(tickers);
            tmp.removeAll(tickersInFilter);
            throw new IllegalArgumentException(tmp + " not in filter");
        }
        tickersInFilter.removeAll(tickers);

        if (tickersInFilter.size() > 0) {
            addTickersToFilter(tickersInFilter);
        } else {
            clearFilter();
        }

        return "remove from filter"; //safe, exception in addTickersToFilter guards this
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
    @Override
    public String APIOrderRequest(
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
     * This method should be invoked before adding papers to filter.
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
    @Override
    public String getVersion() {
        logger.entering(BossaAPI.class.getName(), "getVersion");
        String version = INSTANCE.Get_Version();
        logger.exiting(BossaAPI.class.getName(), "getVersion", version);
        return version;
    }

    /**
     * Stops tracking quotes of all TickerSelector.
     * Removes all TickerSelector from tracking filter.
     *
     * @return success or error message
     */
    //clear filter before adding new papers
    private String clearFilter() {
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
    @Override
    public String shutdown() {
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
    @Override
    public Map<String, PropertyAPI<?,String>> getProperties() {
        return Collections.unmodifiableMap(propertyMap);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        getProperties().values().forEach(property -> property.addPropertyChangeListener(listener));
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        getProperties().values().forEach(property -> property.removePropertyChangeListener(listener));
    }

    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        if(getProperties().containsKey(propertyName)) {
            getProperties().get(propertyName).addPropertyChangeListener(listener);
        }
        else {
            throw new IllegalArgumentException(propertyName + " is not a valid property name");
        }
    }

    @Override
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        if(getProperties().containsKey(propertyName)) {
            getProperties().get(propertyName).removePropertyChangeListener(listener);
        }
        else {
            throw new IllegalArgumentException(propertyName + " is not a valid property name");
        }
    }

    @Override
    public PropertyAPI<?, String> getProperty(String name) {
        return propertyMap.get(name);
    }

    /**
     * Returns a set of TickerSelector which are currently in filter. Modyfing the returned set will not affect the filter.
     *
     * @return set of TickerSelector currently in filter
     */
    Set<Ticker> getTickersInFilter() {
        return new HashSet<>(tickersInFilter); //this should be immutable
    }

    //makes a semicolon separated string from given set of strings
    private static String tickerSetToString(Set<Ticker> tickers) {
        Set<String> isins = tickers.stream().map(Ticker::getIsin).collect(Collectors.toSet());

        StringBuilder filterFormat = new StringBuilder();
        for (String isin : isins) {
            filterFormat.append(isin);
            filterFormat.append(";");
        }
        return filterFormat.toString();
    }

    public static class MasterFilter extends BaseFilter<Ticker> {
        private static final MasterFilter INSTANCE = new MasterFilter();

        public static MasterFilter getInstance() {
            return INSTANCE;
        }

        private MasterFilter() {
            BossaAPI.API.getProperty("Quotes").addPropertyChangeListener(this);
        }
        @Override
        public String addTickersToFilter(Set<Ticker> tickers) {

            super.addTickersToFilter(tickers);
            Filter.getInstance().update(INSTANCE.getTickersInFilter());
            String result = BossaAPI.API.addTickersToFilter(tickers);
            return result;
        }

        @Override
        public String removeTickersFromFilter(Set<Ticker> tickers) throws IllegalStateException {

            super.removeTickersFromFilter(tickers);
            Filter.getInstance().update(INSTANCE.getTickersInFilter());
            String result = BossaAPI.API.removeTickersFromFilter(tickers);
            return result;
        }

        @Override
        public String clearFilter() {
            //String result = BossaAPI.API.clearFilter();
            super.clearFilter();
            return null;
        }

        @Override
        protected void addToWatchers(Set<Ticker> tickers, BaseFilter<Ticker> watcher) {

            super.addToWatchers(tickers, watcher);
            Filter.getInstance().update(INSTANCE.getTickersInFilter());
            BossaAPI.API.addTickersToFilter(tickers);
        }

        @Override
        protected void removeFromParent(Set<Ticker> tickers, BaseFilter<Ticker> filter) {

            super.removeFromParent(tickers, filter);
            Filter.getInstance().update(INSTANCE.getTickersInFilter());
            BossaAPI.API.removeTickersFromFilter(tickers);
        }
        @Override
        public void finalize() {
            BossaAPI.API.getProperty("Quotes").removePropertyChangeListener(this);
            super.finalize();
        }
    }
}
