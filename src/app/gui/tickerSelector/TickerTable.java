package app.gui.tickerSelector;

import app.API.JNAinterface.NolTickerAPI;

import javax.swing.*;
import javax.swing.border.Border;
import java.util.List;
import java.util.logging.Logger;

public class TickerTable {
    private static final Logger logger =
            Logger.getLogger(TickerTable.class.getName());
    private final JScrollPane scrollPane;
    private JTable table;

    private JScrollPane createTable(List<NolTickerAPI> tickers) {
        logger.entering(this.getClass().getName(), "createTable", tickers);
        TickerTableModel model = new TickerTableModel(tickers);

        table = new JTable(model);

        JScrollPane scrollPane2 = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        logger.exiting(this.getClass().getName(), "createTable", scrollPane2);
        return scrollPane2;
    }

    public TickerTable(List<NolTickerAPI> tickers, String title) {
        Object[] params = {tickers, title};
        logger.entering(this.getClass().getName(), "constructor", params);
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
        logger.entering(this.getClass().getName(), "constructor");
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
