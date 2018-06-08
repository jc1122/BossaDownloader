package app.API.properties;

import app.API.JNAinterface.BossaAPIInterface;
import app.API.nolObjects.NolOrderReportAPI;

/**
 * Updates information about current orders. This class handles listeners for {@link NolOrderReportAPI}
 *
 * @see NolOrderReportAPI
 */
public final class Order extends PropertyAPI<NolOrderReportAPI> implements BossaAPIInterface.SetCallbackOrderDummy {
    //private NolOrderReportAPI nolOrderReportAPI;
    private static final Order INSTANCE = new Order();

    private Order() {
    }

    public static Order getInstance() {
        logger.exiting(Order.class.getName(), "getInstance");
        return INSTANCE;
    }


        @Override
        public void invoke(BossaAPIInterface.NolOrderReport nolOrderReport) {
            logger.exiting(this.getClass().getName(), "invoke");
            NolOrderReportAPI oldVal = Order.this.property;
            Order.this.property = new NolOrderReportAPI(nolOrderReport);
            Order
                    .this
                    .propertyChangeSupport
                    .firePropertyChange("Order", oldVal, Order.this.property);
        }
}
