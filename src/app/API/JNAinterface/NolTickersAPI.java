package app.API.JNAinterface;

//import app.API.JNAinterface.BossaAPI;

import app.API.JNAenums.TypeOfList;
import app.API.PublicAPI.Ticker;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Stores list of TickerSelector.
 * Needs to be closed manually using {@link NolTickersAPI#close()} after finished working with object to release
 * resources.
 *
 * @see NolTickerAPI
 */
public final class NolTickersAPI
        extends BossaAPIClassWrapper<NolTickersAPI, BossaAPIInterface.NolTickers>
        implements AutoCloseable {
    private final List<NolTickerAPI> tickerListCache;

    //accessed by reflection
    private NolTickersAPI(TypeOfList typeOfList, NolTickerAPI in_ticker) {
        super(BossaAPIInstance.INSTANCE.InitListTickers());
        Object[] params = {typeOfList, in_ticker};
        logger.entering(NolTickersAPI.class.getName(), "Constructor", params);

        if (in_ticker == null) {
            if (typeOfList.getIntValue() > 0) {
                IllegalArgumentException e =
                        new IllegalArgumentException(typeOfList.name() + " cannot be used with null ticker!");
                logger.finer(e.getMessage());
                throw e;
            }
            BossaAPIInstance.INSTANCE.GetTickers(wrappee, typeOfList, null);
        } else {
            if (isTickerFieldEmpty(typeOfList, in_ticker)) {
                IllegalArgumentException e =
                        new IllegalArgumentException("Ticker field " + typeOfList.name() + " is empty!");
                logger.finer(e.getMessage());
                throw e;
            }
            BossaAPIInstance.INSTANCE.GetTickers(wrappee, typeOfList, in_ticker.wrappee);
        }
        tickerListCache = BossaAPIClassWrapper.convertPointerToListHelper(wrappee.size, wrappee.ptrtickerslist,
                NolTickerAPI.class);
        logger.exiting(this.getClass().getName(), "Constructor");
    }

    /**
     * Returns the requested TickerSelector.
     * <br>
     * Usage: <br>
     * {@code typeOfList.ALL} or {@code typeOfList.UNDEF_LIST} with any {@code in_ticker}
     * to get all TickerSelector from server. <br>
     * {@code typeOfList.ISIN}, {@code typeOfList.CFI}, {@code typeOfList.MARKET_CODE}, {@code typeOfList.SYMBOL} with
     * non null {@code in_ticker} to get a list filtered by given field.
     *
     * @param typeOfList group filter
     * @param in_ticker  null or valid ticker
     * @return TickerSelector
     */
    @NotNull
    public static List<Ticker> getTickers(TypeOfList typeOfList, NolTickerAPI in_ticker) {
        Object[] params = {typeOfList, in_ticker};
        logger.entering(NolTickersAPI.class.getName(), "getTickers", params);
        NolTickersAPI nolTickersAPI = new NolTickersAPI(typeOfList, in_ticker);
        logger.exiting(NolTickersAPI.class.getName(), "getTickers");
        return nolTickersAPI.getTickersList();
    }


    //do not refactor this to constant specific enum method, it is made this way to decouple TypeOfList and NolTickerAPI
    private boolean isTickerFieldEmpty(TypeOfList typeOfList, Ticker in_ticker) {
        switch (typeOfList) {
            case ALL:
                return false;
            case UNDEF_LIST:
                return false;
            case SYMBOL:
                return in_ticker.getName().equals("");
            case ISIN:
                return in_ticker.getIsin().equals("");
            case CFI:
                return in_ticker.getCFI().equals("");
            case MARKET_CODE:
                return in_ticker.getMarketCode().equals("");
            default:
                   throw new IllegalArgumentException("Unknown type of list: " +typeOfList);
        }
    }

    /**
     * Release resources after finished work with object.
     */
    @Override
    public void close() {
        int errorCode = BossaAPIInstance.INSTANCE.ReleaseTickersList(wrappee);
        if (errorCode < 0) {
            //String message = //BossaAPI.GetResultCodeDesc(errorCode);
            IllegalStateException e = new IllegalStateException(Integer.toString(errorCode));
            logger.finer(e.getMessage());
            throw e;
        }
    }

    /**
     * Returns list of TickerSelector. Throws exception if trying to access if resources are released. <br>
     * See {@link NolTickersAPI#close()}.
     *
     * @return list of TickerSelector
     */
    private List<Ticker> getTickersList() throws NullPointerException {
        if (wrappee == null) throw new NullPointerException("Tickers already closed!");
        return Collections.unmodifiableList(tickerListCache);
    }

    @Override
    public void finalize() {
        logger.warning(this.getClass() + " instance not closed, potential memory leak expected.");
        this.close();
    }

}
