package app.API.JNAinterface;

import app.API.PublicAPI.Ticker;
import org.jetbrains.annotations.NotNull;

/**
 * //TODO check behavior and make this an interface
 */
public final class NolOrderRequestAPI extends BossaAPIClassWrapper<NolOrderRequestAPI, BossaAPIInterface.NolOrderRequest> {
    private final String origID;
    private final String origID2;
    private final String acct;
    private final String side;
    private final String ordTyp;
    private final String tmInForce;
    private final String expireDt;
    private final String defPayTyp;
    private final String sessionDt;
    private final String expireTm;
    private final Ticker ticker;

    private NolOrderRequestAPI(BossaAPIInterface.NolOrderRequest nolOrderRequest) {
        super(nolOrderRequest);
        logger.exiting(NolOrderRequestAPI.class.getName(), "Constructor");

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

    int getBitMask() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getBitMask", wrappee.BitMask);
        return wrappee.BitMask;
    }

    @NotNull
    String getOrigID() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getOrigID");
        return origID;
    }

    @NotNull
    String getOrdID() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getOrdID");
        return new String(wrappee.OrdID).trim();
    }

    @NotNull
    String getOrdID2() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getOrdID2");
        return origID2;
    }

    @NotNull
    String getAcct() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getAcct");
        return acct;
    }

    int getMinQty() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getMinQty", wrappee.MinQty);
        return wrappee.MinQty;
    }

    int getDisplayQty() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getDisplayQty", wrappee.DisplayQty);
        return wrappee.DisplayQty;
    }

    @NotNull
    Ticker getTicker() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getTicker");
        return ticker;
    }

    @NotNull
    String getSide() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getSide");
        return side;
    }

    int getQty() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getQty");
        return wrappee.Qty;
    }

    @NotNull
    String getOrdTyp() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getOrdTyp");
        return ordTyp;
    }

    float getPx() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getPx");
        return wrappee.Px;
    }

    float getStopPx() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getStopPx");
        return wrappee.StopPx;
    }

    @NotNull
    String getCcy() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getCcy");
        return new String(wrappee.Ccy).trim();
    }

    @NotNull
    String getTmInForce() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getTmInForce");
        return tmInForce;
    }

    @NotNull
    String getExpireDt() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getExpireDt");
        return expireDt;
    }

    float getTrgrPx() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getTrgrPx");
        return wrappee.TrgrPx;
    }

    @NotNull
    String getDefPayTyp() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getDefPayTyp");
        return defPayTyp;
    }

    @NotNull
    String getSessionDt() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getSessionDt");
        return sessionDt;
    }

    @NotNull
    String getExpireTm() {
        logger.exiting(NolOrderRequestAPI.class.getName(), "getExpireTm");
        return expireTm;
    }

}
