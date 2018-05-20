package app.API;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Used to set search mode for {@link BossaAPI#getTickers(TypeOfList, BossaAPI.NolTickerAPI)}
 */
@SuppressWarnings("unused")
public enum TypeOfList implements JnaEnum<TypeOfList> {
    /**
     * Get all tickerSelector
     */
    UNDEF_LIST {
        @Override
        public boolean isTickerFieldEmpty(BossaAPI.NolTickerAPI ticker) {
            return false;
        }
    },// = -1;
    /**
     * Get all tickerSelector
     */
    ALL {
        @Override
        public boolean isTickerFieldEmpty(BossaAPI.NolTickerAPI ticker) {
            return false;
        }
    }, //= 0;
    /**
     * Get tickerSelector which match name of given ticker
     */
    SYMBOL {
        @Override
        public boolean isTickerFieldEmpty(BossaAPI.NolTickerAPI ticker) {
            return ticker.getName().equals("");
        }
    }, //= 1;
    /**
     * Get tickerSelector which match the ISIN of given ticker
     */
    ISIN {
        @Override
        public boolean isTickerFieldEmpty(BossaAPI.NolTickerAPI ticker) {
            return ticker.getIsin().equals("");
        }
    }, //= 2;
    /**
     * Get tickerSelector which match the CFI of given ticker
     */
    CFI {
        @Override
        public boolean isTickerFieldEmpty(BossaAPI.NolTickerAPI ticker) {
            return ticker.getCFI().equals("");
        }
    }, //= 3;
    /**
     * Get tickerSelector which match the market code of given ticker
     */
    MARKET_CODE {
        @Override
        public boolean isTickerFieldEmpty(BossaAPI.NolTickerAPI ticker) {
            return ticker.getMarketCode().equals("");
        }
    }; //= 4;

    @SuppressWarnings("FieldCanBeLocal")
    private static int start = -1;

    @Override
    @Contract(pure = true)
    public int getIntValue() {
        return this.ordinal() + start;
    }

    @Override
    @Nullable
    public TypeOfList getForValue(int i) {
        for (TypeOfList o : TypeOfList.values()) {
            if (o.getIntValue() == i) {
                return o;
            }
        }
        return null;
    }

    /**
     * Used to ckeck if getting list of tickerSelector is possible by
     * {@link BossaAPI#getTickers(TypeOfList, BossaAPI.NolTickerAPI)}
     *
     * @param ticker whose fields you wish to check
     * @return {@code True} if given field of ticker is not empty
     */
    public abstract boolean isTickerFieldEmpty(BossaAPI.NolTickerAPI ticker);
}
