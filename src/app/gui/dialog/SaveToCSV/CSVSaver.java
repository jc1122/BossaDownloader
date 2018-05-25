package app.gui.dialog.SaveToCSV;

import app.API.BossaAPI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CSVSaver implements PropertyChangeListener {

    Set<String> tickerISINSToSave = new HashSet<>();
    boolean saveToFile = false;

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if(saveToFile) {
            if (propertyChangeEvent.getPropertyName() == "Quotes") {
                BossaAPI.NolRecentInfoAPI quote = (BossaAPI.NolRecentInfoAPI) propertyChangeEvent.getNewValue();
                if (tickerISINSToSave.contains(quote.getTicker().getIsin())) {
                    writeToCSV(quote);
                }
            }
        }
    }

    public void startSaving(Set<BossaAPI.NolTickerAPI> tickers) {
        this.tickerISINSToSave = tickers.stream().map(BossaAPI.NolTickerAPI::getIsin).collect(Collectors.toSet());
        saveToFile = true;
    }

    public void stopSaving() {
        saveToFile = false;
    }

    //this method needs to be synchronized by opened file
    public void writeToCSV(BossaAPI.NolRecentInfoAPI quote) {

        String name = quote.getTicker().getName();
        LocalDate localDate = LocalDate.now();
        String fileName = name + localDate + ".csv";
        File quotesFile = new File(fileName);

        List<String> quoteToString = Arrays.asList("this is a test");

        if(!quotesFile.exists()) {
            List<String> header = Arrays.asList("test,test,test,test");
            try {
                Files.write(Paths.get(fileName), header, StandardCharsets.UTF_8);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        try(Scanner lineScanner = new Scanner(quotesFile)) {
            while(lineScanner.hasNextLine()) {
                String line = lineScanner.nextLine();
                if (line.equals(quoteToString.get(0))) {
                    lineScanner.close();
                    return;
                }
            }
            Files.write(Paths.get(fileName), quoteToString, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("writing to file!");

    }
}
