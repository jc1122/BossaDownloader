package app.API.nolObjects;

import app.API.BossaAPI;
import app.API.JNAinterface.BossaAPIInterface;
import app.API.TypeOfList;

import java.util.List;

/**
 * Stores list of refactoredTickerSelector.
 * Needs to be closed manually using {@link NolTickersAPI#close()} after finished working with object to release
 * resources.
 *
 * @see NolTickerAPI
 */
public final class NolTickersAPI
        extends BossaAPIClassWrapper<NolTickersAPI, BossaAPIInterface.NolTickers>
        implements AutoCloseable {
    private List<NolTickerAPI> tickerListCache;

    public NolTickersAPI(TypeOfList typeOfList, NolTickerAPI in_ticker) {
        Object[] params = {typeOfList, in_ticker};
        logger.entering(NolTickersAPI.class.getName(), "Constructor", params);

        this.wrappee = BossaAPI.INSTANCE.InitListTickers();
        if (in_ticker == null) {
            if (typeOfList.getIntValue() > 0) {
                IllegalArgumentException e =
                        new IllegalArgumentException(typeOfList.name() + " cannot be used with null ticker!");
                logger.finer(e.getMessage());
                throw e;
            }
            BossaAPI.INSTANCE.GetTickers(wrappee, typeOfList, null);
        } else {
            if (typeOfList.isTickerFieldEmpty(in_ticker)) {
                IllegalArgumentException e =
                        new IllegalArgumentException("Ticker field " + typeOfList.name() + " is empty!");
                logger.finer(e.getMessage());
                throw e;
            }
            BossaAPI.INSTANCE.GetTickers(wrappee, typeOfList, in_ticker.wrappee);
        }
        tickerListCache = BossaAPIClassWrapper.convertPointerToListHelper(wrappee.size, wrappee.ptrtickerslist,
                NolTickerAPI.class);
        logger.exiting(this.getClass().getName(), "Constructor");
    }

    /**
     * Release resources after finished work with object.
     */
    @Override
    public void close() {
        int errorCode = BossaAPI.INSTANCE.ReleaseTickersList(wrappee);
        wrappee = null;
        if (errorCode < 0) {
            String message = BossaAPI.GetResultCodeDesc(errorCode);
            IllegalStateException e = new IllegalStateException(message);
            logger.finer(e.getMessage());
            throw e;
        }
    }

    /**
     * Returns list of refactoredTickerSelector. Throws exception if trying to access if resources are released. <br>
     * See {@link NolTickersAPI#close()}.
     *
     * @return list of refactoredTickerSelector
     */
    public List<NolTickerAPI> getTickersList() throws NullPointerException {
        if (wrappee == null) throw new NullPointerException("Tickers already closed!");
        return tickerListCache;
    }

    @Override
    public void finalize() {
        logger.warning(this.getClass() + " instance not closed, potential memory leak expected.");
        this.close();
    }

}
