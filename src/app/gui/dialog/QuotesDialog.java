package app.gui.dialog;

import app.API.JNAinterface.DefaultFilter;
import app.API.JNAinterface.NolRecentInfoAPI;
import app.API.JNAinterface.Tickers;
import app.API.PublicAPI.Ticker;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class QuotesDialog implements PropertyChangeListener {
    DefaultFilter filter = new DefaultFilter();
    Ticker observedTicker;

    JDialog dialog;
    JPanel pane;

    JLabel valolt, volt, tolt, open, high, low, close, bid, ask, bidsize, asksize, totalvolume, totalvalue, openinterest;
    JLabel phase, status, bidamount, askamount, openvalue, closevalue, referprice, offers, error;

    QuotesDialog() {
        Set<Ticker> tickers = Tickers.getInstance().getValue();

        filter.addPropertyChangeListener(this);

        observedTicker = tickers.stream().filter(x -> x.getIsin().contains("KGHM")).findFirst().get();
        filter.addTickersToFilter(new HashSet<>(Collections.singleton(observedTicker)));

        dialog = new JDialog();
        pane = new JPanel();
        GridLayout paneLayout = new GridLayout(0, 2);

        pane.add(new JLabel("Ticker:"));
        pane.add(new JLabel(observedTicker.getName()));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), "Quotes")) {
            NolRecentInfoAPI info = (NolRecentInfoAPI) evt.getNewValue();
            info.
        }
    }
}
