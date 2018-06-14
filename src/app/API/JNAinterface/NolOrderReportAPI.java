package app.API.JNAinterface;

import app.API.Ticker;
import org.jetbrains.annotations.NotNull;

/**
 * TODO check behavior of this class
 */
public final class NolOrderReportAPI extends BossaAPIClassWrapper<NolOrderReportAPI, BossaAPIInterface.NolOrderReport> {
    private final String ordID;
    private final String ordID2;
    private final String statReqID;
    private final String execID;
    private final String execTyp;
    private final String stat;
    private final String acct;
    private final String side;
    private final String ordTyp;
    private final String ccy;
    private final String tmInForce;
    private final String expireDt;
    private final String txnTm;
    private final String defPayTyp;
    private final String getBizRejRsn;
    private final String txt;
    private final String expireTm;
    private final Ticker ticker;

    public NolOrderReportAPI(BossaAPIInterface.NolOrderReport nolOrderReport) {
        super(nolOrderReport);
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

    int getID() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getID");
        return wrappee.ID;
    }

    @NotNull
    String getOrdID() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getOrdID");
        return ordID;
    }

    @NotNull
    String getOrdID2() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getOrdID2");
        return ordID2;
    }

    @NotNull
    String getStatReqID() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getStatReqID");
        return statReqID;
    }

    @NotNull
    String getExecID() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getExecID");
        return execID;
    }

    @NotNull
    String getExecTyp() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getExecTyp");
        return execTyp;
    }

    @NotNull
    String getStat() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getStat");
        return stat;
    }

    int getRejRsn() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getRejRsn");
        return wrappee.RejRsn;
    }

    @NotNull
    String getAcct() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getAcct");
        return acct;
    }

    @NotNull
    Ticker getTicker() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getTicker");
        return ticker;
    }

    @NotNull
    String getSide() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getSide");
        return side;
    }

    int getQty() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getQty");
        return wrappee.Qty;
    }

    @NotNull
    String getOrdTyp() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getOrdTyp");
        return ordTyp;
    }

    float getPx() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getPx");
        return wrappee.Px;
    }

    float getStopPx() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getStopPx");
        return wrappee.StopPx;
    }

    @NotNull
    String getCcy() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getCcy");
        return ccy;
    }

    @NotNull
    String getTmInForce() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getTmInForce");
        return tmInForce;
    }

    @NotNull
    String getExpireDt() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getExpireDt");
        return expireDt;
    }

    float getLastPx() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getLastPx");
        return wrappee.LastPx;
    }

    int getLastQty() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getLastQty");
        return wrappee.LastQty;
    }

    int getLeavesQty() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getLeavesQty");
        return wrappee.LeavesQty;
    }

    int getCumQty() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getCumQty");
        return wrappee.CumQty;
    }


    @NotNull
    String getTxnTm() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getTxnTm");
        return txnTm;
    }

    float getComm() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getComm");
        return wrappee.Comm;
    }

    float getNetMny() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getNetMny");
        return wrappee.NetMny;
    }

    int getMinQty() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getMinQty");
        return wrappee.MinQty;
    }


    int getDisplayQty() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getDisplayQty");
        return wrappee.DisplayQty;
    }

    float getTrgrPx() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getTrgrPx");
        return wrappee.TrgrPx;
    }


    @NotNull
    String getDefPayTyp() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getDefPayTyp");
        return defPayTyp;
    }

    @NotNull
    String getBizRejRsn() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getBizRejRsn");
        return getBizRejRsn;
    }


    @NotNull
    String getTxt() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getTxt");
        return txt;
    }


    @NotNull
    String getExpireTm() {
        logger.exiting(NolOrderReportAPI.class.getName(), "getExpireTm");
        return expireTm;
    }

}
