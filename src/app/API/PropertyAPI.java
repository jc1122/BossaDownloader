package app.API;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Logger;

/**
 * Provides {@link PropertyChangeSupport} for callbacks of API and wraps property.
 * Property may be accessed only after it has been initialized!
 * Property name should be the same as the name of class inheriting from this class.
 * Child classes should be immutable.
 * @param <T> class of wrapped property
 */
//refactoring CallbackHelpers to generic class impossible due to type erasure...
//cannot map Object type in JNA to multiple classes with typemapper, it would break ordinary type mapping
public abstract class PropertyAPI<T> {
    protected final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    protected T property;
    protected static final Logger logger =
            Logger.getLogger(PropertyAPI.class.getName());

//    private PropertyAPI() {
//    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        logger.exiting(this.getClass().getName(), "addPropertyChangeListener");
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        logger.exiting(this.getClass().getName(), "removePropertyChangeListener");
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public T getProperty() {
        logger.exiting(this.getClass().getName(), "getProperty");
        if (property == null) {
            throw new NullPointerException(
                    "property "
                            + this.getClass()
                            + " has not been initialized yet, access through listener!");
        }
        return property;
    }
}
