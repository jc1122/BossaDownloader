package app.API.JNAinterface;

import org.jetbrains.annotations.Contract;

/**
 * Stores market data of one level of market quotes. Information stored is offer side (bid/ask), offer depth (level),
 * price, amount of offers, cumulative volume of security.
 */
final class NolBidAskTblAPI extends BossaAPIClassWrapper<NolBidAskTblAPI, BossaAPIInterface.NolBidAskTbl> {

    //this constructor is accessed by reflection
    private NolBidAskTblAPI(BossaAPIInterface.NolBidAskTbl nolBidAskTbl) {
        super(nolBidAskTbl);
    }

    /**
     * Position of offer level. <br>
     * Ex. If current offers on ask side are 100, 101, <b>102</b>, 103, then the offer in bold will have a depth of 3.
     *
     * @return position of offer, relative to best offer on given side
     */
    @Contract(pure = true)
    private int getDepth() {
        logger.exiting(NolBidAskTblAPI.class.getName(), "getDepth");
        return wrappee.depth;
    }

    /**
     * Returns the market side of offer. Bid or Ask.
     *
     * @return side
     */
    @Contract(pure = true)
    private MarketSide getSide() {
        logger.exiting(NolBidAskTblAPI.class.getName(), "getSide");
        if (wrappee.side > 2 || wrappee.side < 1) {
            throw new IllegalStateException("not initialized!");
        }
        return MarketSide.values[wrappee.side - 1];
    }

    /**
     * Price of offer on current level.
     *
     * @return price
     */
    @Contract(pure = true)
    private double getPrice() {
        logger.exiting(NolBidAskTblAPI.class.getName(), "getPrice");
        return wrappee.price;
    }

    /**
     * Volume of offers on current level.
     *
     * @return size
     */
    @Contract(pure = true)
    private int getSize() {
        logger.exiting(NolBidAskTblAPI.class.getName(), "getSize");
        return wrappee.size;
    }

    /**
     * Amount of offers on current level.
     *
     * @return amount
     */
    @Contract(pure = true)
    private int getAmount() {
        logger.exiting(NolBidAskTblAPI.class.getName(), "getAmount");
        return wrappee.amount;
    }

    @Override
    public String toString() {
        return "\nNolBidAskTblAPI \n" +
                "Side: " + getSide() + "\n" +
                "Depth: " + getDepth() + "\n" +
                "Price: " + getPrice() + "\n" +
                "Size: " + getSize() + "\n" +
                "Count of offers: " + getAmount() + "\n";
    }
}
