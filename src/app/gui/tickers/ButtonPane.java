package app.gui.tickers;

import javax.swing.*;
import app.gui.Model;
import java.util.HashSet;

public class ButtonPane {
    private JPanel totalPane = new JPanel();
    private JPanel filterPane = new JPanel();
    private BoxLayout totalPaneLayout = new BoxLayout(totalPane, BoxLayout.Y_AXIS);
    private JTextField searchField;

    private JButton okButton = new JButton("OK");
    private JButton cancelButton = new JButton("Cancel");
    private JButton saveButton = new JButton("Save...");
    private JButton loadButton = new JButton("Load...");
    private TickerTablesPane tickerTablesPane;

    JPanel buttonPane = new JPanel();

    public void setTickerTablesPane(TickerTablesPane tickerTablesPane) {
        this.tickerTablesPane = tickerTablesPane;
    }

    public ButtonPane(Model model, JDialog dialog) {
        buttonPane.add(okButton);
        buttonPane.add(cancelButton);
        buttonPane.add(saveButton);
        buttonPane.add(loadButton);

        okButton.addActionListener(e -> {
                model.clearFilter();
                model.addTickersToFilter(new HashSet<>(tickerTablesPane.getTickersInFilter()));
                dialog.dispose();
        }
                );
        cancelButton.addActionListener( e -> {
            dialog.dispose();

        });

        filterPane.add(new JLabel("Search text: "));
        searchField = new JTextField();
        searchField.setColumns(30);
        filterPane.add(searchField);

        totalPane.setLayout(totalPaneLayout);
        totalPane.add(filterPane);
        totalPane.add(buttonPane);
    }

    public JPanel getPane() {
        return totalPane;
    }

    public JTextField getSearchField() {
        return searchField;
    }
}
