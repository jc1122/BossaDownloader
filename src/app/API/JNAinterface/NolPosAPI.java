package app.API.JNAinterface;

import app.API.PublicAPI.Ticker;
import org.jetbrains.annotations.NotNull;

/**
 * Contains information about position in portfolio.
 */
public final class NolPosAPI extends BossaAPIClassWrapper<NolPosAPI, BossaAPIInterface.NolPos> implements Position {
    private final Ticker ticker;

    //constructor accessed by reflection
    private NolPosAPI(BossaAPIInterface.NolPos nolPos) {
        super(nolPos);
        this.ticker = new NolTickerAPI(wrappee.ticker);
    }

    /**
     * Returns the ticker of portfolio position.
     *
     * @return ticker
     */
    @Override
    @NotNull
    public Ticker getTicker() {
        logger.exiting(NolPosAPI.class.getName(), "getTicker");
        return ticker;
    }

    /**
     * Get amount of shares of position on default account. This amount is at users disposal.
     *
     * @return amount
     */
    @Override
    public int getAcc110() {
        logger.exiting(NolPosAPI.class.getName(), "getAcc110", wrappee.acc110);
        return wrappee.acc110;
    }

    /**
     * Get amount of shares which are currently blocked. Ex. they may be blocked for a pending sell order.
     *
     * @return amount
     */
    @Override
    public int getAcc120() {
        logger.exiting(NolPosAPI.class.getName(), "getAcc120", wrappee.acc120);
        return wrappee.acc120;
    }

    @Override
    public double getValue() {
        return 0;
    }

    @Override
    public String toString() {
        return "\nNolPosAPI " +
                "\nTicker: " + getTicker() +
                "\nAcc110 shares free: " + getAcc110() +
                "\nAcc120 shares blocked: " + getAcc120();
    }
}
