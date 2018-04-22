package app.bossaAPI;

public enum MarketSide {
    BID(1),
    ASK(2);
    private int value;

    MarketSide(int value) {
        this.value = value;
    }

    public static final MarketSide[] values = values();


}
