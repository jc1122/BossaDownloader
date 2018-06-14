package app.API.JNAinterface;

import app.API.PublicAPI.Ticker;
import org.jetbrains.annotations.NotNull;

public interface Position {
    @NotNull Ticker getTicker();

    int getAcc110();

    int getAcc120();

    double getValue();
}
