package app.API.JNAinterface;

import app.API.PublicAPI.PropertyAPI;

/**
 * Updates delay time to server. This class handles listeners for delay to NOL server.
 */
final class Delay extends PropertyAPI<Float, String> {

    private static final Delay INSTANCE = new Delay();
    private static final DelayCallbackHelper CALLBACK_HELPER = INSTANCE.new DelayCallbackHelper();

    private Delay() {
        super("Delay");
    }

    static Delay getInstance() {
        logger.exiting(Delay.class.getName(), "getInstance");
        return INSTANCE;
    }

    static DelayCallbackHelper getCallbackHelper() {
        return CALLBACK_HELPER;
    }

    final class DelayCallbackHelper implements BossaAPIInterface.SetCallbackDelayDummy {
        @Override
        public void invoke(float delay) {
            logger.exiting(this.getClass().getName(), "invoke");
            Float oldVal = Delay.this.property; //must be boxed type or InvocationTargetException by JNA callback
            Delay.this.property = delay;
            Delay.this.propertyChangeSupport.firePropertyChange("Delay", oldVal, delay);
        }
    }
}
