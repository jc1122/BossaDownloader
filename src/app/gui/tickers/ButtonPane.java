package app.gui.tickers;

import javax.swing.*;

public class ButtonPane {
    JPanel totalPane = new JPanel();
    JPanel filterPane = new JPanel();
    BoxLayout totalPaneLayout = new BoxLayout(totalPane, BoxLayout.Y_AXIS);
    JTextField searchField;

    JPanel buttonPane = new JPanel();

    public ButtonPane() {
        buttonPane.add(new JButton("OK"));
        buttonPane.add(new JButton("Cancel"));
        buttonPane.add(new JButton("Save..."));
        buttonPane.add(new JButton("Load..."));

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
