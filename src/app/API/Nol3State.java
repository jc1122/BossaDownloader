package app.API;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public enum Nol3State implements JnaEnum<Nol3State> {
    NOL3_CLOSED_WHILE_API_RUNNING {
        @Override
        public boolean allowInit() {
            return false;
        }
    }, // 1
    OFFLINE {
        @Override
        public boolean allowInit() {
            return false;
        }
    }, //= 2;
    ONLINE {
        @Override
        public boolean allowInit() {
            return true;
        }
    }, //= 3;
    NOT_RUNNING {
        @Override
        public boolean allowInit() {
            return false;
        }
    }, //= 4;
    NO_ORDERS {
        @Override
        public boolean allowInit() {
            return true;
        }
    }; //= 5;

    private static int start = 1;

    @Contract(pure = true)
    public int getIntValue() {
        return this.ordinal() + start;
    }

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
