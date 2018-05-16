package app.gui.tickers;

import app.API.BossaAPI;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TickerTableModel extends AbstractTableModel {

    private List<BossaAPI.NolTickerAPI> tickers;

    public void setData(List<BossaAPI.NolTickerAPI> tickers) {
        this.tickers = tickers;
    }

    public List<BossaAPI.NolTickerAPI> getData() {
        return tickers;
    }

    TickerTableModel(List<BossaAPI.NolTickerAPI> tickers) {
        this.tickers = tickers;
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

    public void addRow(BossaAPI.NolTickerAPI ticker) {
        tickers.add(ticker);
        this.fireTableDataChanged();
    }

    public BossaAPI.NolTickerAPI removeRow(int row) {
        BossaAPI.NolTickerAPI ticker = tickers.remove(row);
        this.fireTableDataChanged();
        return ticker;
    }

    public BossaAPI.NolTickerAPI getRow(int row) {
        return tickers.get(row);
    }

}
