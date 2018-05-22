package app.gui.dialog;

import app.gui.Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class GUIModel implements PropertyChangeListener {
    protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listaner) {
        propertyChangeSupport.removePropertyChangeListener(listaner);
    }

    protected GUIModel(Model model) {
        model.addPropertyListener(this);
    }

    @Override
    public abstract void propertyChange(PropertyChangeEvent propertyChangeEvent);
}
