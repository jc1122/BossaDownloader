package app.gui.dialog.TickerSelector;


import app.gui.dialog.GUIController;

public class SelectTickersController<K extends SelectTickersModel, L extends SelectTickersView> extends GUIController<K,L> {

    SelectTickersController(K model, Class<L> viewClass) {
        super(model, viewClass);
    }
}
