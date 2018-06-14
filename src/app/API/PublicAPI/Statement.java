package app.API.PublicAPI;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface Statement {
    @NotNull String getName();

    boolean getIke();

    @NotNull String getType();

    @Contract(pure = true)
    Map<String, Double> getFundMap();

    @Contract(pure = true)
    List<Position> getPositions();
}
