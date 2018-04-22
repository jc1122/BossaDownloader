package app.API;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public enum TypeofList implements JnaEnum<TypeofList> {
    UndefList {
        @Override
        public boolean isTickerFieldEmpty(BossaAPI.NolTickerAPI ticker) {
            return false;
        }
    },// = -1;
    All {
        @Override
        public boolean isTickerFieldEmpty(BossaAPI.NolTickerAPI ticker) {
            return false;
        }
    }, //= 0;
    Symbol {
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
    MarketCode {
        @Override
        public boolean isTickerFieldEmpty(BossaAPI.NolTickerAPI ticker) {
            return ticker.getMarketCode().equals("");
        }
    }; //= 4;

    private static int start = -1;

    @Contract(pure = true)
    public int getIntValue() {
        return this.ordinal() + start;
    }

    @Nullable
    public TypeofList getForValue(int i) {
        for (TypeofList o : TypeofList.values()) {
            if (o.getIntValue() == i) {
                return o;
            }
        }
        return null;
    }

    public abstract boolean isTickerFieldEmpty(BossaAPI.NolTickerAPI ticker);
}
