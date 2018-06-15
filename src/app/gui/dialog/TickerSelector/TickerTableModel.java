package app.gui.dialog.TickerSelector;

import app.API.PublicAPI.Ticker;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.logging.Logger;

public class TickerTableModel extends AbstractTableModel {
    private static final Logger logger =
            Logger.getLogger(TickerTableModel.class.getName());
    private List<Ticker> tickers;

    public void setData(List<Ticker> tickers) {
        logger.entering(this.getClass().getName(), "setData", tickers);
        this.tickers = tickers;
        logger.exiting(this.getClass().getName(), "setData");
    }

    public List<Ticker> getData() {
        return tickers;
    }

    public TickerTableModel(List<Ticker> tickers) {
        logger.entering(this.getClass().getName(), "constructor", tickers);
        this.tickers = tickers;
        logger.exiting(this.getClass().getName(), "constructor");
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return String.class;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Name";
            case 1:
                return "ISIN";
            case 2:
                return "CFI";
            case 3:
                return "Group";
            case 4:
                return "Market Code";
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return tickers.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return tickers.get(rowIndex).getName();
            case 1:
                return tickers.get(rowIndex).getIsin();
            case 2:
                return tickers.get(rowIndex).getCFI();
            case 3:
                return tickers.get(rowIndex).getGroup();
            case 4:
                return tickers.get(rowIndex).getMarketCode();
        }
        return null;
    }

    void addRow(Ticker ticker) {
        logger.entering(this.getClass().getName(), "addRow", ticker);
        tickers.add(ticker);
        this.fireTableDataChanged();
        logger.exiting(this.getClass().getName(), "addRow");
    }

    Ticker removeRow(int row) {
        logger.entering(this.getClass().getName(), "removeRow", row);
        Ticker ticker = tickers.remove(row);
        this.fireTableDataChanged();
        logger.exiting(this.getClass().getName(), "removeRow", ticker);
        return ticker;
    }

    @SuppressWarnings("unused")
    Ticker getRow(int row) {
        return tickers.get(row);
    }

}
