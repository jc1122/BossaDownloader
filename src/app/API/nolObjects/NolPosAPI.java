package app.API.nolObjects;

import app.API.JNAinterface.BossaAPIInterface;
import org.jetbrains.annotations.NotNull;

/**
 * Contains information about position in portfolio.
 */
public final class NolPosAPI extends BossaAPIClassWrapper<NolPosAPI, BossaAPIInterface.NolPos> {
    NolTickerAPI ticker;

    private NolPosAPI(BossaAPIInterface.NolPos nolPos) {
        this.wrappee = nolPos;
        this.ticker = new NolTickerAPI(wrappee.ticker);
    }

    /**
     * Returns the ticker of portfolio position.
     *
     * @return ticker
     */
    @NotNull
    public NolTickerAPI getTicker() {
        logger.exiting(NolPosAPI.class.getName(), "getTicker");
        return ticker;
    }

    /**
     * Get amount of shares of position on default account. This amount is at users disposal.
     *
     * @return amount
     */
    public int getAcc110() {
        logger.exiting(NolPosAPI.class.getName(), "getAcc110", wrappee.acc110);
        return wrappee.acc110;
    }

    /**
     * Get amount of shares which are currently blocked. Ex. they may be blocked for a pending sell order.
     *
     * @return amount
     */
    public int getAcc120() {
        logger.exiting(NolPosAPI.class.getName(), "getAcc120", wrappee.acc120);
        return wrappee.acc120;
    }

    @Override
    public String toString() {
        return "\nNolPosAPI " +
                "\nTicker: " + getTicker() +
                "\nAcc110 shares free: " + getAcc110() +
                "\nAcc120 shares blocked: " + getAcc120();
    }
}
