package app.API;

/**
 * Stores possible market sides - BID and ASK
 */
public enum MarketSide {
    /**
     * BID side of market
     */
    BID(1),
    /**
     * ASK side of market
     */
    ASK(2);

    private final int value;

    MarketSide(int value) {
        this.value = value;
    }

    /**
     * Returns 1 for BID and 2 for ASK
     *
     * @return Returns 1 for BID and 2 for ASK
     */
    public int getValue() {
        return value;
    }

    public static final MarketSide[] values = values();


}
