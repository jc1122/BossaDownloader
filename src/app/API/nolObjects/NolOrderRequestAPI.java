package app.API.nolObjects;

import app.API.JNAinterface.BossaAPIInterface;
import org.jetbrains.annotations.NotNull;

/**
 * //TODO check behavior and add javadoc
 */
public final class NolOrderRequestAPI extends BossaAPIClassWrapper<NolOrderRequestAPI, BossaAPIInterface.NolOrderRequest> {
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
