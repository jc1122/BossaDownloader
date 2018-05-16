package app.gui.tickers;

import app.API.BossaAPI;

import javax.swing.*;
import javax.swing.border.Border;
import java.util.List;

public class TickerTable {
    JScrollPane scrollPane;
    JTable table;

    private JScrollPane createTable(List<BossaAPI.NolTickerAPI> tickers) {


        TickerTableModel model = new TickerTableModel(tickers);

        table = new JTable(model);

        JScrollPane scrollPane2 = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        return scrollPane2;
    }

    public TickerTable(List<BossaAPI.NolTickerAPI> tickers, String title) {
        scrollPane = createTable(tickers);
        Border innerBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );
        Border completeBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                innerBorder);
        scrollPane.setBorder(completeBorder);
        //setBorder(scrollPane);
    }

    public JScrollPane getPane() {
        return scrollPane;
    }

    public JTable getTable() {
        return table;
    }

    public TickerTableModel getModel() {
        return (TickerTableModel) table.getModel();
    }

}
