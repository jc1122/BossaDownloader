package app.gui.dialog;

import app.gui.Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Model part of MVC for dialogs. Provides basic functionality - property change support.
 * Registers itself as listener to overlaying model provided in constructor.
 * Implement {@link GUIModel#propertyChange(PropertyChangeEvent)} to notify listeners about
 * property changes of overlaying model.
 *
 *
 * @see GUIDialog
 * @see GUIView
 * @see GUIController
 */
public abstract class GUIModel implements PropertyChangeListener {
    protected Model mainModel;
    protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listaner) {
        propertyChangeSupport.removePropertyChangeListener(listaner);
    }

    protected GUIModel(Model mainModel) {
        this.mainModel = mainModel;
        mainModel.addPropertyChangeListener(this);
    }

    @Override
    public abstract void propertyChange(PropertyChangeEvent propertyChangeEvent);
}
