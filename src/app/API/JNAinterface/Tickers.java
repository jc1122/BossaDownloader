package app.API.JNAinterface;

import app.API.JNAenums.TypeOfList;
import app.API.PublicAPI.PropertyAPI;
import app.API.PublicAPI.Ticker;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * property class for tickers
 */
public class Tickers extends PropertyAPI<Set<Ticker>, String> {

    private static Tickers INSTANCE = new Tickers();

    public static Tickers getInstance() {
        return INSTANCE;
    }

    private Tickers() {
        super("Tickers");
    }

    @Override
    public Set<Ticker> getValue() {
        if (property == null) {
            property = Collections.unmodifiableSet(new HashSet<>(NolTickersAPI.getTickers(TypeOfList.ALL, null)));
        }
        return super.getValue();
    }
}
