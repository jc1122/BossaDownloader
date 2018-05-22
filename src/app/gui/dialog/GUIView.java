package app.gui.dialog;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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

    public JDialog getDialog() {
        return dialog;
    }

    @Override
    public abstract void propertyChange(PropertyChangeEvent evt);

    public abstract void createGUI();
}
