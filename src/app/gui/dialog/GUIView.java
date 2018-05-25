package app.gui.dialog;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Template for View of MVC pattern for dialogs. Provides basic functionality - registers itself as listener in the model.
 * Creates an empty dialog.
 * @param <K>
 * @param <L>
 * @param <M>
 */
public abstract class GUIView<K extends GUIModel, L extends GUIView<K, L, M>, M extends GUIController<K, L>>
        implements PropertyChangeListener {
    protected JDialog dialog;
    protected M controller;
    protected K model;

    protected GUIView(M controller, K model) {
        this.controller = controller;
        this.model = model;
        dialog = new JDialog();
        model.addPropertyChangeListener(this);
    }

    /**
     *
     * @return
     */
    public JDialog getDialog() {
        return dialog;
    }

    @Override
    public abstract void propertyChange(PropertyChangeEvent evt);

    /**
     * Called by controller while creating View on event dispatch thread. Implement the visual elements of the view here.
     */
    public abstract void createGUI();
}
