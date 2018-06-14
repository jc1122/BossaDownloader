package app.API.PublicAPI;

import org.jetbrains.annotations.NotNull;

public interface Position {
    @NotNull Ticker getTicker();

    int getAcc110();

    int getAcc120();

    double getValue();
}
