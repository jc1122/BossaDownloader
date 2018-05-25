package app.gui.dialog.SaveToCSV;

import app.API.BossaAPI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

public class CSVSaver implements PropertyChangeListener {

    Set<BossaAPI.NolTickerAPI> tickersToSave = new HashSet<>();
    boolean saveToFile = false;

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if(saveToFile) {
            if (propertyChangeEvent.getPropertyName() == "Quotes") {
                BossaAPI.NolRecentInfoAPI quote = (BossaAPI.NolRecentInfoAPI) propertyChangeEvent.getNewValue();
                if (tickersToSave.contains(quote.getTicker())) {
                    writeToCSV(quote);
                }
            }
        }
    }

    public void startSaving(Set<BossaAPI.NolTickerAPI> tickers) {
        this.tickersToSave = tickers;
        saveToFile = true;
    }

    public void stopSaving() {
        saveToFile = false;
    }

    public void writeToCSV(BossaAPI.NolRecentInfoAPI quote) {
        //TODO add functionality to write to file
        System.out.println("writing to file!");
    }
}
