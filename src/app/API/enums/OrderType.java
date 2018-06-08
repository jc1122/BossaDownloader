package app.API.enums;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Type of order request
 */
//http://technofovea.com/blog/archives/815
public enum OrderType implements JnaEnum<OrderType> {
    Undef, //= -1;
    /**
     * Place new order
     */
    NewOrder, //= 0;
    /**
     * Modify existing order
     */
    ModOrder, //= 1;
    /**
     * Delete pending order
     */
    DelOrder, //= 2;
    /**
     * Check order status
     */
    StatOrder; //= 3;

    @SuppressWarnings("FieldCanBeLocal")
    private static final int start = -1;

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract(pure = true)
    public int getIntValue() {
        return this.ordinal() + start;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
