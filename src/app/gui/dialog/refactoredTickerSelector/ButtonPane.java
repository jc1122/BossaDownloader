package app.gui.dialog.refactoredTickerSelector;

import app.gui.Model;

import javax.swing.*;
import java.util.HashSet;
import java.util.logging.Logger;

class ButtonPane {
    private static final Logger logger =
            Logger.getLogger(ButtonPane.class.getName());

    private final JPanel totalPane = new JPanel();
    private final JTextField searchField;

    private TickerTablesPane tickerTablesPane;

    void setTickerTablesPane(TickerTablesPane tickerTablesPane) {
        logger.entering(this.getClass().getName(), "setTickerTablesPane", tickerTablesPane);
        this.tickerTablesPane = tickerTablesPane;
        logger.exiting(this.getClass().getName(), "setTickerTablesPane");
    }

    public ButtonPane(SelectTickersModel model, JDialog dialog) {
        Object[] params = {model, dialog};
        logger.entering(this.getClass().getName(), "constructor", params);
        JButton okButton = new JButton("OK");
        JPanel buttonPane = new JPanel();
        buttonPane.add(okButton);
        JButton cancelButton = new JButton("Cancel");
        buttonPane.add(cancelButton);
        JButton saveButton = new JButton("Save...");
        buttonPane.add(saveButton);
        JButton loadButton = new JButton("Load...");
        buttonPane.add(loadButton);

        okButton.addActionListener(e -> {
                    model.clearFilter();
            if (tickerTablesPane.getTickersInFilter().size() > 0) {
                model.addTickersToFilter(new HashSet<>(tickerTablesPane.getTickersInFilter()));
            }
                    dialog.dispose();
                }
        );
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel filterPane = new JPanel();
        filterPane.add(new JLabel("Search text: "));
        searchField = new JTextField();
        searchField.setColumns(30);
        filterPane.add(searchField);

        BoxLayout totalPaneLayout = new BoxLayout(totalPane, BoxLayout.Y_AXIS);
        totalPane.setLayout(totalPaneLayout);
        totalPane.add(filterPane);
        totalPane.add(buttonPane);
        logger.exiting(this.getClass().getName(), "constructor");
    }

    public JPanel getPane() {
        return totalPane;
    }

    JTextField getSearchField() {
        return searchField;
    }
}
