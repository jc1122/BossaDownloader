package app.API.JNAinterface;

import app.API.enums.Nol3State;

/**
 * Updates info about current NOL3 app status. This class handles listeners for {@link Nol3State}
 *
 * @see Nol3State
 */
public final class Status extends PropertyAPI<Nol3State> implements BossaAPIInterface.SetCallbackStatusDummy{

    private static final Status INSTANCE = new Status();

    private Status() {
    }

    public static Status getInstance() {
        logger.exiting(Status.class.getName(), "getInstance");
        return INSTANCE;
    }

        @Override
        public void invoke(Nol3State nol3State) {
            logger.exiting(this.getClass().getName(), "invoke");
            Nol3State oldVal = Status.this.property;
            Status.this.property = nol3State;
            Status.this.propertyChangeSupport.firePropertyChange("Status", oldVal, property);
        }
}
