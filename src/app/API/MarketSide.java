package app.API;

public enum MarketSide {
    BID(1),
    ASK(2);

    private final int value;

    MarketSide(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static final MarketSide[] values = values();


}
