package app.API.JNAinterface;

import app.API.JNAenums.Nol3State;
import app.API.PropertyAPI;

/**
 * Updates info about current NOL3 app status. This class handles listeners for {@link Nol3State}
 *
 * @see Nol3State
 */
final class Status extends PropertyAPI<Nol3State, String> {

    private static final Status INSTANCE = new Status();
    private static final Status.StatusCallbackHelper CALLBACK_HELPER = INSTANCE.new StatusCallbackHelper();

    private Status() {
        super("Status");
    }

    static Status getInstance() {
        logger.exiting(Status.class.getName(), "getInstance");
        return INSTANCE;
    }

    static StatusCallbackHelper getCallbackHelper() {
        return CALLBACK_HELPER;
    }

    final class StatusCallbackHelper implements BossaAPIInterface.SetCallbackStatusDummy {
        @Override
        public void invoke(Nol3State nol3State) {
            logger.exiting(this.getClass().getName(), "invoke");
            Nol3State oldVal = Status.this.property;
            Status.this.property = nol3State;
            Status.this.propertyChangeSupport.firePropertyChange("Status", oldVal, property);
        }
    }
}

