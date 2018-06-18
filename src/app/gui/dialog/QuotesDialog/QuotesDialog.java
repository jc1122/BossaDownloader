package app.gui.dialog.QuotesDialog;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import app.API.PublicAPI.BaseFilter;
import app.API.PublicAPI.DefaultFilter;
import app.API.PublicAPI.Ticker;

public class QuotesDialog implements PropertyChangeListener {

    JDialog dialog;

    QuotesDialog() {
        BaseFilter<Ticker> filter = new DefaultFilter();
    }
    public void createGUI() {
        dialog = new JDialog();
        JPanel pane = new JPanel();

    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

    }
}
