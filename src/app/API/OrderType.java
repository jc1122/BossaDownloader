package app.API;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

//http://technofovea.com/blog/archives/815
public enum OrderType implements JnaEnum<OrderType> {
    Undef, //= -1;
    NewOrder, //= 0;
    ModOrder, //= 1;
    DelOrder, //= 2;
    StatOrder; //= 3;

    @SuppressWarnings("FieldCanBeLocal")
    private static int start = -1;

    @Contract(pure = true)
    public int getIntValue() {
        return this.ordinal() + start;
    }

    @Nullable
    public OrderType getForValue(int i) {
        for (OrderType o : OrderType.values()) {
            if (o.getIntValue() == i) {
                return o;
            }
        }
        return null;
    }
}
