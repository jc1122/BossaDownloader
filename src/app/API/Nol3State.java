package app.API;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public enum Nol3State implements JnaEnum<Nol3State> {
    /**
     * NOL3 is not running
     */
    NOL3_CLOSED_WHILE_API_RUNNING {
        @Override
        public boolean allowInit() {
            return false;
        }
    }, // 1
    /**
     * NOL3 is running but disconnected
     */
    OFFLINE {
        @Override
        public boolean allowInit() {
            return false;
        }
    }, //= 2;
    /**
     * NOL3 is running and connected to server
     */
    ONLINE {
        @Override
        public boolean allowInit() {
            return true;
        }
    }, //= 3;
    /**
     * NOL3 is not running
     */
    NOT_RUNNING {
        @Override
        public boolean allowInit() {
            return false;
        }
    }, //= 4;
    /**
     * NOL3 is running, but no order placement is possible
     */
    NO_ORDERS {
        @Override
        public boolean allowInit() {
            return true;
        }
    }; //= 5;

    @SuppressWarnings("FieldCanBeLocal")
    private static int start = 1;

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
    public Nol3State getForValue(int i) {
        for (Nol3State o : Nol3State.values()) {
            if (o.getIntValue() == i) {
                return o;
            }
        }
        return null;
    }

    public abstract boolean allowInit();
}
