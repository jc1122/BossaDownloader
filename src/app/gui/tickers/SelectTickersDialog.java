package app.gui.tickers;

import app.API.BossaAPI;
import app.API.TypeOfList;
import app.gui.Model;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SelectTickersDialog {
    //TODO add functionality
    public SelectTickersDialog(Model model) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Tickers to watch");
        List<BossaAPI.NolTickerAPI> tickers = model.getTickers(TypeOfList.ALL, null);
        //TODO add tickers in filter to model and to this dialog and display them in window
        ButtonPane buttonPane = new ButtonPane();

        TickerTablesPane tickerTablesPane = new TickerTablesPane(buttonPane.getSearchField(), tickers);
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
