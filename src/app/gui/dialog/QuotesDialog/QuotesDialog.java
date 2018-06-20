package app.gui.dialog.QuotesDialog;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import app.API.JNAinterface.DefaultFilter;

public class QuotesDialog implements PropertyChangeListener {

    JDialog dialog;

    QuotesDialog() {
        DefaultFilter filter = new DefaultFilter();
    }
    public void createGUI() {
        dialog = new JDialog();
        JPanel pane = new JPanel();

    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

    }
}
