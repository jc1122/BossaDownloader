package app.gui.dialog.QuoteDialog;

import app.API.JNAinterface.DefaultFilter;
import app.API.JNAinterface.NolBidAskTblAPI;
import app.API.JNAinterface.NolRecentInfoAPI;
import app.API.JNAinterface.Tickers;
import app.API.PublicAPI.Ticker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.Instant;
import java.util.*;

public class QuoteLastInfo implements PropertyChangeListener {

    PropertyChangeSupport pcs;

    private static Map<String, QuoteLastInfo> INSTANCES = new HashMap<>();
    private String tolt, phase, status;
    private Map<String, NolBidAskTblAPI> offers;
    private String isin;
    private double valoLT, open, high, low, close, bid, ask, totalValue, openVal, closeVal, referPrice;
    private int bidsize, asksize, totalVolume, openInt, bidAmount, askAmount, voLT;
    private Ticker ticker;

    public String getName() {
        return ticker.getName();
    }
    public String getHeader() {
        return "Timestamp,Name,Phase,Status," +
                "Open,High,Low,Close," +
                "ReferencePrice,TotalValue,TotalVolume,OpenValue,CloseValue," +
                "PriceOfLastTransaction,VolumeOfLastTransaction,TimeOfLastTransaction," +
                "Bid,BidAmount,BidSize," +
                "Ask,AskAmount,AskSize," +
                "OpenInterest," +
                "ASK5Price,ASK5Size,ASK5Amount," +
                "ASK4Price,ASK4Size,ASK4Amount," +
                "ASK3Price,ASK3Size,ASK3Amount," +
                "ASK2Price,ASK2Size,ASK2Amount," +
                "ASK1Price,ASK1Size,ASK1Amount," +
                "BID1Price,BID1Size,BID1Amount," +
                "BID2Price,BID2Size,BID2Amount," +
                "BID3Price,BID3Size,BID3Amount," +
                "BID4Price,BID4Size,BID4Amount," +
                "BID5Price,BID5Size,BID5Amount,";
    }
    @Override
    public String toString() {
        StringJoiner builder = new StringJoiner(",");
        builder.add(Instant.now().toString());
        builder.add(ticker.getName());
        builder.add(phase);
        builder.add(status);
        builder.add(Double.toString(open));
        builder.add(Double.toString(high));
        builder.add(Double.toString(low));
        builder.add(Double.toString(close));
        builder.add(Double.toString(referPrice));
        builder.add(Double.toString(totalValue));
        builder.add(Integer.toString(totalVolume));
        builder.add(Double.toString(openVal));
        builder.add(Double.toString(closeVal));
        builder.add(Double.toString(valoLT));
        builder.add(Integer.toString(voLT));
        builder.add(tolt);
        builder.add(Double.toString(bid));
        builder.add(Integer.toString(bidAmount));
        builder.add(Integer.toString(bidsize));
        builder.add(Double.toString(ask));
        builder.add(Integer.toString(askAmount));
        builder.add(Integer.toString(asksize));
        builder.add(Integer.toString(openInt));
        for(NolBidAskTblAPI offer : offers.values()) {
            if(offer == null) {
                builder.add("");
                builder.add("");
                builder.add("");
                continue;
            }
            builder.add(Double.toString(offer.getPrice()));
            builder.add(Integer.toString(offer.getSize()));
            builder.add(Integer.toString(offer.getAmount()));
        }

        return builder.toString();
    }
    private QuoteLastInfo(String isin) {
        DefaultFilter filter = new DefaultFilter();
        Set<Ticker> tickers = Tickers.getInstance().getValue();

        if (tickers.stream().anyMatch(x -> x.getIsin().equals(isin))) {
            ticker = tickers.stream().filter(x -> x.getIsin().contains(isin)).findFirst().get();
            this.isin = isin;
            offers = new LinkedHashMap<>();
            for(int i=5; i>0; i--) {
                offers.put("ASK" + i,null);
            }
            for(int i=1; i<6; i++) {
                offers.put("BID" + i,null);
            }
            pcs = new PropertyChangeSupport(this);
            filter.addPropertyChangeListener(this);
            filter.addTickersToFilter(Collections.singleton(ticker));
        } else {
            throw new IllegalArgumentException("ISIN not found: " + isin);
        }
    }

    public static QuoteLastInfo getInstance(String isin) {
        if (INSTANCES.containsKey(isin)) {
            return INSTANCES.get(isin);
        }
        QuoteLastInfo info = new QuoteLastInfo(isin);
        INSTANCES.put(isin, info);
        return info;
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        //System.out.println("quote incoming!");
        if (propertyChangeEvent.getPropertyName().equals("Quotes")) {
            NolRecentInfoAPI info = (NolRecentInfoAPI) propertyChangeEvent.getNewValue();

            if (info.getTicker().getIsin().equals(isin)) {
                Map<String, Boolean> bitMask = info.getBitMask();

                if (bitMask.get("ValoLT")) {
                    valoLT = info.getValoLT();
                }
                if (bitMask.get("VoLT")) {
                    voLT = info.getVoLT();
                }
                if (bitMask.get("ToLT")) {
                    tolt = info.getToLT();
                }
                if (bitMask.get("Open")) {
                    open = info.getOpen();
                }
                if (bitMask.get("High")) {
                    high = info.getHigh();
                }
                if (bitMask.get("Low")) {
                    low = info.getLow();
                }
                if (bitMask.get("Close")) {
                    close = info.getClose();
                }
                if (bitMask.get("Bid")) {
                    bid = info.getBid();
                }
                if (bitMask.get("Ask")) {
                    ask = info.getAsk();
                }
                if (bitMask.get("BidSize")) {
                    bidsize = info.getBidSize();
                }
                if (bitMask.get("AskSize")) {
                    asksize = info.getAskSize();
                }
                if (bitMask.get("TotalVolume")) {
                    totalVolume = info.getTotalVolume();
                }
                if (bitMask.get("TotalValue")) {
                    totalValue = info.getTotalValue();
                }
                if (bitMask.get("OpenInterest")) {
                    openInt = info.getOpenInterest();
                }
                if (bitMask.get("Phase")) {
                    phase = info.getPhase();
                }
                if (bitMask.get("Status")) {
                    status = info.getStatus();
                }
                if (bitMask.get("BidAmount")) {
                    bidAmount = info.getBidAmount();
                }
                if (bitMask.get("AskAmount")) {
                    askAmount = info.getAskAmount();
                }
                if (bitMask.get("OpenValue")) {
                    openVal = info.getOpenValue();
                }
                if (bitMask.get("CloseValue")) {
                    closeVal = info.getCloseValue();
                }
                if (bitMask.get("ReferPrice")) {
                    referPrice = info.getReferPrice();
                }
                if (bitMask.get("Offers")) {
                    for(NolBidAskTblAPI offer : info.getOffers()) {
                        offers.replace(offer.getSide().toString() + offer.getDepth(),offer);
                    }
                }
                pcs.firePropertyChange("QuoteLastInfo", null, this);
                //System.out.println("fired property change");
                //System.out.println(this.getHeader());
                //System.out.println(this.toString());
            }
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
        System.out.println("added listener");
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
