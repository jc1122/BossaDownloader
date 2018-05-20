package app.gui.tickers;

import app.API.BossaAPI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.util.List;
import java.util.logging.Logger;

public class TickerTablesPane {
    private static final Logger logger =
            Logger.getLogger(TickerTablesPane.class.getName());
    private JPanel pane;
    private TickerTable left, right;

    private void setTableSorter(JTable table) {
        logger.entering(this.getClass().getName(),"setTableSorter", table);
        table.setAutoCreateRowSorter(true);
        TableRowSorter<TickerTableModel> sorter = new TableRowSorter<>((TickerTableModel) table.getModel());
        table.setRowSorter(sorter);
        logger.exiting(this.getClass().getName(),"setTableSorter");
    }

    private void newFilter(String text) {
        logger.entering(this.getClass().getName(),"newFilter", text);
        RowFilter<TickerTableModel, Object> rf;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(text);
        } catch (java.util.regex.PatternSyntaxException e) {
            logger.finest("expression does not parse");
            return;
        }
        //these casts are safe
        //noinspection unchecked
        TableRowSorter<TickerTableModel> leftSorter = (TableRowSorter<TickerTableModel>) (left.getTable().getRowSorter());
        //noinspection unchecked
        TableRowSorter<TickerTableModel> rightSorter = (TableRowSorter<TickerTableModel>) (right.getTable().getRowSorter());
        leftSorter.setRowFilter(rf);
        rightSorter.setRowFilter(rf);
        logger.exiting(this.getClass().getName(),"newFilter");
    }

    TickerTablesPane(JTextField filterText, List<BossaAPI.NolTickerAPI> tickers, List<BossaAPI.NolTickerAPI> tickersInFilter) {
        Object[] params = {filterText, tickers, tickersInFilter};
        logger.entering(this.getClass().getName(),"constructor", params);

        pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));

        left = new TickerTable(tickers, "Tickers to choose from:");
        setTableSorter(left.getTable());

        right = new TickerTable(tickersInFilter, "Tickers in filter:");
        setTableSorter(right.getTable());

        pane.add(left.getPane());
        pane.add(new MoveButtonPane(left.getTable(), right.getTable()).getPanel());
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
        logger.exiting(this.getClass().getName(),"constructor");
    }

    public JPanel getPane() {
        return pane;
    }

    public List<BossaAPI.NolTickerAPI> getTickersInFilter() {
        return right.getModel().getData();
    }
}
