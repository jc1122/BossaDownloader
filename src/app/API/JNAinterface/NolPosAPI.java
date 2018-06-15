package app.API.JNAinterface;

import app.API.PublicAPI.DefaultFilter;
import app.API.PublicAPI.Filter;
import app.API.PublicAPI.Position;
import app.API.PublicAPI.Ticker;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.Objects;

/**
 * Contains information about position in portfolio.
 * Will update value of current position on market data change.
 */
public final class NolPosAPI extends BossaAPIClassWrapper<NolPosAPI, BossaAPIInterface.NolPos> implements Position {
    private final Ticker ticker;

    @SuppressWarnings("FieldCanBeLocal") //will fall out of scope and not update property when collected by gc
    private final DefaultFilter<Ticker> filter;

    private final PropertyChangeSupport pcs;
    private double price = 0;

    private class TickerFilter extends DefaultFilter<Ticker> {

        TickerFilter() {
            super(Filter.getMasterFilter());
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (Objects.equals(evt.getPropertyName(), "Quotes")) {
                NolRecentInfoAPI info = (NolRecentInfoAPI) evt.getNewValue();
                if (info.getTicker().equals(ticker) && price != info.getReferPrice()) {
                    pcs.firePropertyChange("Price", price, info.getReferPrice());
                    price = info.getReferPrice();
                    filter.removeTickersFromFilter(Collections.singleton(getTicker()));
                }
            }
            super.propertyChange(evt);
        }
    }
    //constructor accessed by reflection
    private NolPosAPI(BossaAPIInterface.NolPos nolPos) {
        super(nolPos);
        this.ticker = new NolTickerAPI(wrappee.ticker);
        filter = new TickerFilter();
        filter.addTickersToFilter(Collections.singleton(this.getTicker()));
        pcs = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        logger.exiting(this.getClass().getName(), "addPropertyChangeListener");
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        logger.exiting(this.getClass().getName(), "removePropertyChangeListener");
        pcs.removePropertyChangeListener(listener);
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
        return price * (getAcc110() + getAcc120());
    }

    @Override
    public String toString() {
        return "\nNolPosAPI " +
                "\nTicker: " + getTicker() +
                "\nAcc110 shares free: " + getAcc110() +
                "\nAcc120 shares blocked: " + getAcc120();
    }
}
