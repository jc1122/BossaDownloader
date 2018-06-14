package app.API;

import java.beans.PropertyChangeListener;
import java.util.Map;

public interface Properties<K, V extends PropertyAPI<?,K>> {

    Map<K, V> getProperties();
    void addPropertyChangeListener(PropertyChangeListener listener); //add listener to all properties
    void removePropertyChangeListener(PropertyChangeListener listener); //remove listener from all properties
    void addPropertyChangeListener(K propertyName, PropertyChangeListener listener);
    void removePropertyChangeListener(K propertyName, PropertyChangeListener listener);
    V getProperty(K name);
}
