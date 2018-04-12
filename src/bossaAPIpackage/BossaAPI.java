package bossaAPIpackage;

import com.sun.jna.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.*;

/**
 * bossaAPIpackage TODO dodaj to do modelu
 */
@SuppressWarnings({"WeakerAccess", "unused", "Convert2MethodRef", "Convert2Lambda"})
public enum BossaAPI {
    ;
    private static BossaAPIInterface apiInstance = null;

    static {
        Map<String, String> functionNames = new HashMap<>();

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

        apiInstance = (BossaAPIInterface)
                Native.loadLibrary("nolclientapi",
                        BossaAPIInterface.class,
                        options);
    }

    //declare all pointers used by API
    //private static BossaAPIInterface.NolTickers tickers = null;

    @Contract(pure = true)
    public static BossaAPIInterface getApiInstance() {
        return apiInstance;
    }

    private static abstract class BossaAPIClassWrapper<T, Q extends Structure> {
        protected Q wrappee;
    }

    private static <S extends BossaAPIClassWrapper,
            U extends Structure,
            T extends BossaAPIClassWrapper<S, U>>
    List<T> convertPointerToListHelper(int size, U pointer, Class<T> wrapperClass) {
        if (size == 0) {
            return Collections.emptyList();
        }
        List<Structure> wrappeeList = Arrays.asList(pointer.toArray(size));
        List<T> wrappedAPIList = new ArrayList<>(size);

        Class pointerClass = pointer.getClass();
        if (pointer instanceof Structure.ByReference) { //reflection will not find the right constructor without this ugly hack
            pointerClass = pointer.getClass().getSuperclass();
        }

        for (Structure element : wrappeeList) {
            try {
                wrappedAPIList.add(wrapperClass.getConstructor(pointerClass).newInstance(element));
            } catch (Exception e) {
                System.err.println("This should never happen: " + e);
            }
        }

        return wrappedAPIList;
    }

    public static final class NolBidAskTblAPI extends BossaAPIClassWrapper<NolBidAskTblAPI, BossaAPIInterface.NolBidAskTbl> {

        public NolBidAskTblAPI(BossaAPIInterface.NolBidAskTbl nolBidAskTbl) {
            this.wrappee = nolBidAskTbl;
        }

        @Contract(pure = true)
        public int getDepth() {
            return wrappee.depth;
        }

        @Contract(pure = true)
        public int getSide() {
            return wrappee.side;
        }

        @Contract(pure = true)
        public double getPrice() {
            return wrappee.price;
        }

        @Contract(pure = true)
        public int getSize() {
            return wrappee.size;
        }

        @Contract(pure = true)
        public int getAmount() {
            return wrappee.amount;
        }


    }


    public static final class NolBidAskStrAPI extends BossaAPIClassWrapper<NolBidAskStrAPI, BossaAPIInterface.NolBidAskStr> {

        public NolBidAskStrAPI(BossaAPIInterface.NolBidAskStr nolBidAskStr) {
            this.wrappee = nolBidAskStr;
        }

/*        @Contract(pure = true)
        public int getOffersize() {
            return wrappee.offersize;
        }*/

        @NotNull
        public List<NolBidAskTblAPI> getBidask_table() {
            return convertPointerToListHelper(wrappee.offersize, wrappee.bidask_table,
                    NolBidAskTblAPI.class);
            //return new NolBidAskTblAPI(wrappee.bidask_table);
        }

    }

    public static final class NolTickerAPI extends BossaAPIClassWrapper<NolTickerAPI, BossaAPIInterface.NolTicker> {

        public NolTickerAPI(BossaAPIInterface.NolTicker wrappee) {
            this.wrappee = wrappee;
        }

        @NotNull
        public String getIsin() {
            return new String(wrappee.Isin);
        }

        @NotNull
        public String getName() {
            return new String(wrappee.Name);
        }

        @NotNull
        public String getMarketCode() {
            return new String(wrappee.MarketCode);
        }

        @NotNull
        public String getCFI() {
            return new String(wrappee.CFI);
        }

        @NotNull
        public String getGroup() {
            return new String(wrappee.Group);
        }

    }

    public static final class NolTickersAPI extends BossaAPIClassWrapper<NolTickersAPI, BossaAPIInterface.NolTickers> {
        public NolTickersAPI(BossaAPIInterface.NolTickers nolTickers) {
            this.wrappee = nolTickers;
        }

        public List<NolTickerAPI> getTickersList() {
            return convertPointerToListHelper(wrappee.size, wrappee.ptrtickerslist,
                    NolTickerAPI.class);

/*            List nolTickersList = Arrays.asList(wrappee.ptrtickerslist.toArray(wrappee.size));
            List<NolTickerAPI> nolTickersAPIList = new ArrayList<>(wrappee.size);

            for (Object ticker : nolTickersList) {
                nolTickersAPIList.add(new NolTickerAPI((BossaAPIInterface.NolTicker) ticker));
            }
            return nolTickersAPIList;*/
        }

    }

    public static final class NolRecentInfoAPI extends BossaAPIClassWrapper<NolRecentInfoAPI, BossaAPIInterface.NolRecentInfo> {

        public NolRecentInfoAPI(BossaAPIInterface.NolRecentInfo nolRecentInfo) {
            this.wrappee = nolRecentInfo;
        }

        @Contract(pure = true)
        public int getBitMask() {
            return wrappee.BitMask;
        }

        @NotNull
        public NolTickerAPI getTicker() {
            return new NolTickerAPI(wrappee.ticker);
        }

        @Contract(pure = true)
        public double getValoLT() {
            return wrappee.ValoLT;
        }

        @Contract(pure = true)
        public int getVoLT() {
            return wrappee.VoLT;
        }

        @NotNull
        public String getToLT() {
            return new String(wrappee.ToLT);
        }

        @Contract(pure = true)
        public double getOpen() {
            return wrappee.Open;
        }

        @Contract(pure = true)
        public double getHigh() {
            return wrappee.High;
        }

        @Contract(pure = true)
        public double getLow() {
            return wrappee.Low;
        }

        @Contract(pure = true)
        public double getClose() {
            return wrappee.Close;
        }

        @Contract(pure = true)
        public double getBid() {
            return wrappee.Bid;
        }

        @Contract(pure = true)
        public double getAsk() {
            return wrappee.Ask;
        }

        @Contract(pure = true)
        public int getBidSize() {
            return wrappee.BidSize;
        }

        public int getAskSize() {
            return wrappee.AskSize;
        }

        public int getTotalVolume() {
            return wrappee.TotalVolume;
        }

        public double getTotalValue() {
            return wrappee.TotalValue;
        }

        public int getOpenInterest() {
            return wrappee.OpenInterest;
        }

        public String getPhase() {
            return new String(wrappee.Phase);
        }

        public String getStatus() {
            return new String(wrappee.Status);
        }

        public int getBidAmount() {
            return wrappee.BidAmount;
        }

        public int getAskAmount() {
            return wrappee.AskAmount;
        }

        public double getOpenValue() {
            return wrappee.OpenValue;
        }

        public double getCloseValue() {
            return wrappee.CloseValue;
        }

        public double getReferPrice() {
            return wrappee.ReferPrice;
        }

        public NolBidAskStrAPI getOffers() {
            return new NolBidAskStrAPI(wrappee.offers);
        }

        public int getError() {
            return wrappee.Error;
        }

    }

    public static final class NolFundAPI extends BossaAPIClassWrapper<NolFundAPI, BossaAPIInterface.NolFund> {

        public NolFundAPI(BossaAPIInterface.NolFund nolFund) {
            this.wrappee = nolFund;
        }

        public String getName() {
            return new String(wrappee.name);
        }

        public String getValue() {
            return new String(wrappee.value);
        }

    }

    public static final class NolPosAPI extends BossaAPIClassWrapper<NolPosAPI, BossaAPIInterface.NolPos> {

        public NolPosAPI(BossaAPIInterface.NolPos nolPos) {
            this.wrappee = nolPos;
        }

        public NolTickerAPI getTicker() {
            return new NolTickerAPI(wrappee.ticker);
        }

        public int getAcc110() {
            return wrappee.acc110;
        }

        public int getAcc120() {
            return wrappee.acc120;
        }

    }

    public static final class NolStatementAPI extends BossaAPIClassWrapper<NolStatementAPI, BossaAPIInterface.NolStatement> {

        public NolStatementAPI(BossaAPIInterface.NolStatement nolStatement) {
            this.wrappee = nolStatement;
        }

        public String getName() {
            return new String(wrappee.name);
        }

        public String getIke() {
            return new String(wrappee.ike);
        }

        public String getType() {
            return new String(wrappee.type);
        }

        public List<NolFundAPI> getFund() {
            return convertPointerToListHelper(wrappee.sizefund, wrappee.ptrfund,
                    NolFundAPI.class);
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

        public List<NolPosAPI> getPositions() {
            return convertPointerToListHelper(wrappee.sizepos, wrappee.ptrpos, NolPosAPI.class);
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

    public static final class NolAggrStatementAPI extends BossaAPIClassWrapper<NolAggrStatementAPI, BossaAPIInterface.NolAggrStatement> {

        public NolAggrStatementAPI(BossaAPIInterface.NolAggrStatement nolAggrStatement) {
            this.wrappee = nolAggrStatement;
        }

        public List<NolStatementAPI> getStatement() {
            return convertPointerToListHelper(wrappee.size, wrappee.ptrstate, NolStatementAPI.class);
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


    public static final class NolOrderReportAPI extends BossaAPIClassWrapper<NolOrderReportAPI, BossaAPIInterface.NolOrderReport> {

        public NolOrderReportAPI(BossaAPIInterface.NolOrderReport nolOrderReport) {
            this.wrappee = nolOrderReport;
        }

        public long getBitMask() {
            return wrappee.BitMask;
        }

        public int getID() {
            return wrappee.ID;
        }

        public String getOrdID() {
            return new String(wrappee.OrdID);
        }

        public String getOrdID2() {
            return new String(wrappee.OrdID2);
        }

        public String getStatReqID() {
            return new String(wrappee.StatReqID);
        }

        public String getExecID() {
            return new String(wrappee.ExecID);
        }

        public String getExecTyp() {
            return Byte.toString(wrappee.ExecTyp);
        }

        public String getStat() {
            return Byte.toString(wrappee.Stat);
        }

        public int getRejRsn() {
            return wrappee.RejRsn;
        }

        public String getAcct() {
            return new String(wrappee.Acct);
        }

        public NolTickerAPI getTicker() {
            return new NolTickerAPI(wrappee.ticker);
        }

        public String getSide() {
            return new String(wrappee.Side);
        }

        public int getQty() {
            return wrappee.Qty;
        }

        public String getOrdTyp() {
            return new String(wrappee.OrdTyp);
        }

        public float getPx() {
            return wrappee.Px;
        }

        public float getStopPx() {
            return wrappee.StopPx;
        }

        public String getCcy() {
            return new String(wrappee.Ccy);
        }

        public String getTmInForce() {
            return new String(wrappee.TmInForce);
        }

        public String getExpireDt() {
            return new String(wrappee.ExpireDt);
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
            return new String(wrappee.TxnTm);
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
            return new String(wrappee.DefPayTyp);
        }

        public String getBizRejRsn() {
            return Byte.toString(wrappee.BizRejRsn);
        }


        public String getTxt() {
            return new String(wrappee.Txt);
        }


        public String getExpireTm() {
            return new String(wrappee.ExpireTm);
        }

    }

    public static final class NolOrderRequestAPI extends BossaAPIClassWrapper<NolOrderRequestAPI, BossaAPIInterface.NolOrderRequest> {

        public NolOrderRequestAPI(BossaAPIInterface.NolOrderRequest nolOrderRequest) {
            this.wrappee = nolOrderRequest;
        }

        public int getBitMask() {
            return wrappee.BitMask;
        }

        public String getOrigID() {
            return new String(wrappee.OrigID);
        }

        public String getOrdID() {
            return new String(wrappee.OrdID);
        }

        public String getOrdID2() {
            return new String(wrappee.OrdID2);
        }

        public String getAcct() {
            return new String(wrappee.Acct);
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
            return new String(wrappee.Side);
        }

        public int getQty() {
            return wrappee.Qty;
        }

        public String getOrdTyp() {
            return new String(wrappee.OrdTyp);
        }

        public float getPx() {
            return wrappee.Px;
        }

        public float getStopPx() {
            return wrappee.StopPx;
        }

        public String getCcy() {
            return new String(wrappee.Ccy);
        }

        public String getTmInForce() {
            return new String(wrappee.TmInForce);
        }

        public String getExpireDt() {
            return new String(wrappee.ExpireDt);
        }

        public float getTrgrPx() {
            return wrappee.TrgrPx;
        }

        public String getDefPayTyp() {
            return new String(wrappee.DefPayTyp);
        }

        public String getSessionDt() {
            return new String(wrappee.SessionDt);
        }

        public String getExpireTm() {
            return new String(wrappee.ExpireTm);
        }

    }

    public static int Initialize() {
        return apiInstance.Initialize("BOS;BOS");
    }

    public static int AddToFilter(String TickersToAdd, boolean Flush) {
        return apiInstance.AddToFilter(TickersToAdd, Flush);
    }

    public static int RemFromFilter(String TickersToRem, boolean Flush) {
        return apiInstance.RemFromFilter(TickersToRem, Flush);
    }

    public interface SetCallbackDummyAPI extends Callback {
        void invoke(NolRecentInfoAPI nolRecentInfoAPI);
    }

    @SuppressWarnings("Convert2Lambda")
    public static int SetCallback(SetCallbackDummyAPI dummy) {
        return apiInstance.SetCallback(new BossaAPIInterface.SetCallbackDummy() {
            @Override
            public void invoke(BossaAPIInterface.NolRecentInfo nolrecentinfo) {
                dummy.invoke(new NolRecentInfoAPI(nolrecentinfo));
            }
        });
    }

    public interface SetCallbackAccountDummyAPI extends Callback {
        void invoke(NolAggrStatementAPI nolaggrstatementAPI);
    }

    // function for setting a callback function (accounts information)
    public static int SetCallbackAccount(SetCallbackAccountDummyAPI dummy) {
        return apiInstance.SetCallbackAccount(nolaggrstatement -> dummy.invoke(new NolAggrStatementAPI(nolaggrstatement)));
    }

    public interface SetCallbackOrderDummyAPI extends Callback {
        void invoke(NolOrderReportAPI nolorderreport);
    }

    // function for setting a callback function (orders information)
    public static int SetCallbackOrder(SetCallbackOrderDummyAPI dummy) {
        return apiInstance.SetCallbackOrder(nolorderreport -> dummy.invoke(new NolOrderReportAPI(nolorderreport)));
    }

    public interface SetCallbackOutlookDummyAPI extends Callback {
        void invoke(String outlook);
    }

    // function for setting a callback function (outlook information)
    public static int SetCallbackOutlook(SetCallbackOutlookDummyAPI dummy) {
        return apiInstance.SetCallbackOutlook(outlook -> dummy.invoke(outlook));
    }

    public static int APIOrderRequest(
            NolOrderRequestAPI nolorderrequest,
            NolOrderReportAPI nolorderreport,
            OrderType Typ) {

        return apiInstance.APIOrderRequest(
                nolorderrequest.wrappee,
                nolorderreport.wrappee,
                Typ);
    }

    public static int SetTradingSess(boolean val) {
        return apiInstance.SetTradingSess(val);
    }

    public interface SetCallbackDelayDummyAPI extends Callback {
        void invoke(float delay);
    }

    public static int SetCallbackDelay(SetCallbackDelayDummyAPI dummy) {
        return apiInstance.SetCallbackDelay(delay -> dummy.invoke(delay));
    }

    public static int GetTickers(NolTickersAPI ptrtickers, TypeofList typeofList, NolTickerAPI in_ticker) {
        if (in_ticker == null)
            return apiInstance.GetTickers(ptrtickers.wrappee, typeofList, null); //in_ticker.wrappee
        else
            return apiInstance.GetTickers(ptrtickers.wrappee, typeofList, in_ticker.wrappee); //in_ticker.wrappee
    }

    public static NolTickersAPI InitListTickers() {
        //tickers = apiInstance.InitListTickers();
        return new NolTickersAPI(apiInstance.InitListTickers());
    }

    public static int ReleaseTickersList(NolTickersAPI ptrtickers) {
        return apiInstance.ReleaseTickersList(ptrtickers.wrappee);
    }

    public interface SetCallbackStatusDummyAPI extends BossaAPIInterface.SetCallbackStatusDummy {
        void invoke(int var);
    }

    public static int SetCallbackStatus(SetCallbackStatusDummyAPI dummy) {
        return apiInstance.SetCallbackStatus(dummy);
    }

    // function for describing errors
    public static String GetResultCodeDesc(int code) {    /* code returned by function */
        return apiInstance.GetResultCodeDesc(code);
    }

    // function for getting the information about verson of dll
    public static String Get_Version() {
        return apiInstance.Get_Version();
    }

    public static int ClearFilter() {
        return apiInstance.ClearFilter();
    }

    public static String Shutdown() {
        return apiInstance.Shutdown();
    }
}
