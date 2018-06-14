package app.API.PublicAPI;

import org.jetbrains.annotations.NotNull;

public interface Ticker {
    @NotNull String getIsin();

    @NotNull String getName();

    @NotNull String getMarketCode();

    @NotNull String getCFI();

    @NotNull String getGroup();
}
