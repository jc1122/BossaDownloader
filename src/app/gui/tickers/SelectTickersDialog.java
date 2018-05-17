package app.gui.tickers;

import app.API.BossaAPI;
import app.API.TypeOfList;
import app.gui.Model;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectTickersDialog {
    //TODO add functionality
    public SelectTickersDialog(Model model) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Tickers to watch");

        //TODO refactor this, possibly in BossaAPI, to many unnecessary conversions
        Set<BossaAPI.NolTickerAPI> allTickers = new HashSet<>(model.getTickers(TypeOfList.ALL, null));
        allTickers.removeAll(model.getTickersInFilter());

        List<BossaAPI.NolTickerAPI> tickers = new ArrayList<>(allTickers);
        List<BossaAPI.NolTickerAPI> tickersInFilter = new ArrayList<>(model.getTickersInFilter());

        //TODO add tickers in filter to model and to this dialog and display them in window
        ButtonPane buttonPane = new ButtonPane(model);
        TickerTablesPane tickerTablesPane = new TickerTablesPane(buttonPane.getSearchField(), tickers, tickersInFilter);
        buttonPane.setTickerTablesPane(tickerTablesPane);

        //setBorder(pane);

        dialog.setLayout(new BorderLayout());

        dialog.add(tickerTablesPane.getPane(), BorderLayout.CENTER);
        dialog.add(buttonPane.getPane(), BorderLayout.PAGE_END);

        dialog.pack();
        SwingUtilities.invokeLater(() ->
                dialog.setVisible(true));

        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}
