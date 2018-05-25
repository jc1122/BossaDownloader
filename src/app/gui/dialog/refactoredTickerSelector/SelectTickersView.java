package app.gui.dialog.refactoredTickerSelector;


import app.gui.dialog.GUIView;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;

public class SelectTickersView<K extends SelectTickersModel,
        L extends SelectTickersView<K, L, M>,
        M extends SelectTickersController<K, L>> extends GUIView<K,L,M> {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    @Override
    public void createGUI() {
        dialog.setTitle("Tickers to watch");
    }

    SelectTickersView(M controller, K model) {
        super(controller, model);
        ButtonPane buttonPane = new ButtonPane(model, dialog);
        TickerTablesPane tickerTablesPane = new TickerTablesPane(buttonPane.getSearchField(), model.getTickers(), model.getTickersInFilter());

        buttonPane.setTickerTablesPane(tickerTablesPane);

        dialog.setLayout(new BorderLayout());

        dialog.add(tickerTablesPane.getPane(), BorderLayout.CENTER);
        dialog.add(buttonPane.getPane(), BorderLayout.PAGE_END);

        dialog.pack();
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}
