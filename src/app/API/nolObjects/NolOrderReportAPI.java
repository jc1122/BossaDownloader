package app.API.nolObjects;

import app.API.JNAinterface.BossaAPIInterface;
import org.jetbrains.annotations.NotNull;

/**
 * TODO check behavior of this class
 */
public final class NolOrderReportAPI extends BossaAPIClassWrapper<NolOrderReportAPI, BossaAPIInterface.NolOrderReport> {
    String ordID, ordID2, statReqID, execID, execTyp, stat, acct, side, ordTyp, ccy, tmInForce, expireDt, txnTm;
    String defPayTyp, getBizRejRsn, txt, expireTm;
    NolTickerAPI ticker;

    public NolOrderReportAPI(BossaAPIInterface.NolOrderReport nolOrderReport) {
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
