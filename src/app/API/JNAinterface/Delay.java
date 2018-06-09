package app.API.JNAinterface;

/**
 * Updates delay time to server. This class handles listeners for delay to NOL server.
 */
public final class Delay extends PropertyAPI<Float> implements BossaAPIInterface.SetCallbackDelayDummy {

    private static final Delay INSTANCE = new Delay();

    private Delay() {
    }

    public static Delay getInstance() {
        logger.exiting(Delay.class.getName(), "getInstance");
        return INSTANCE;
    }

        @Override
        public void invoke(float delay) {
            logger.exiting(this.getClass().getName(), "invoke");
            Float oldVal = Delay.this.property; //must be boxed type or InvocationTargetException by JNA callback
            Delay.this.property = delay;
            Delay.this.propertyChangeSupport.firePropertyChange("Delay", oldVal, delay);
        }
}
