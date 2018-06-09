package app.API.JNAinterface;

/**
 * Updates information about current orders. This class handles listeners for {@link NolOrderReportAPI}
 *
 * @see NolOrderReportAPI
 */
public final class Order extends PropertyAPI<NolOrderReportAPI> {
    //private NolOrderReportAPI nolOrderReportAPI;
    private static final Order INSTANCE = new Order();
    private static final OrderCallbackHelper CALLBACK_HELPER = INSTANCE.new OrderCallbackHelper();

    private Order() {
    }

    public static Order getInstance() {
        logger.exiting(Order.class.getName(), "getInstance");
        return INSTANCE;
    }

    static OrderCallbackHelper getCallbackHelper() {
        return CALLBACK_HELPER;
    }

    final class OrderCallbackHelper implements BossaAPIInterface.SetCallbackOrderDummy {
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
}
