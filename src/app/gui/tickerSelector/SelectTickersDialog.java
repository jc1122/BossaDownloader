package app.gui.tickerSelector;

import app.API.PublicAPI.Ticker;
import app.gui.Model;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * You need to {@link SelectTickersDialog#getDialog()} then {@link JDialog#setVisible(boolean)}
 * manually to display the dialog.
 */
//TODO refactor to MVC
public class SelectTickersDialog {
    private static final Logger logger =
            Logger.getLogger(SelectTickersDialog.class.getName());
    private final JDialog dialog;

    public JDialog getDialog() {
        return dialog;
    }

    public SelectTickersDialog(Model model) {
        logger.entering(this.getClass().getName(), "constructor", model);
        dialog = new JDialog();
        dialog.setTitle("Tickers to watch");

        //TODO refactor this, possibly in BossaAPI, too many unnecessary conversions
        Set<Ticker> allTickers = model.getTickers();
        allTickers.removeAll(model.getTickersInFilter());

        List<Ticker> tickers = new ArrayList<>(allTickers);
        List<Ticker> tickersInFilter = new ArrayList<>(model.getTickersInFilter());

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
        logger.exiting(this.getClass().getName(), "constructor");
    }
}
