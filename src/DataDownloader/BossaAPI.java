package datadownloader;

import com.sun.jna.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * BossaAPI TODO dodaj to do modelu
 */
@SuppressWarnings({"WeakerAccess", "Convert2Lambda", "unused"})
public class BossaAPI {
    private static BossaAPI instance = null;
    private static BossaAPIInterface apiInstance = null;

    private BossaAPI() {
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

        Map<String, FunctionMapper> options = new HashMap<>();
        options.put(Library.OPTION_FUNCTION_MAPPER, new FunctionMapper() {
            public String getFunctionName(NativeLibrary library, Method method) {
                return functionNames.get(method.getName());
            }
        });
        apiInstance = (BossaAPIInterface)
                Native.loadLibrary("nolclientapi",
                        BossaAPIInterface.class,
                        options);
    }

    static BossaAPIInterface getApiInstance() {
        if(instance == null) {
            instance = new BossaAPI();
        }
        return apiInstance;
    }

    @SuppressWarnings("UnusedReturnValue")
    public interface BossaAPIInterface extends Library {

        interface OrderType {
            int Undef = -1;
            int NewOrder = 0;
            int ModOrder = 1;
            int DelOrder = 2;
            int StatOrder = 3;
        }

        @SuppressWarnings("unused")
        interface TypeofList {
            int Undef = -1;
            int All = 0;
            int Symbol = 1;
            int ISIN = 2;
            int CFI = 3;
            int MarketCode = 4;
        }

        class NolBidAskTbl extends Structure {
            public static class ByReference extends NolBidAskTbl implements Structure.ByReference { }

            public int depth; // pozycja ofery 1-5
            public int side;  // 1- kupno/ 2- sprzeda¿
            public double price; // cena ofery
            public int size;     // rozmiar oferty
            public int amount;		// iloœæ ofert
        }

        class NolBidAskStr extends Structure {
            public int offersize;
            public NolBidAskTbl.ByReference bidask_table;
        }

        class NolTicker extends Structure {
            public static class ByReference extends NolTicker implements Structure.ByReference { }

            public byte[] Isin = new byte[13];		// International Securities Identifying Number
            public byte[] Name = new byte[21];		// Full name of the ticker
            public byte[] MarketCode = new byte[3];
            public byte[] CFI = new byte[7];
            public byte[] Group = new byte[3];
        }

        class NolTickers extends Structure {
            public static class ByReference extends NolTickers implements Structure.ByReference { }

            public NolTicker.ByReference ptrtickerslist;	// pointer to list of tickers
            public int size;			// size of table of ticker structures
        }

        @SuppressWarnings("unused")
        class NolRecentInfo extends Structure {
            public int BitMask; 		// mask of data
            public NolTicker ticker;	// structure containing information about name and isin
            public double ValoLT;	// a(flag in BitMask) - value of last transaction 1
            public int VoLT;		// b - volume of last transaction 2
            public byte[] ToLT = new byte[9];		// c - time of last transaction 4
            public double Open;		// d - open price 8
            public double High;		// e - current high price 10
            public double Low;		// f - current low price 20
            public double Close;		// g - close price 40
            public double Bid;		// h - the best bid price 80
            public double Ask;		// i - the best ask price  100
            public int BidSize;		// j - size of the best bid 200
            public int AskSize;		// k - size of the best ask 400
            public int TotalVolume;	// l - total volume 800
            public double TotalValue;	// m - total value  1000
            public int OpenInterest;	// n - number of open intrests 2000
            public byte[] Phase = new byte[5];			// o - ticker phase 4000
            public byte[] Status = new byte[5];		// p - ticker status 8000
            public int BidAmount;		// r - amount of bid 10000
            public int AskAmount;		// s - amount of ask 20000
            public double OpenValue;	// t - open value    40000
            public double CloseValue;	// u - close value   80000
            public double ReferPrice;// w - reference price 100000
            public NolBidAskStr offers; // y - offers       200000

            public int Error;			// z - error
        }

        class NolFund extends Structure {
            public static class ByReference extends NolFund implements Structure.ByReference { }

            public byte[] name = new byte[30];         // name of fundation
            public byte[] value = new byte[30];        // value of fundation
        }

        class NolPos extends Structure{
            public static class ByReference extends NolPos implements Structure.ByReference { }

            public NolTicker ticker;         // isin of acctivity
            public int acc110; 	       // amount of acctivity
            public int acc120;
        }

        class NolStatement extends Structure {
            public static class ByReference extends NolStatement implements Structure.ByReference { }

            public byte[] name = new byte[13];              // account
            public byte[] ike = new byte[2];				// is ike ?
            public byte[] type = new byte[2];				// type of account
            public NolFund.ByReference ptrfund;           // pointer to table of fundations
            public int sizefund;               // size of fountations table
            public NolPos.ByReference ptrpos;             // pointer to table of acctivities
            public int sizepos;                // size of acctivities table
        }

        class NolAggrStatement extends Structure {
            public NolStatement.ByReference ptrstate;       // pointer to nolstatments
            public int size;                     // number of accounts
        }

        @SuppressWarnings("unused")
        class NolOrderReport extends Structure {
            public long BitMask;		// BitMask wich data are active
            public int ID;				// bit 0 ID from Library				0001
            public byte[] OrdID = new byte[10]; 		// bit 1 of BitMask  order ID		0002
            public byte[] OrdID2 = new byte[10]; 		// bit 2 secondary order ID			0004
            public byte[] StatReqID = new byte[10];		// bit 3 ID of OrderStatusRequest	0008
            public byte[] ExecID = new byte[10];		// bit 4 transaction ID				0010
            public byte ExecTyp;			// bit 5 type of execution			0020
            public byte Stat;				// bit 6 order satus				0040
            public int RejRsn;				// bit 7 reject reason				0080
            public byte[] Acct = new byte[17];			// bit 8 account					0100
            public NolTicker ticker; 		// bit 9 name/alias ; 10 - isin		0200 0400
            public byte[] Side = new byte[2];			// bit 11							0x0800
            public int Qty;				// bit 12 Quantity order			0x1000
            public byte[] OrdTyp = new byte[2];			// bit 13 order type			0x2000
            public float Px;				// bit 14 price						0x4000
            public float StopPx;			// bit 15 stop price				0x8000
            public byte[] Ccy = new byte[6];			// bit 16 currency										 0x10000
            public byte[] TmInForce = new byte[2];		// bit 17 specifies how long the order remains in effect 0x20000
            public byte[] ExpireDt = new byte[9];		// bit 18 date of order expiration						 0x40000
            public float LastPx;			// bit 19 price of this last fill						 0x80000
            public int LastQty;			// bit 20 Quantity bought/sold on this last fill		0x100000
            public int LeavesQty;		    // bit 21 Quantity open for further execution				0x200000
            public int CumQty;			    // bit 22 Total Quantity									0x400000
            public byte[] TxnTm = new byte[18];			// bit 23 time of execution/order creation				0x800000
            public float Comm;				// bit 24 Commission										0x1000000
            public float NetMny;			// bit 25 Total amount due as the result of the transaction	0x2000000
            public int MinQty;			// bit 26 Minimum quantity of an order to be executed			0x4000000
            public int DisplayQty;		// bit 27 the quantity to be displayed							0x8000000
            public float TrgrPx;			// bit 28 the price at which the triegger should hit		0x10000000
            public byte[] DefPayTyp = new byte[2];		// bit 29 Defferred Payment Type							0x20000000
            public byte BizRejRsn;			// bit 30													0x40000000
            public byte[] Txt = new byte[160];			// bit 31													0x80000000
            public byte[] ExpireTm = new byte[9];		// bit 32 expire time
        }

        @SuppressWarnings("unused")
        class NolOrderRequest extends Structure {
            public int BitMask;
            public byte[] OrigID = new byte[10];	//a	secondary order ID from library		0001
            public byte[] OrdID = new byte[10];	//b	order ID								0002
            public byte[] OrdID2 = new byte[10];	//c	secondary order ID					0004
            public byte[] Acct = new byte[17];	//d	account										0008
            public int MinQty;	//e	minimum quantity of an order to be executed	0010
            public int DisplayQty;	//f	the quantity to be displayed			0020
            public NolTicker ticker;	//g, h ticker structure					    0040 - isin,0080 - name/alias
            public byte[] Side = new byte[2];	//i													0100
            public int Qty;		//j	quantity ordered							0200
            public byte[] OrdTyp = new byte[2];	//k	order type									0400
            public float Px;		//l	price										0800
            public float StopPx;	//m	stop price									1000
            public byte[] Ccy = new byte[6];	//n	curenccy									2000
            public byte[] TmInForce = new byte[2];	//o	specifies how long the order remains in effect 4000
            public byte[] ExpireDt = new byte[9];	//p	date of order expiration				   8000
            public float TrgrPx;	//r	the price at which the trigger should hit	   10000
            public byte[] DefPayTyp = new byte[2];	//s 	Defferred Payment Type					   20000
            public byte[] SessionDt = new byte[9];	//t	 session date								40000
            public byte[] ExpireTm = new byte[9];	//u expire time									80000
        }

        // initialize function
        // AppId - string containing the application information
        int Initialize(String AppId);

        // function for adding ticker(s) to a filter
        // TickersToAdd - string containing isins separated by ';'
        // Flush = true, short/name ticker, Flush = false - ISIN
        int AddToFilter(String TickersToAdd, boolean Flush);

        // function for removing ticker(s) from a filter
        // TickersToRem - string containing isins separated by ';',
        // Flush = true, short/name ticker, Flush = false - ISIN
        int RemFromFilter(String TickersToRem, boolean Flush);

        interface SetCallbackDummy extends Callback {
            void invoke(NolRecentInfo nolrecentinfo);
        }

        // function for setting a callback function
        // void (*ptrcallback) (RecentInfo*) - pointer to callback function
        int SetCallback(SetCallbackDummy dummy);

        // function for clearing a filter
        // Flush - flag used for automatic filter approvment
        int ClearFilter();

        // function for describing errors
        String GetResultCodeDesc(int code); 	/* code returned by function */

        // function for getting the information about verson of dll
        String Get_Version();

        // shutdown function
        String Shutdown();

        interface SetCallbackAccountDummy extends Callback {
            void invoke(NolAggrStatement nolaggrstatement);
        }

        // function for setting a callback function (accounts information)
        int SetCallbackAccount(SetCallbackAccountDummy dummy);

        interface SetCallbackOrderDummy extends Callback {
            void invoke(NolOrderReport nolorderreport);
        }

        // function for setting a callback function (orders information)
        int SetCallbackOrder(SetCallbackOrderDummy dummy);

        interface SetCallbackOutlookDummy extends Callback {
            void invoke(String outlook);
        }

        // function for setting a callback function (outlook information)
        int SetCallbackOutlook(SetCallbackOutlookDummy dummy);

        // function for orders ( modifications, cancels, status request)
        int APIOrderRequest(NolOrderRequest nolorderrequest, NolOrderReport nolorderreport, OrderType Typ);

        int SetTradingSess(boolean val);

        interface SetCallbackDelayDummy extends Callback {
            void invoke(float delay);
        }

        int SetCallbackDelay(SetCallbackDelayDummy dummy);

        int GetTickers(NolTickers ptrtickers, int TypeofList, NolTicker in_ticker);

        // function for instantiante an Tickers's object
        NolTickers InitListTickers();

        int ReleaseTickersList(NolTickers ptrtickers);

        interface SetCallbackStatusDummy extends Callback {
            void invoke(int var);
        }

        int SetCallbackStatus(SetCallbackStatusDummy dummy);
    }
}
