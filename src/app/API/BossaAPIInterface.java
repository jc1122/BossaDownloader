package app.API;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Structure;

/**
 * JNA interface for BossaAPI native dll. This is a direct mapping of native API to Java.
 * To use functionality, use {@link BossaAPI}.
 */
@SuppressWarnings({"WeakerAccess", "Convert2Lambda", "unused", "UnusedReturnValue"})
interface BossaAPIInterface extends Library {

    class NolBidAskTbl extends Structure {

        public static class ByReference extends NolBidAskTbl implements Structure.ByReference {
        }

        public int depth; // pozycja ofery 1-5
        public int side;  // 1- kupno/ 2- sprzeda¿
        public double price; // cena ofery
        public int size;     // rozmiar oferty
        public int amount;        // iloœæ ofert
    }

    class NolBidAskStr extends Structure {
        public int offersize;
        public NolBidAskTbl.ByReference bidask_table;
    }

    class NolTicker extends Structure {

        public static class ByReference extends NolTicker implements Structure.ByReference {
        }

        public byte[] Isin = new byte[13];        // International Securities Identifying Number
        public byte[] Name = new byte[21];        // Full name of the ticker
        public byte[] MarketCode = new byte[3];
        public byte[] CFI = new byte[7];
        public byte[] Group = new byte[3];
    }

    class NolTickers extends Structure {

        public static class ByReference extends NolTickers implements Structure.ByReference {
        }

        public NolTicker.ByReference ptrtickerslist;    // pointer to list of tickers
        public int size;            // size of table of ticker structures
    }

    @SuppressWarnings("unused")
    class NolRecentInfo extends Structure {
        public int BitMask;        // mask of data
        public NolTicker ticker;    // structure containing information about name and isin
        public double ValoLT;    // a(flag in BitMask) - value of last transaction 1
        public int VoLT;        // b - volume of last transaction 2
        public byte[] ToLT = new byte[9];        // c - time of last transaction 4
        public double Open;        // d - open price 8
        public double High;        // e - current high price 10
        public double Low;        // f - current low price 20
        public double Close;        // g - close price 40
        public double Bid;        // h - the best bid price 80
        public double Ask;        // i - the best ask price  100
        public int BidSize;        // j - size of the best bid 200
        public int AskSize;        // k - size of the best ask 400
        public int TotalVolume;    // l - total volume 800
        public double TotalValue;    // m - total value  1000
        public int OpenInterest;    // n - number of open intrests 2000
        public byte[] Phase = new byte[5];            // o - ticker phase 4000
        public byte[] Status = new byte[5];        // p - ticker status 8000
        public int BidAmount;        // r - amount of bid 10000
        public int AskAmount;        // s - amount of ask 20000
        public double OpenValue;    // t - open value    40000
        public double CloseValue;    // u - close value   80000
        public double ReferPrice;// w - reference price 100000
        public NolBidAskStr offers; // y - offers       200000

        public int Error;            // z - error

    }

    class NolFund extends Structure {
        public static class ByReference extends NolFund implements Structure.ByReference {
        }

        public byte[] name = new byte[30];         // name of fundation
        public byte[] value = new byte[30];        // value of fundation
    }

    class NolPos extends Structure {
        public static class ByReference extends NolPos implements Structure.ByReference {
        }

        public NolTicker ticker;         // isin of acctivity
        public int acc110;           // amount of acctivity
        public int acc120;
    }

    class NolStatement extends Structure {
        public static class ByReference extends NolStatement implements Structure.ByReference {
        }

        public byte[] name = new byte[13];//new String(new byte[13]);// = new byte[13];              // account
        public byte[] ike = new byte[2];                // is ike ?
        public byte[] type = new byte[2];                // type of account
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
        public long BitMask;        // BitMask wich data are active
        public int ID;                // bit 0 ID from Library				0001
        public byte[] OrdID = new byte[10];        // bit 1 of BitMask  order ID		0002
        public byte[] OrdID2 = new byte[10];        // bit 2 secondary order ID			0004
        public byte[] StatReqID = new byte[10];        // bit 3 ID of OrderStatusRequest	0008
        public byte[] ExecID = new byte[10];        // bit 4 transaction ID				0010
        public byte ExecTyp;            // bit 5 type of execution			0020
        public byte Stat;                // bit 6 order satus				0040
        public int RejRsn;                // bit 7 reject reason				0080
        public byte[] Acct = new byte[17];            // bit 8 account					0100
        public NolTicker ticker;        // bit 9 name/alias ; 10 - isin		0200 0400
        public byte[] Side = new byte[2];            // bit 11							0x0800
        public int Qty;                // bit 12 Quantity order			0x1000
        public byte[] OrdTyp = new byte[2];            // bit 13 order type			0x2000
        public float Px;                // bit 14 price						0x4000
        public float StopPx;            // bit 15 stop price				0x8000
        public byte[] Ccy = new byte[6];            // bit 16 currency										 0x10000
        public byte[] TmInForce = new byte[2];        // bit 17 specifies how long the order remains in effect 0x20000
        public byte[] ExpireDt = new byte[9];        // bit 18 date of order expiration						 0x40000
        public float LastPx;            // bit 19 price of this last fill						 0x80000
        public int LastQty;            // bit 20 Quantity bought/sold on this last fill		0x100000
        public int LeavesQty;            // bit 21 Quantity open for further execution				0x200000
        public int CumQty;                // bit 22 Total Quantity									0x400000
        public byte[] TxnTm = new byte[18];            // bit 23 time of execution/order creation				0x800000
        public float Comm;                // bit 24 Commission										0x1000000
        public float NetMny;            // bit 25 Total amount due as the result of the transaction	0x2000000
        public int MinQty;            // bit 26 Minimum quantity of an order to be executed			0x4000000
        public int DisplayQty;        // bit 27 the quantity to be displayed							0x8000000
        public float TrgrPx;            // bit 28 the price at which the triegger should hit		0x10000000
        public byte[] DefPayTyp = new byte[2];        // bit 29 Defferred Payment Type							0x20000000
        public byte BizRejRsn;            // bit 30													0x40000000
        public byte[] Txt = new byte[160];            // bit 31													0x80000000
        public byte[] ExpireTm = new byte[9];        // bit 32 expire time

    }

    @SuppressWarnings("unused")
    class NolOrderRequest extends Structure {
        public int BitMask;
        public byte[] OrigID = new byte[10];    //a	secondary order ID from library		0001
        public byte[] OrdID = new byte[10];    //b	order ID								0002
        public byte[] OrdID2 = new byte[10];    //c	secondary order ID					0004
        public byte[] Acct = new byte[17];    //d	account										0008
        public int MinQty;    //e	minimum quantity of an order to be executed	0010
        public int DisplayQty;    //f	the quantity to be displayed			0020
        public NolTicker ticker;    //g, h ticker structure					    0040 - isin,0080 - name/alias
        public byte[] Side = new byte[2];    //i													0100
        public int Qty;        //j	quantity ordered							0200
        public byte[] OrdTyp = new byte[2];    //k	order type									0400
        public float Px;        //l	price										0800
        public float StopPx;    //m	stop price									1000
        public byte[] Ccy = new byte[6];    //n	curenccy									2000
        public byte[] TmInForce = new byte[2];    //o	specifies how long the order remains in effect 4000
        public byte[] ExpireDt = new byte[9];    //p	date of order expiration				   8000
        public float TrgrPx;    //r	the price at which the trigger should hit	   10000
        public byte[] DefPayTyp = new byte[2];    //s 	Defferred Payment Type					   20000
        public byte[] SessionDt = new byte[9];    //t	 session date								40000
        public byte[] ExpireTm = new byte[9];    //u expire time									80000
    }

    /**
     *  Initializes library, should be called before calling any other methods. However, to check if NOL3 is running,
     *  you may run {@link BossaAPIInterface#SetCallbackStatus(SetCallbackStatusDummy)} first and check status.
     *  Will fail if called, when <a href="http://bossa.pl/oferta/internet/pomoc/nol">NOL3</a>
     *  is not running.
     * @param AppId login and pass separated by semocolon, currently only "BOS;BOS" is accepted
     * @return error code, see: {@link BossaAPIInterface#GetResultCodeDesc(int)}
     */
    int Initialize(String AppId);

    /**
     * Starts tracking quotes of given tickers. Quotes will be sent to the function set by
     * {@link BossaAPIInterface#SetCallback}. If the filter is not empty, then API will crash.
     * Use {@link BossaAPIInterface#ClearFilter()} to clear filter.
     * @param TickersToAdd isins or names of tickers separated by semicolon
     * @param Flush {@code True} for names, {@code False} for ISINs
     * @return error code, see: {@link BossaAPIInterface#GetResultCodeDesc(int)}
     */
    int AddToFilter(String TickersToAdd, boolean Flush);

    /**
     * Replaces tickers currently in filter with given tickers and starts tracking quotes.
     * @see BossaAPIInterface#ClearFilter()
     * @see BossaAPIInterface#AddToFilter(String, boolean)
     * @param TickersToRem
     * @param Flush
     * @return error code, see: {@link BossaAPIInterface#GetResultCodeDesc(int)}
     */
    int RemFromFilter(String TickersToRem, boolean Flush);

    /**
     * Substitute of pointer to function {@link BossaAPIInterface#SetCallback(SetCallbackDummy)}.
     *
     */
    interface SetCallbackDummy extends Callback {
        /**
         * Implement the functionality for callback function here.
         * @param nolrecentinfo
         */
        void invoke(NolRecentInfo nolrecentinfo);
    }

    /**
     * Function for setting a callback function for quotes.
     * {@code void (*ptrcallback) (RecentInfo*)} - pointer to callback function. Use {@link SetCallbackDummy} instead.
     * @param dummy
     * @return error code, see: {@link BossaAPIInterface#GetResultCodeDesc(int)}
     */
    int SetCallback(SetCallbackDummy dummy);

    /**
     * Stop tracking quotes of previously selected tickers. Only tickers which are in filter will receive quotes updates.
     * Clearing filter effectively stops updates of all quotes.
     * @return error code, see: {@link BossaAPIInterface#GetResultCodeDesc(int)}
     */
    int ClearFilter();

    /**
     * Translates error code to message. See <a href="http://bossa.pl/notowania/narzedzia/bossapi/"> documentation</a>
     * for a list of available messages.
     * @param code non negative value is a success, negative is an error
     * @return
     */
    String GetResultCodeDesc(int code); 	/* code returned by function */

    /**
     * Returns version of API native dll.
     * @return version of API native dll
     */
    String Get_Version();

    /**
     * Clean up memory after finishing work with API native dll.
     * @return error code, see: {@link BossaAPIInterface#GetResultCodeDesc(int)}
     */
    int Shutdown();
    /**
     * Substitute of pointer to function {@link BossaAPIInterface#SetCallbackAccount(SetCallbackAccountDummy)}.
     *
     */
    interface SetCallbackAccountDummy extends Callback {
        /**
         * Implement the functionality for callback function here.
         * @param nolaggrstatement
         */
        void invoke(NolAggrStatement nolaggrstatement);
    }
    /**
     * Function for setting a callback function updating information about accounts.
     * @param dummy
     * @return error code, see: {@link BossaAPIInterface#GetResultCodeDesc(int)}
     */
    // function for setting a callback function (accounts information)
    int SetCallbackAccount(SetCallbackAccountDummy dummy);
    /**
     * Substitute of pointer to function {@link BossaAPIInterface#SetCallbackOrder(SetCallbackOrderDummy)}.
     *
     */
    interface SetCallbackOrderDummy extends Callback {
        /**
         * Implement the functionality for callback function here.
         * @param nolorderreport
         */
        void invoke(NolOrderReport nolorderreport);
    }
    /**
     * Function for setting a callback function updating information about pending orders.
     * @param dummy
     * @return error code, see: {@link BossaAPIInterface#GetResultCodeDesc(int)}
     */
    // function for setting a callback function (orders information)
    int SetCallbackOrder(SetCallbackOrderDummy dummy);

    /**
     * Substitute of pointer to function {@link BossaAPIInterface#SetCallbackOutlook(SetCallbackOutlookDummy)}.
     *
     */
    interface SetCallbackOutlookDummy extends Callback {
        /**
         * Implement the functionality for callback function here.
         * @param outlook
         */
        void invoke(String outlook);
    }
    /**
     * Function for setting a callback function updating information about messages from NOL
     * @param dummy
     * @return error code, see: {@link BossaAPIInterface#GetResultCodeDesc(int)}
     */
    int SetCallbackOutlook(SetCallbackOutlookDummy dummy);

    /**
     * Handles modification, cancels and status of pending orders.
     * //TODO check and describe functionality
     * @param nolorderrequest
     * @param nolorderreport
     * @param Typ
     * @return
     */
    // function for orders ( modifications, cancels, status request)
    int APIOrderRequest(NolOrderRequest nolorderrequest, NolOrderReport nolorderreport, OrderType Typ);

    /**
     * Enables request for update of trading session status and phase. Should be called before tickers are added to filter.
     * @param val True to receive updates, False to stop receiving updates
     * @return
     */
    int SetTradingSess(boolean val);

    /**
     * Substitute of pointer to function {@link BossaAPIInterface#SetCallbackDelay(SetCallbackDelayDummy)}.
     *
     */
    interface SetCallbackDelayDummy extends Callback {
        /**
         * Implement the functionality for callback function here.
         * @param delay
         */
        void invoke(float delay);
    }
    /**
     * Function for setting a callback function receiving delay to NOL server.
     * @param dummy
     * @return error code, see: {@link BossaAPIInterface#GetResultCodeDesc(int)}
     */
    int SetCallbackDelay(SetCallbackDelayDummy dummy);

    /**
     * Returns a list of tickers of given type. Use any {@code in_ticker} with {@link TypeOfList#UNDEF_LIST} or
     * {@link TypeOfList#ALL} to get all tickers. Use a valid ticker with other possible {@link TypeOfList} values to
     * get tickers matching given property. If the selected property of {@code in_ticker} is null or empty string, then
     * NOL will crash! After finished work with list of tickers use {@link BossaAPIInterface#ReleaseTickersList(NolTickers)}
     * to release memory.
     * @param ptrtickers pointer to list of tickers, see: {@link BossaAPIInterface#InitListTickers()}
     * @param typeOfList see: {@link TypeOfList}
     * @param in_ticker null or a valid ticker
     * @return
     */
    int GetTickers(NolTickers ptrtickers, TypeOfList typeOfList, NolTicker in_ticker);

    /**
     * Allocates memory for tickers.
     * @see BossaAPIInterface#GetTickers(NolTickers, TypeOfList, NolTicker)
     * @return instantiated tickers list
     */
    NolTickers InitListTickers();

    /**
     * Releases memory of pointer to tickers.
     * @param ptrtickers
     * @return  error code, see: {@link BossaAPIInterface#GetResultCodeDesc(int)}
     */
    int ReleaseTickersList(NolTickers ptrtickers);
    /**
     * Substitute of pointer to function {@link BossaAPIInterface#SetCallbackStatus(SetCallbackStatusDummy)}
     *
     */
    interface SetCallbackStatusDummy extends Callback {
        /**
         * Implement the functionality for callback function here.
         * @param var
         */
        void invoke(Nol3State var);
    }
    /**
     * Function for setting a callback function updating status of session
     * @param dummy
     * @return error code, see: {@link BossaAPIInterface#GetResultCodeDesc(int)}
     */
    int SetCallbackStatus(SetCallbackStatusDummy dummy);

}
