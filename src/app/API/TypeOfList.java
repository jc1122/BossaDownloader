package app.API;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public enum TypeOfList implements JnaEnum<TypeOfList> {
    UNDEF_LIST {
        @Override
        public boolean isTickerFieldEmpty(BossaAPI.NolTickerAPI ticker) {
            return false;
        }
    },// = -1;
    ALL {
        @Override
        public boolean isTickerFieldEmpty(BossaAPI.NolTickerAPI ticker) {
            return false;
        }
    }, //= 0;
    SYMBOL {
        @Override
        public boolean isTickerFieldEmpty(BossaAPI.NolTickerAPI ticker) {
            return ticker.getName().equals("");
        }
    }, //= 1;
    ISIN {
        @Override
        public boolean isTickerFieldEmpty(BossaAPI.NolTickerAPI ticker) {
            return ticker.getIsin().equals("");
        }
    }, //= 2;
    CFI {
        @Override
        public boolean isTickerFieldEmpty(BossaAPI.NolTickerAPI ticker) {
            return ticker.getCFI().equals("");
        }
    }, //= 3;
    MARKET_CODE {
        @Override
        public boolean isTickerFieldEmpty(BossaAPI.NolTickerAPI ticker) {
            return ticker.getMarketCode().equals("");
        }
    }; //= 4;

    @SuppressWarnings("FieldCanBeLocal")
    private static int start = -1;

    @Contract(pure = true)
    public int getIntValue() {
        return this.ordinal() + start;
    }

    @Nullable
    public TypeOfList getForValue(int i) {
        for (TypeOfList o : TypeOfList.values()) {
            if (o.getIntValue() == i) {
                return o;
            }
        }
        return null;
    }

    public abstract boolean isTickerFieldEmpty(BossaAPI.NolTickerAPI ticker);
}
