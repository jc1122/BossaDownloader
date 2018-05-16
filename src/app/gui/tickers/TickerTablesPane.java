package app.gui.tickers;

import app.API.BossaAPI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;

public class TickerTablesPane {
    private JPanel pane;
    TickerTable left, right;

    private void setTableSorter(JTable table) {
        table.setAutoCreateRowSorter(true);
        TableRowSorter<TickerTableModel> sorter = new TableRowSorter<>((TickerTableModel) table.getModel());
        table.setRowSorter(sorter);
    }

    private void newFilter(String text) {
        RowFilter<TickerTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(text);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        TableRowSorter<TickerTableModel> leftSorter = (TableRowSorter<TickerTableModel>) (left.getTable().getRowSorter());
        TableRowSorter<TickerTableModel> rightSorter = (TableRowSorter<TickerTableModel>) (right.getTable().getRowSorter());
        leftSorter.setRowFilter(rf);
        rightSorter.setRowFilter(rf);
    }

    TickerTablesPane(JTextField filterText, List<BossaAPI.NolTickerAPI> tickers) {
        List<BossaAPI.NolTickerAPI> tickersToFilter = new ArrayList<>();

        pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));


        left = new TickerTable(tickers, "Tickers to choose from:");
        setTableSorter(left.getTable());

        right = new TickerTable(tickersToFilter, "Tickers in filter:");
        setTableSorter(right.getTable());
        JTable rightTable = right.getTable();

        pane.add(left.getPane());
        pane.add(new MoveButtonPane(left.getTable(), rightTable).getPanel());
        pane.add(right.getPane());

        filterText.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        newFilter(filterText.getText());
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        newFilter(filterText.getText());
                    }

                    public void removeUpdate(DocumentEvent e) {
                        newFilter(filterText.getText());
                    }
                });
    }

    public JPanel getPane() {
        return pane;
    }

}
