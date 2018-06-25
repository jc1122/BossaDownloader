package app.gui.dialog.QuoteDialog;

import app.API.JNAinterface.DefaultFilter;
import app.API.JNAinterface.NolBidAskTblAPI;
import app.API.JNAinterface.NolRecentInfoAPI;
import app.API.JNAinterface.Tickers;
import app.API.PublicAPI.Ticker;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

public class QuotesDialog implements PropertyChangeListener {

    private JDialog dialog;
    private Ticker instrument;


    private Map<String, JLabel> labels;
    private Map<String, JLabel> offerLabels;

    private Map<String, JLabel> values;
    private Map<String, JLabel> offerValues;


    public QuotesDialog() {
        DefaultFilter filter = new DefaultFilter();
        Set<Ticker> tickers = Tickers.getInstance().getValue();
        instrument = tickers.stream().filter(x -> x.getIsin().contains("KGHM000017")).findFirst().get();

        SwingUtilities.invokeLater(this::createGUI);

        filter.addPropertyChangeListener(this);
        filter.addTickersToFilter(Collections.singleton(instrument));

    }

    public void createGUI() {
        dialog = new JDialog();
        JPanel pane = new JPanel();
        GridLayout paneLayout = new GridLayout(0,2);
        pane.setLayout(paneLayout);

        labels = new LinkedHashMap<>();
        labels.put("Ticker", new JLabel());
        labels.put("TickerISIN", new JLabel());
        labels.put("TickerName", new JLabel());
        labels.put("TickerMarketCode", new JLabel());
        labels.put("TickerCFI", new JLabel());
        labels.put("TickerGroup", new JLabel());

        labels.put("Phase", new JLabel());
        labels.put("Status", new JLabel());

        labels.put("Open", new JLabel());
        labels.put("High", new JLabel());
        labels.put("Low", new JLabel());
        labels.put("Close", new JLabel());
        labels.put("ReferPrice", new JLabel());

        labels.put("ValoLT", new JLabel());
        labels.put("VoLT", new JLabel());
        labels.put("ToLT", new JLabel());

        labels.put("Bid", new JLabel());
        labels.put("BidSize", new JLabel());
        labels.put("BidAmount", new JLabel());

        labels.put("Ask", new JLabel());
        labels.put("AskSize", new JLabel());
        labels.put("AskAmount", new JLabel());

        labels.put("TotalVolume", new JLabel());
        labels.put("TotalValue", new JLabel());
        labels.put("OpenInterest", new JLabel());

        labels.put("OpenValue", new JLabel());
        labels.put("CloseValue", new JLabel());

        labels.put("Error", new JLabel());

        labels.forEach((key, value) -> value.setText(key));
        values = new HashMap<>();
        labels.keySet().forEach(key -> values.put(key, new JLabel()));

        for(String key : labels.keySet()) {
            pane.add(labels.get(key));
            pane.add(values.get(key));
        }

        BoxLayout dialogLayout = new BoxLayout(dialog.getContentPane(), BoxLayout.PAGE_AXIS);
        dialog.getContentPane().setLayout(dialogLayout);
        dialog.getContentPane().add(pane);
        dialog.getContentPane().add(createOfferPane());
        dialog.setSize(new Dimension(600, 300));
        dialog.setVisible(true);
    }

    private JPanel createOfferPane() {
        JPanel offerPane = new JPanel();
        GridLayout layout = new GridLayout(0, 4);
        offerPane.setLayout(layout);

        offerLabels = new LinkedHashMap<>();
        offerLabels.put("Side", new JLabel());
        offerLabels.put("Price", new JLabel());
        offerLabels.put("Size", new JLabel());
        offerLabels.put("Amount", new JLabel());

        offerValues = new LinkedHashMap<>();
        offerValues.put("ASK5side", new JLabel());
        offerValues.put("ASK5price", new JLabel());
        offerValues.put("ASK5size", new JLabel());
        offerValues.put("ASK5amount", new JLabel());

        offerValues.put("ASK4side", new JLabel());
        offerValues.put("ASK4price", new JLabel());
        offerValues.put("ASK4size", new JLabel());
        offerValues.put("ASK4amount", new JLabel());

        offerValues.put("ASK3side", new JLabel());
        offerValues.put("ASK3price", new JLabel());
        offerValues.put("ASK3size", new JLabel());
        offerValues.put("ASK3amount", new JLabel());

        offerValues.put("ASK2side", new JLabel());
        offerValues.put("ASK2price", new JLabel());
        offerValues.put("ASK2size", new JLabel());
        offerValues.put("ASK2amount", new JLabel());

        offerValues.put("ASK1side", new JLabel());
        offerValues.put("ASK1price", new JLabel());
        offerValues.put("ASK1size", new JLabel());
        offerValues.put("ASK1amount", new JLabel());

        offerValues.put("BID1side", new JLabel());
        offerValues.put("BID1price", new JLabel());
        offerValues.put("BID1size", new JLabel());
        offerValues.put("BID1amount", new JLabel());

        offerValues.put("BID2side", new JLabel());
        offerValues.put("BID2price", new JLabel());
        offerValues.put("BID2size", new JLabel());
        offerValues.put("BID2amount", new JLabel());

        offerValues.put("BID3side", new JLabel());
        offerValues.put("BID3price", new JLabel());
        offerValues.put("BID3size", new JLabel());
        offerValues.put("BID3amount", new JLabel());

        offerValues.put("BID4side", new JLabel());
        offerValues.put("BID4price", new JLabel());
        offerValues.put("BID4size", new JLabel());
        offerValues.put("BID4amount", new JLabel());

        offerValues.put("BID5side", new JLabel());
        offerValues.put("BID5price", new JLabel());
        offerValues.put("BID5size", new JLabel());
        offerValues.put("BID5amount", new JLabel());

        for (String key : offerLabels.keySet()) {
            offerLabels.get(key).setText(key);
            offerPane.add(offerLabels.get(key));
        }

        for (String key : offerValues.keySet()) {
            offerPane.add(offerValues.get(key));
        }
        return offerPane;
    }
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if(Objects.equals(propertyChangeEvent.getPropertyName(), "Quotes")) {
            NolRecentInfoAPI info = (NolRecentInfoAPI) propertyChangeEvent.getNewValue();
            Map<String, Boolean> bitMask = info.getBitMask();
            //System.out.println(bitMask);

            values.get("TickerISIN").setText(info.getTicker().getIsin());
            values.get("TickerName").setText(info.getTicker().getName());
            values.get("TickerMarketCode").setText(info.getTicker().getMarketCode());
            values.get("TickerCFI").setText(info.getTicker().getCFI());
            values.get("TickerGroup").setText(info.getTicker().getGroup());

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
            if(bitMask.get("Bid")) {
                values.get("Bid").setText(Double.toString(info.getBid()));
            }
            if(bitMask.get("Ask")) {
                //System.out.println(info.getAsk());
                values.get("Ask").setText(Double.toString(info.getAsk()));
            }
            if(bitMask.get("BidSize")) {
                values.get("BidSize").setText(Integer.toString(info.getBidSize()));
            }
            if(bitMask.get("AskSize")) {
                values.get("AskSize").setText(Integer.toString(info.getAskSize()));
            }
            if(bitMask.get("TotalVolume")) {
                values.get("TotalVolume").setText(Integer.toString(info.getTotalVolume()));
            }
            if(bitMask.get("TotalValue")) {
                values.get("TotalValue").setText(Double.toString(info.getTotalValue()));
            }
            if(bitMask.get("OpenInterest")) {
                values.get("OpenInterest").setText(Integer.toString(info.getOpenInterest()));
            }
            if(bitMask.get("Phase")) {
                values.get("Phase").setText(info.getPhase());
            }
            if(bitMask.get("Status")) {
                values.get("Status").setText(info.getStatus());
            }
            if(bitMask.get("BidAmount")) {
                values.get("BidAmount").setText(Integer.toString(info.getBidAmount()));
            }
            if(bitMask.get("AskAmount")) {
                values.get("AskAmount").setText(Integer.toString(info.getAskAmount()));
            }
            if(bitMask.get("OpenValue")) {
                values.get("OpenValue").setText(Double.toString(info.getOpenValue()));
            }
            if(bitMask.get("CloseValue")) {
                values.get("CloseValue").setText(Double.toString(info.getCloseValue()));
            }
            if(bitMask.get("ReferPrice")) {
                values.get("ReferPrice").setText(Double.toString(info.getReferPrice()));
            }
            if(bitMask.get("Offers")) {
                Set<NolBidAskTblAPI> offers = info.getOffers();
                for (NolBidAskTblAPI offer : offers) {
                    StringBuilder key = new StringBuilder();
                    key.append(offer.getSide());
                    key.append(offer.getDepth());
                    //System.out.println(key.toString() + "price");
                    offerValues.get(key.toString() + "side")
                            .setText(offer.getSide().toString());
                    offerValues.get(key.toString() + "price")
                            .setText(Double.toString(offer.getPrice()));
                    offerValues.get(key.toString() + "size")
                            .setText(Integer.toString(offer.getSize()));
                    offerValues.get(key.toString() + "amount")
                            .setText(Integer.toString(offer.getAmount()));
                }
            }
            if(bitMask.get("Error")) {
                values.get("Error").setText("no error info");
            }
        }
    }
}
