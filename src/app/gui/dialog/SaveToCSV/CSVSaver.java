package app.gui.dialog.SaveToCSV;

import app.API.JNAinterface.NolRecentInfoAPI;
import app.API.PublicAPI.Ticker;
import app.gui.dialog.QuoteDialog.QuoteLastInfo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.*;

public class CSVSaver implements PropertyChangeListener {

    private Set<Ticker> tickersToSave = new HashSet<>();
    private boolean saveToFile = false;
    Set<QuoteLastInfo> observedQuotes;
    public CSVSaver() {
        observedQuotes = new HashSet<>();
        observedQuotes.add(QuoteLastInfo.getInstance("PL0GF0014183")); //FW20
        observedQuotes.add(QuoteLastInfo.getInstance("PL9999999987")); //WIG20
        observedQuotes.add(QuoteLastInfo.getInstance("PLALIOR00045")); //ALIOR
        observedQuotes.add(QuoteLastInfo.getInstance("PLBZ00000044")); //BZWBK
        observedQuotes.add(QuoteLastInfo.getInstance("PLCCC0000016")); //CCC
        observedQuotes.add(QuoteLastInfo.getInstance("PLOPTTC00011")); //CDProjekt
        observedQuotes.add(QuoteLastInfo.getInstance("PLCFRPT00013")); //polsat
        observedQuotes.add(QuoteLastInfo.getInstance("PLENERG00022")); //energa
        observedQuotes.add(QuoteLastInfo.getInstance("PLEURCH00011")); //eurocash
        observedQuotes.add(QuoteLastInfo.getInstance("PLJSW0000015")); //kghm
        observedQuotes.add(QuoteLastInfo.getInstance("PLLOTOS00025")); //lotos
        observedQuotes.add(QuoteLastInfo.getInstance("PLLPP0000011")); //lpp
        observedQuotes.add(QuoteLastInfo.getInstance("PLBRE0000012")); //mbank
        observedQuotes.add(QuoteLastInfo.getInstance("PLTLKPL00017")); //orange
        observedQuotes.add(QuoteLastInfo.getInstance("PLPEKAO00016")); //pekao
        observedQuotes.add(QuoteLastInfo.getInstance("PLPGER000010")); //pge
        observedQuotes.add(QuoteLastInfo.getInstance("PLPGNIG00014")); //pgnig
        observedQuotes.add(QuoteLastInfo.getInstance("PLPKN0000018")); //pknorlen
        observedQuotes.add(QuoteLastInfo.getInstance("PLPKO0000016")); //pkobp
        observedQuotes.add(QuoteLastInfo.getInstance("PLPZU0000011")); //pzu
        observedQuotes.add(QuoteLastInfo.getInstance("PLTAURN00011")); //tauronpe
        observedQuotes.forEach(quote -> quote.addPropertyChangeListener(this));
        observedQuotes.forEach(this::writeToCSV);
    }
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if(true) { //saveToFile
            System.out.println("here!");
            if (Objects.equals(propertyChangeEvent.getPropertyName(), "QuoteLastInfo")) {
                QuoteLastInfo quote = (QuoteLastInfo) propertyChangeEvent.getNewValue();
                writeToCSV(quote);
            }
        }
    }

    public void startSaving(Set<Ticker> tickers) {
        this.tickersToSave = tickers;
        saveToFile = true;
    }

    public void stopSaving() {
        saveToFile = false;
    }

    //this method needs to be synchronized by opened file
    public void writeToCSV(QuoteLastInfo quote) {

        String name = quote.getName();
        LocalDate localDate = LocalDate.now();
        String fileName = name + localDate + ".csv";
        File quotesFile = new File(fileName);
        List<String> quoteToString = null;
        System.out.println(quote);
        quoteToString = Arrays.asList(quote.toString());

        if(!quotesFile.exists()) {
            List<String> header = Arrays.asList(quote.getHeader());
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
