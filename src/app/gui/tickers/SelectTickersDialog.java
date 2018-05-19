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

/**
 * You need to {@link SelectTickersDialog#getDialog()} then {@link JDialog#setVisible(boolean)}
 * manually to display the dialog.
 */
public class SelectTickersDialog {

    JDialog dialog;
    public JDialog getDialog() {
        return dialog;
    }
    public SelectTickersDialog(Model model) {
        dialog = new JDialog();
        dialog.setTitle("Tickers to watch");

        //TODO refactor this, possibly in BossaAPI, to many unnecessary conversions
        Set<BossaAPI.NolTickerAPI> allTickers = new HashSet<>(model.getTickers(TypeOfList.ALL, null));
        allTickers.removeAll(model.getTickersInFilter());

        List<BossaAPI.NolTickerAPI> tickers = new ArrayList<>(allTickers);
        List<BossaAPI.NolTickerAPI> tickersInFilter = new ArrayList<>(model.getTickersInFilter());

        ButtonPane buttonPane = new ButtonPane(model, dialog);
        TickerTablesPane tickerTablesPane = new TickerTablesPane(buttonPane.getSearchField(), tickers, tickersInFilter);
        buttonPane.setTickerTablesPane(tickerTablesPane);

        //setBorder(pane);

        dialog.setLayout(new BorderLayout());

        dialog.add(tickerTablesPane.getPane(), BorderLayout.CENTER);
        dialog.add(buttonPane.getPane(), BorderLayout.PAGE_END);

        dialog.pack();
//        SwingUtilities.invokeLater(() ->
//                dialog.setVisible(true));

        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}
