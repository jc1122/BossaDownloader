package datadownloader;

import com.sun.jna.*;
import org.jetbrains.annotations.Contract;

import java.lang.reflect.Method;
import java.util.*;

/**
 * BossaAPI TODO dodaj to do modelu
 */
@SuppressWarnings({"WeakerAccess", "unused", "Convert2MethodRef", "Convert2Lambda"})
enum BossaAPI {
    ;
    private static BossaAPIInterface apiInstance = null;

    static {
        Map<String,String> functionNames = new HashMap<>();

        functionNames.put("APIOrderRequest","_APIOrderRequest@12");
        functionNames.put("AddToFilter","_AddToFilter@8");
        functionNames.put("ClearFilter","_ClearFilter@0");
        functionNames.put("GetResultCodeDesc","_GetResultCodeDesc@4");
        functionNames.put("GetTickers","_GetTickers@12");
        functionNames.put("Get_Version","_Get_Version@0");
        functionNames.put("InitListTickers","_InitListTickers@0");
        functionNames.put("Initialize","_Initialize@4");
        functionNames.put("ReleaseTickersList","_ReleaseTickersList@4");
        functionNames.put("RemFromFilter","_RemFromFilter@8");
        functionNames.put("SetCallback","_SetCallback@4");
        functionNames.put("SetCallbackAccount","_SetCallbackAccount@4");
        functionNames.put("SetCallbackDelay","_SetCallbackDelay@4");
        functionNames.put("SetCallbackOrder","_SetCallbackOrder@4");
        functionNames.put("SetCallbackOutlook","_SetCallbackOutlook@4");
        functionNames.put("SetCallbackStatus","_SetCallbackStatus@4");
        functionNames.put("SetTradingSess","_SetTradingSess@4");
        functionNames.put("Shutdown","_Shutdown@0");

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

    @Contract(pure = true)
    static BossaAPIInterface getApiInstance() {
        return apiInstance;
    }

    static class NolBidAskTblAPI {
        private BossaAPIInterface.NolBidAskTbl nolBidAskTbl;

        public NolBidAskTblAPI(BossaAPIInterface.NolBidAskTbl nolBidAskTbl) {
            this.nolBidAskTbl = nolBidAskTbl;
        }

        public int getDepth() {
            return nolBidAskTbl.depth;
        }

        public int getSide() {
            return nolBidAskTbl.side;
        }

        public double getPrice() {
            return nolBidAskTbl.price;
        }

        public int getSize() {
            return nolBidAskTbl.size;
        }

        public int getAmount() {
            return nolBidAskTbl.amount;
        }

    }

    static class NolBidAskStrAPI {
        private BossaAPIInterface.NolBidAskStr nolBidAskStr;

        public NolBidAskStrAPI(BossaAPIInterface.NolBidAskStr nolBidAskStr) {
            this.nolBidAskStr = nolBidAskStr;
        }

        public int getOffersize() {
            return nolBidAskStr.offersize;
        }

        public NolBidAskTblAPI getBidask_table() {
            return new NolBidAskTblAPI(nolBidAskStr.bidask_table);
        }
    }

    static class NolTickerAPI {
        private BossaAPIInterface.NolTicker nolTicker;

        public NolTickerAPI(BossaAPIInterface.NolTicker nolTicker) {
            this.nolTicker = nolTicker;
        }

        public String getIsin() {
            return new String(nolTicker.Isin);
        }

        public String getName() {
            return new String(nolTicker.Name);
        }

        public String getMarketCode() {
            return new String(nolTicker.MarketCode);
        }

        public String getCFI() {
            return new String(nolTicker.CFI);
        }

        public String getGroup() {
            return new String(nolTicker.Group);
        }
    }

    static class NolTickersAPI {
        private BossaAPIInterface.NolTickers nolTickers;

        public NolTickersAPI(BossaAPIInterface.NolTickers nolTickers) {
            this.nolTickers = nolTickers;
        }

        public List<NolTickerAPI> getTickersList() {
            List nolTickersList = Arrays.asList(nolTickers.ptrtickerslist.toArray(nolTickers.size));
            List<NolTickerAPI> nolTickersAPIList = new ArrayList<>(nolTickers.size);

            for (Object ticker : nolTickersList) {
                nolTickersAPIList.add(new NolTickerAPI((BossaAPIInterface.NolTicker) ticker));
            }
            return nolTickersAPIList;
        }
    }

    static class NolRecentInfoAPI {
        private BossaAPIInterface.NolRecentInfo nolRecentInfo;

        public NolRecentInfoAPI(BossaAPIInterface.NolRecentInfo nolRecentInfo) {
            this.nolRecentInfo = nolRecentInfo;
        }

        public int getBitMask() {
            return nolRecentInfo.BitMask;
        }

        public NolTickerAPI getTicker() {
            return new NolTickerAPI(nolRecentInfo.ticker);
        }

        public double getValoLT() {
            return nolRecentInfo.ValoLT;
        }

        public int getVoLT() {
            return nolRecentInfo.VoLT;
        }

        public String getToLT() {
            return new String(nolRecentInfo.ToLT);
        }

        public double getOpen() {
            return nolRecentInfo.Open;
        }

        public double getHigh() {
            return nolRecentInfo.High;
        }

        public double getLow() {
            return nolRecentInfo.Low;
        }

        public double getClose() {
            return nolRecentInfo.Close;
        }

        public double getBid() {
            return nolRecentInfo.Bid;
        }

        public double getAsk() {
            return nolRecentInfo.Ask;
        }

        public int getBidSize() {
            return nolRecentInfo.BidSize;
        }

        public int getAskSize() {
            return nolRecentInfo.AskSize;
        }

        public int getTotalVolume() {
            return nolRecentInfo.TotalVolume;
        }

        public double getTotalValue() {
            return nolRecentInfo.TotalValue;
        }

        public int getOpenInterest() {
            return nolRecentInfo.OpenInterest;
        }

        public String getPhase() {
            return new String(nolRecentInfo.Phase);
        }

        public String getStatus() {
            return new String(nolRecentInfo.Status);
        }

        public int getBidAmount() {
            return nolRecentInfo.BidAmount;
        }

        public int getAskAmount() {
            return nolRecentInfo.AskAmount;
        }

        public double getOpenValue() {
            return nolRecentInfo.OpenValue;
        }

        public double getCloseValue() {
            return nolRecentInfo.CloseValue;
        }

        public double getReferPrice() {
            return nolRecentInfo.ReferPrice;
        }

        public NolBidAskStrAPI getOffers() {
            return new NolBidAskStrAPI(nolRecentInfo.offers);
        }

        public int getError() {
            return nolRecentInfo.Error;
        }
    }

    static class NolFundAPI {
        private BossaAPIInterface.NolFund nolFund;

        public NolFundAPI(BossaAPIInterface.NolFund nolFund) {
            this.nolFund = nolFund;
        }

        public String getName() {
            return new String(nolFund.name);
        }

        public String getValue() {
            return new String(nolFund.value);
        }

    }

    static class NolPosAPI {
        private BossaAPIInterface.NolPos nolPos;

        public NolPosAPI(BossaAPIInterface.NolPos nolPos) {
            this.nolPos = nolPos;
        }

        public NolTickerAPI getTicker() {
            return new NolTickerAPI(nolPos.ticker);
        }

        public int getAcc110() {
            return nolPos.acc110;
        }

        public int getAcc120() {
            return nolPos.acc120;
        }
    }

    static class NolStatementAPI {
        private BossaAPIInterface.NolStatement nolStatement;

        public NolStatementAPI(BossaAPIInterface.NolStatement nolStatement) {
            this.nolStatement = nolStatement;
        }

        public String getName() {
            return new String(nolStatement.name);
        }

        public String getIke() {
            return new String(nolStatement.ike);
        }

        public String getType() {
            return new String(nolStatement.type);
        }

        public List<NolFundAPI> getFund() {
            List nolFundList = Arrays.asList(nolStatement.ptrfund.toArray(nolStatement.sizefund));
            List<NolFundAPI> NolFundAPIList = new ArrayList<>(nolStatement.sizefund);

            for (Object fund : nolFundList) {
                NolFundAPIList.add(new NolFundAPI((BossaAPIInterface.NolFund) fund));
            }
            return NolFundAPIList;
        }

        public List<NolPosAPI> getPositions() {
            List nolPosList = Arrays.asList(nolStatement.ptrpos.toArray(nolStatement.sizepos));
            List<NolPosAPI> NolPosAPIList = new ArrayList<>(nolStatement.sizepos);

            for (Object position : nolPosList) {
                NolPosAPIList.add(new NolPosAPI((BossaAPIInterface.NolPos) position));
            }
            return NolPosAPIList;
        }
    }

    static class NolAggrStatementAPI {
        private BossaAPIInterface.NolAggrStatement nolAggrStatement;

        public NolAggrStatementAPI(BossaAPIInterface.NolAggrStatement nolAggrStatement) {
            this.nolAggrStatement = nolAggrStatement;
        }

        public List<NolStatementAPI> getStatement() {
            List nolStatementList = Arrays.asList(nolAggrStatement.ptrstate.toArray(nolAggrStatement.size));
            List<NolStatementAPI> nolStatementAPIList = new ArrayList<>(nolAggrStatement.size);

            for (Object statement : nolStatementList) {
                nolStatementAPIList.add(new NolStatementAPI((BossaAPIInterface.NolStatement) statement));
            }
            return nolStatementAPIList;
        }
    }

    static class NolOrderReportAPI {
        private BossaAPIInterface.NolOrderReport nolOrderReport;

        public NolOrderReportAPI(BossaAPIInterface.NolOrderReport nolOrderReport) {
            this.nolOrderReport = nolOrderReport;
        }

        public long getBitMask() {
            return nolOrderReport.BitMask;
        }

        public int getID() {
            return nolOrderReport.ID;
        }

        public String getOrdID() {
            return new String(nolOrderReport.OrdID);
        }

        public String getOrdID2() {
            return new String(nolOrderReport.OrdID2);
        }

        public String getStatReqID() {
            return new String(nolOrderReport.StatReqID);
        }

        public String getExecID() {
            return new String(nolOrderReport.ExecID);
        }

        public String getExecTyp() {
            return Byte.toString(nolOrderReport.ExecTyp);
        }

        public String getStat() {
            return Byte.toString(nolOrderReport.Stat);
        }

        public int getRejRsn() {
            return nolOrderReport.RejRsn;
        }

        public String getAcct() {
            return new String(nolOrderReport.Acct);
        }

        public NolTickerAPI getTicker() {
            return new NolTickerAPI(nolOrderReport.ticker);
        }

        public String getSide() {
            return new String(nolOrderReport.Side);
        }

        public int getQty() {
            return nolOrderReport.Qty;
        }

        public String getOrdTyp() {
            return new String(nolOrderReport.OrdTyp);
        }

        public float getPx() {
            return nolOrderReport.Px;
        }

        public float getStopPx() {
            return nolOrderReport.StopPx;
        }

        public String getCcy() {
            return new String(nolOrderReport.Ccy);
        }

        public String getTmInForce() {
            return new String(nolOrderReport.TmInForce);
        }

        public String getExpireDt() {
            return new String(nolOrderReport.ExpireDt);
        }

        public float getLastPx() {
            return nolOrderReport.LastPx;
        }

        public int getLastQty() {
            return nolOrderReport.LastQty;
        }

        public int getLeavesQty() {
            return nolOrderReport.LeavesQty;
        }

        public int getCumQty() {
            return nolOrderReport.CumQty;
        }


        public String getTxnTm() {
            return new String(nolOrderReport.TxnTm);
        }

        public float getComm() {
            return nolOrderReport.Comm;
        }

        public float getNetMny() {
            return nolOrderReport.NetMny;
        }

        public int getMinQty() {
            return nolOrderReport.MinQty;
        }


        public int getDisplayQty() {
            return nolOrderReport.DisplayQty;
        }

        public float getTrgrPx() {
            return nolOrderReport.TrgrPx;
        }


        public String getDefPayTyp() {
            return new String(nolOrderReport.DefPayTyp);
        }

        public String getBizRejRsn() {
            return Byte.toString(nolOrderReport.BizRejRsn);
        }


        public String getTxt() {
            return new String(nolOrderReport.Txt);
        }


        public String getExpireTm() {
            return new String(nolOrderReport.ExpireTm);
        }

    }

    static class NolOrderRequestAPI {
        private BossaAPIInterface.NolOrderRequest nolOrderRequest;

        public NolOrderRequestAPI(BossaAPIInterface.NolOrderRequest nolOrderRequest) {
            this.nolOrderRequest = nolOrderRequest;
        }

        public int getBitMask() {
            return nolOrderRequest.BitMask;
        }

        public String getOrigID() {
            return new String(nolOrderRequest.OrigID);
        }

        public String getOrdID() {
            return new String(nolOrderRequest.OrdID);
        }

        public String getOrdID2() {
            return new String(nolOrderRequest.OrdID2);
        }

        public String getAcct() {
            return new String(nolOrderRequest.Acct);
        }

        public int getMinQty() {
            return nolOrderRequest.MinQty;
        }

        public int getDisplayQty() {
            return nolOrderRequest.DisplayQty;
        }

        public NolTickerAPI getTicker() {
            return new NolTickerAPI(nolOrderRequest.ticker);
        }

        public String getSide() {
            return new String(nolOrderRequest.Side);
        }

        public int getQty() {
            return nolOrderRequest.Qty;
        }

        public String getOrdTyp() {
            return new String(nolOrderRequest.OrdTyp);
        }

        public float getPx() {
            return nolOrderRequest.Px;
        }

        public float getStopPx() {
            return nolOrderRequest.StopPx;
        }

        public String getCcy() {
            return new String(nolOrderRequest.Ccy);
        }

        public String getTmInForce() {
            return new String(nolOrderRequest.TmInForce);
        }

        public String getExpireDt() {
            return new String(nolOrderRequest.ExpireDt);
        }

        public float getTrgrPx() {
            return nolOrderRequest.TrgrPx;
        }

        public String getDefPayTyp() {
            return new String(nolOrderRequest.DefPayTyp);
        }

        public String getSessionDt() {
            return new String(nolOrderRequest.SessionDt);
        }

        public String getExpireTm() {
            return new String(nolOrderRequest.ExpireTm);
        }
    }

    static int Initialize() {
        return apiInstance.Initialize("BOS;BOS");
    }

    static int AddToFilter(String TickersToAdd, boolean Flush) {
        return apiInstance.AddToFilter(TickersToAdd, Flush);
    }

    static int RemFromFilter(String TickersToRem, boolean Flush) {
        return apiInstance.RemFromFilter(TickersToRem, Flush);
    }

    interface SetCallbackDummyAPI extends Callback {
        void invoke(NolRecentInfoAPI nolRecentInfoAPI);
    }

    @SuppressWarnings("Convert2Lambda")
    static int SetCallback(SetCallbackDummyAPI dummy) {
        return apiInstance.SetCallback(new BossaAPIInterface.SetCallbackDummy() {
            @Override
            public void invoke(BossaAPIInterface.NolRecentInfo nolrecentinfo) {
                dummy.invoke(new NolRecentInfoAPI(nolrecentinfo));
            }
        });
    }

    interface SetCallbackAccountDummyAPI extends Callback {
        void invoke(NolAggrStatementAPI nolaggrstatementAPI);
    }

    // function for setting a callback function (accounts information)
    static int SetCallbackAccount(SetCallbackAccountDummyAPI dummy) {
        return apiInstance.SetCallbackAccount(nolaggrstatement -> dummy.invoke(new NolAggrStatementAPI(nolaggrstatement)));
    }

    interface SetCallbackOrderDummyAPI extends Callback {
        void invoke(NolOrderReportAPI nolorderreport);
    }

    // function for setting a callback function (orders information)
    static int SetCallbackOrder(SetCallbackOrderDummyAPI dummy) {
        return apiInstance.SetCallbackOrder(nolorderreport -> dummy.invoke(new NolOrderReportAPI(nolorderreport)));
    }

    interface SetCallbackOutlookDummyAPI extends Callback {
        void invoke(String outlook);
    }

    // function for setting a callback function (outlook information)
    static int SetCallbackOutlook(SetCallbackOutlookDummyAPI dummy) {
        return apiInstance.SetCallbackOutlook(outlook -> dummy.invoke(outlook));
    }

    static int APIOrderRequest(
            NolOrderRequestAPI nolorderrequest,
            NolOrderReportAPI nolorderreport,
            BossaAPIInterface.OrderType Typ) {

        return apiInstance.APIOrderRequest(
                nolorderrequest.nolOrderRequest,
                nolorderreport.nolOrderReport,
                Typ);
    }

    static int SetTradingSess(boolean val) {
        return apiInstance.SetTradingSess(val);
    }

    interface SetCallbackDelayDummyAPI extends Callback {
        void invoke(float delay);
    }

    static int SetCallbackDelay(SetCallbackDelayDummyAPI dummy) {
        return apiInstance.SetCallbackDelay(delay -> dummy.invoke(delay));
    }

    static int GetTickers(NolTickersAPI ptrtickers, BossaAPIInterface.TypeofList typeofList, NolTickerAPI in_ticker) {
        return apiInstance.GetTickers(ptrtickers.nolTickers, typeofList, in_ticker.nolTicker);
    }

    static NolTickersAPI InitListTickers() {
        return new NolTickersAPI(apiInstance.InitListTickers());
    }

    static int ReleaseTickersList(NolTickersAPI ptrtickers) {
        return apiInstance.ReleaseTickersList(ptrtickers.nolTickers);
    }

    interface SetCallbackStatusDummyAPI extends BossaAPIInterface.SetCallbackStatusDummy {
        void invoke(int var);
    }

    static int SetCallbackStatus(SetCallbackStatusDummyAPI dummy) {
        return apiInstance.SetCallbackStatus(dummy);
    }
    //TODO finish wrapping
}
