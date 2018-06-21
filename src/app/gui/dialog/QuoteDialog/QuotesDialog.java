package app.gui.dialog.QuoteDialog;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import app.API.JNAinterface.DefaultFilter;
import app.API.JNAinterface.NolRecentInfoAPI;
import app.API.JNAinterface.Tickers;
import app.API.PublicAPI.Ticker;

public class QuotesDialog implements PropertyChangeListener {

    private JDialog dialog;
    private Ticker instrument;


    private Map<String, JLabel> labels;
    private Map<String, JLabel> values;

    public QuotesDialog() {
        DefaultFilter filter = new DefaultFilter();
        Set<Ticker> tickers = Tickers.getInstance().getValue();
        instrument = tickers.stream().filter(x -> x.getIsin().contains("KGHM000017")).findFirst().get();


        filter.addPropertyChangeListener(this);
        filter.addTickersToFilter(Collections.singleton(instrument));

        createGUI();
    }

    public void createGUI() {
        dialog = new JDialog();
        JPanel pane = new JPanel();
        GridLayout paneLayout = new GridLayout(0,2);
        pane.setLayout(paneLayout);

        labels = new HashMap<>();
        labels.put("ValoLT", new JLabel());
        labels.put("VoLT", new JLabel());
        labels.put("ToLT", new JLabel());
        labels.put("Open", new JLabel());
        labels.put("High", new JLabel());
        labels.put("Low", new JLabel());
        labels.put("Close", new JLabel());
        labels.put("Bid", new JLabel());
        labels.put("Ask", new JLabel());
        labels.put("BidSize", new JLabel());
        labels.put("AskSize", new JLabel());
        labels.put("TotalVolume", new JLabel());
        labels.put("TotalValue", new JLabel());
        labels.put("OpenInterest", new JLabel());
        labels.put("Phase", new JLabel());
        labels.put("Status", new JLabel());
        labels.put("BidAmount", new JLabel());
        labels.put("AskAmount", new JLabel());
        labels.put("OpenValue", new JLabel());
        labels.put("CloseValue", new JLabel());
        labels.put("ReferPrice", new JLabel());
        labels.put("Offers", new JLabel());
        labels.put("Error", new JLabel());

        labels.forEach((key, value) -> value.setText(key));
        labels.keySet().forEach(key -> values.put(key, new JLabel()));

        for(String key : labels.keySet()) {
            pane.add(labels.get(key));
            pane.add(values.get(key));
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if(Objects.equals(propertyChangeEvent.getPropertyName(), "Quotes")) {
            NolRecentInfoAPI info = (NolRecentInfoAPI) propertyChangeEvent.getNewValue();
            Map<String, Boolean> bitMask = info.getBitMask();
            if(bitMask.get("ValoLT")) {
                values.get("ValoLT").setText(Double.toString(info.getValoLT()));
            }
            if(bitMask.get("VoLT")) {
                values.get("VoLT").setText(Double.toString(info.getVoLT()));
            }
            if(bitMask.get("ToLT")) {
                values.get("ToLT").setText(info.getToLT());
            }
            if(bitMask.get("Open")) {
                values.get("Open").setText(Double.toString(info.getOpen()));
            }
            if(bitMask.get("High")) {
                values.get("High").setText(Double.toString(info.getHigh()));
            }
            if(bitMask.get("Low")) {
                values.get("Low").setText(Double.toString(info.getLow()));
            }
            if(bitMask.get("Close")) {
                values.get("Close").setText(Double.toString(info.getClose()));
            }
        }
    }
}
