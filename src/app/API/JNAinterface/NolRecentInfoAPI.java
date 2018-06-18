package app.API.JNAinterface;

//import app.API.JNAinterface.BossaAPI;
import app.API.PublicAPI.Ticker;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores market info about given ticker. <br>
 * Data returned in this class is shattered. Not all fields are valid in each instance!
 * To check if a field is valid, use {@link NolRecentInfoAPI#getBitMask()}. <br><br>
 * The info includes: <br>
 * Offers - current market depth (up to 5 levels), see: {@link NolBidAskStrAPI} <br>
 * Ticker - description of current instrument, see: {@link NolTickerAPI} <br>
 * ToLT - time of last transaction as {@link String}, see: {@link NolRecentInfoAPI#getToLT()} <br>
 * Phase - session phase //TODO check which values are possible <br>
 * Status - session status //TODO check which values are possible <br>
 * ValoLT - value of last transaction, see {@link NolRecentInfoAPI#getValoLT()} <br>
 * VoLT - volume of last transaction, see {@link NolRecentInfoAPI#getVoLT()} <br>
 * Open - price at session open, see {@link NolRecentInfoAPI#getOpen()}<br>
 * High - current session highest price, see {@link NolRecentInfoAPI#getHigh()}<br>
 * Low - current session lowest price, see {@link NolRecentInfoAPI#getLow()}<br>
 * Close - price st close of current session, see {@link NolRecentInfoAPI#getClose()} <br>
 * Bid - current best bid price, see {@link NolRecentInfoAPI#getBid()} <br>
 * Ask - current best ask price, see {@link NolRecentInfoAPI#getAsk()} <br>
 * BidSize - sum of number of shares at current best bid, see {@link NolRecentInfoAPI#getBidSize()}<br>
 * AskSize - sum of number of shares at current best ask, see {@link NolRecentInfoAPI#getAskSize()}<br>
 * TotalVolume - cumulative volume of current session, see {@link NolRecentInfoAPI#getTotalValue()}<br>
 * TotalValue - cumulative turnover of current session, see {@link NolRecentInfoAPI#getTotalValue()}<br>
 * OpenInterest - applicable only to derivates, number of open positions, see {@link NolRecentInfoAPI#getOpenInterest()}<br>
 * BidAmount - amount of offers at current best bid, see {@link NolRecentInfoAPI#getAskAmount()} <br>
 * AskAmount - amount of offers at current best ask, see {@link NolRecentInfoAPI#getAskAmount()} <br>
 * OpenValue - turnover at session open, see {@link NolRecentInfoAPI#getOpenValue()}<br>
 * CloseValue - turnover at session close, see {@link NolRecentInfoAPI#getCloseValue()}<br>
 * ReferPrice - reference price, typically close price of previous session, see {@link NolRecentInfoAPI#getReferPrice()}<br>
 * Error - error code of this message, for validity check only, see {@link NolRecentInfoAPI#getError()}<br>
 */
public final class NolRecentInfoAPI extends BossaAPIClassWrapper<NolRecentInfoAPI, BossaAPIInterface.NolRecentInfo> {

    private final NolBidAskStrAPI offers;
    private final Ticker ticker;
    private final String bitMask;
    private final String toLT;
    private final String phase;
    private final String status;
    private final Map<String, Boolean> fieldInMessage;

    NolRecentInfoAPI(BossaAPIInterface.NolRecentInfo nolRecentInfo) {
        super(nolRecentInfo);
        logger.entering(NolRecentInfoAPI.class.getName(), "Constructor");
        offers = new NolBidAskStrAPI(wrappee.offers);
        ticker = new NolTickerAPI(wrappee.ticker);

        toLT = new String(wrappee.ToLT).trim();
        phase = new String(wrappee.Phase).trim();
        status = new String(wrappee.Status).trim();
        fieldInMessage = new HashMap<>();

        String tmpBitMask = Integer.toBinaryString(wrappee.BitMask);
        //fill with zeros to get 32 bits
        tmpBitMask = new String(new char[32 - tmpBitMask.length()]).replace("\0", "0") + tmpBitMask;
        //reverse to get youngest bits at start
        bitMask = new StringBuilder(tmpBitMask).reverse().toString();


        if (this.getError() < 0) {
            int message = this.getError();//BossaAPI.GetResultCodeDesc(this.getError()); TODO try a better way to decouple this from BossaAPI
            IllegalStateException e = new IllegalStateException(Integer.toString(message));
            logger.finer(e.getMessage());
            throw e;
        }
        fillBitMask();
    }

    private void fillBitMask() {
        char[] arrayBitMask = bitMask.toCharArray();
        fieldInMessage.put("ValoLT", arrayBitMask[0] == '1');
        fieldInMessage.put("VoLT", arrayBitMask[1] == '1');
        fieldInMessage.put("ToLT", arrayBitMask[2] == '1');
        fieldInMessage.put("Open", arrayBitMask[3] == '1');
        fieldInMessage.put("High", arrayBitMask[4] == '1');
        fieldInMessage.put("Low", arrayBitMask[5] == '1');
        fieldInMessage.put("Close", arrayBitMask[6] == '1');
        fieldInMessage.put("Bid", arrayBitMask[7] == '1');
        fieldInMessage.put("Ask", arrayBitMask[8] == '1');
        fieldInMessage.put("BidSize", arrayBitMask[9] == '1');
        fieldInMessage.put("AskSize", arrayBitMask[10] == '1');
        fieldInMessage.put("TotalVolume", arrayBitMask[11] == '1');
        fieldInMessage.put("TotalValue", arrayBitMask[12] == '1');
        fieldInMessage.put("OpenInterest", arrayBitMask[13] == '1');
        fieldInMessage.put("Phase", arrayBitMask[14] == '1');
        fieldInMessage.put("Status", arrayBitMask[15] == '1');
        fieldInMessage.put("BidAmount", arrayBitMask[16] == '1');
        fieldInMessage.put("AskAmount", arrayBitMask[17] == '1');
        fieldInMessage.put("OpenValue", arrayBitMask[18] == '1');
        fieldInMessage.put("CloseValue", arrayBitMask[19] == '1');
        fieldInMessage.put("ReferPrice", arrayBitMask[20] == '1');
        fieldInMessage.put("Offers", arrayBitMask[21] == '1');
        fieldInMessage.put("Error", arrayBitMask[22] == '1');
    }

    /**
     * Returned map contains information, if a given field is valid.
     * {@code True} for filled filled {@code False} for unfilled.
     *
     * @return map of property names associated with a boolean value
     */
    @Contract(pure = true)
    public Map<String, Boolean> getBitMask() {
        logger.exiting(NolRecentInfoAPI.class.getName(), "getBitMask", wrappee.BitMask);
        return Collections.unmodifiableMap(fieldInMessage);
    }

    @NotNull
    public Ticker getTicker() {
        logger.exiting(NolTickerAPI.class.getName(), "getTicker");
        return ticker;
    }

    /**
     * Returns cash value of last transaction.
     *
     * @return cash value
     */
    @Contract(pure = true)
    public double getValoLT() {
        logger.exiting(NolTickerAPI.class.getName(), "getValoLT", wrappee.ValoLT);
        return wrappee.ValoLT;
    }

    /**
     * Returns volume of last transaction.
     *
     * @return volume
     */
    @Contract(pure = true)
    public int getVoLT() {
        logger.exiting(NolTickerAPI.class.getName(), "getVoLT", wrappee.VoLT);
        return wrappee.VoLT;
    }

    /**
     * Returns time of last transaction in hh:mm:ss format.
     *
     * @return time
     */
    @NotNull
    public String getToLT() {
        logger.exiting(NolTickerAPI.class.getName(), "getToLT");
        return toLT;
    }

    /**
     * Returns open price of current trading session.
     *
     * @return open
     */
    @Contract(pure = true)
    public double getOpen() {
        logger.exiting(NolTickerAPI.class.getName(), "getOpen");
        return wrappee.Open;
    }

    /**
     * Return high price of current trading session.
     *
     * @return high
     */
    @Contract(pure = true)
    public double getHigh() {
        logger.exiting(NolTickerAPI.class.getName(), "getHigh", wrappee.High);
        return wrappee.High;
    }

    /**
     * Return low price of current trading session.
     *
     * @return low
     */
    @Contract(pure = true)
    public double getLow() {
        logger.exiting(NolTickerAPI.class.getName(), "getLow", wrappee.Low);
        return wrappee.Low;
    }

    /**
     * Return close price of current trading session. TODO check if this is true!
     *
     * @return close
     */
    @Contract(pure = true)
    public double getClose() {
        logger.exiting(NolTickerAPI.class.getName(), "getClose", wrappee.Close);
        return wrappee.Close;
    }

    /**
     * Return current best bid price.
     *
     * @return bid price
     */
    @Contract(pure = true)
    public double getBid() {
        logger.exiting(NolTickerAPI.class.getName(), "getBid", wrappee.Bid);
        return wrappee.Bid;
    }

    /**
     * Return current best ask price.
     *
     * @return ask price
     */
    @Contract(pure = true)
    public double getAsk() {
        logger.exiting(NolTickerAPI.class.getName(), "getAsk", wrappee.Ask);
        return wrappee.Ask;
    }

    /**
     * Return size of offer of current best bid price.
     *
     * @return size
     */
    @Contract(pure = true)
    public int getBidSize() {
        logger.exiting(NolTickerAPI.class.getName(), "getBidSize", wrappee.BidSize);
        return wrappee.BidSize;
    }

    /**
     * Return size of offer of current best ask price.
     *
     * @return size
     */
    public int getAskSize() {
        logger.exiting(NolTickerAPI.class.getName(), "getAskSize", wrappee.AskSize);
        return wrappee.AskSize;
    }

    /**
     * Get cumulative volume of current trading session.
     *
     * @return cumulative volume
     */
    public int getTotalVolume() {
        logger.exiting(NolTickerAPI.class.getName(), "getTotalVolume", wrappee.TotalVolume);
        return wrappee.TotalVolume;
    }

    /**
     * Get cumulative turnover of current trading session.
     *
     * @return turnover
     */
    public double getTotalValue() {
        logger.exiting(NolTickerAPI.class.getName(), "getTotalValue", wrappee.TotalValue);
        return wrappee.TotalValue;
    }

    /**
     * Get open interest. Valid for derivates only.
     *
     * @return open interest
     */
    public int getOpenInterest() {
        logger.exiting(NolTickerAPI.class.getName(), "getOpenInterest", wrappee.OpenInterest);
        return wrappee.OpenInterest;
    }

    /**
     * Phase of trading session.
     *
     * @return phase
     */
    public String getPhase() {
        logger.exiting(NolTickerAPI.class.getName(), "getPhase", wrappee.Phase);
        return phase;
    }

    /**
     * Status of trading session.
     *
     * @return status
     */
    public String getStatus() {
        logger.exiting(NolTickerAPI.class.getName(), "getStatus", wrappee.Status);
        return status;
    }

    /**
     * Get amount of offers of current best bid. May return 0 instead of real value.
     * Use {@link NolBidAskTblAPI#getAmount()} on best bid from {@link NolRecentInfoAPI#getOffers()}
     * for selected best bid instead of this.
     *
     * @return amount of offers
     */
    public int getBidAmount() {
        logger.exiting(NolTickerAPI.class.getName(), "getBidAmount", wrappee.BidAmount);
        return wrappee.BidAmount;
    }

    /**
     * Get amount of offers of current best ask. May return 1 instead of real value.
     * Use {@link NolBidAskTblAPI#getAmount()} on best ask from {@link NolRecentInfoAPI#getOffers()}
     * for selected best ask instead of this.
     *
     * @return amount of offers
     */
    public int getAskAmount() {
        logger.exiting(NolTickerAPI.class.getName(), "getAskAmount", wrappee.AskAmount);
        return wrappee.AskAmount;
    }

    /**
     * Returns turnover of session opening.
     *
     * @return turnover
     */
    public double getOpenValue() {
        logger.exiting(NolTickerAPI.class.getName(), "getOpenValue", wrappee.OpenValue);
        return wrappee.OpenValue;
    }

    /**
     * Returns turnover of session closing.
     *
     * @return turnover
     */
    public double getCloseValue() {
        logger.exiting(NolTickerAPI.class.getName(), "getCloseValue", wrappee.CloseValue);
        return wrappee.CloseValue;
    }

    /**
     * Return close price of previous trading session.
     *
     * @return reference price
     */
    public double getReferPrice() {
        logger.exiting(NolTickerAPI.class.getName(), "getReferPrice", wrappee.ReferPrice);
        return wrappee.ReferPrice;
    }

    /**
     * Return L2 market depth. Up to 5 price levels of bid and ask.
     *
     * @return list of offers
     * @see NolBidAskTblAPI
     */
    public List<NolBidAskTblAPI> getOffers() {
        logger.exiting(NolTickerAPI.class.getName(), "getOffers", wrappee.offers);
        return offers.getBidask_table();
    }

    /**
     * If less than 0 then the message is invalid
     *
     * @return error code
     */
    private int getError() {
        return wrappee.Error;
    }

    @Override
    public String toString() {
        return "\nNolRecentInfoAPI" +
                "\nBit mask: " + getBitMask() +
                "\nTicker: " + getTicker() +
                "\nValue of last transaction: " + getValoLT() +
                "\nVolume of last transaction: " + getVoLT() +
                "\nTime of last transaction: " + getToLT() +
                "\nOpen: " + getOpen() +
                "\nHigh: " + getHigh() +
                "\nLow: " + getLow() +
                "\nClose: " + getClose() +
                "\nBid: " + getBid() +
                "\nAsk: " + getAsk() +
                "\nBid size: " + getBidSize() +
                "\nAsk size: " + getAskSize() +
                "\nTotal volume: " + getTotalVolume() +
                "\nTotal value: " + getTotalValue() +
                "\nOpen interest: " + getOpenInterest() +
                "\nPhase: " + getPhase() +
                "\nStatus: " + getStatus() +
                "\nBid amount: " + getBidAmount() +
                "\nAsk amount: " + getAskAmount() +
                "\nOpen value: " + getOpenValue() +
                "\nClose value: " + getCloseValue() +
                "\nReference price: " + getReferPrice() +
                "\nOffers: " + getOffers();
    }
}
