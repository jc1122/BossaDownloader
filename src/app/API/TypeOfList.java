package app.API;

import app.API.nolObjects.NolTickerAPI;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Used to set search mode for {@link BossaAPI#getTickers(TypeOfList, NolTickerAPI)}
 */
@SuppressWarnings("unused")
public enum TypeOfList implements JnaEnum<TypeOfList> {
    /**
     * Get all refactoredTickerSelector
     */
    UNDEF_LIST {
        @Override
        public boolean isTickerFieldEmpty(NolTickerAPI ticker) {
            return false;
        }
    },// = -1;
    /**
     * Get all refactoredTickerSelector
     */
    ALL {
        @Override
        public boolean isTickerFieldEmpty(NolTickerAPI ticker) {
            return false;
        }
    }, //= 0;
    /**
     * Get refactoredTickerSelector which match name of given ticker
     */
    SYMBOL {
        @Override
        public boolean isTickerFieldEmpty(NolTickerAPI ticker) {
            return ticker.getName().equals("");
        }
    }, //= 1;
    /**
     * Get refactoredTickerSelector which match the ISIN of given ticker
     */
    ISIN {
        @Override
        public boolean isTickerFieldEmpty(NolTickerAPI ticker) {
            return ticker.getIsin().equals("");
        }
    }, //= 2;
    /**
     * Get refactoredTickerSelector which match the CFI of given ticker
     */
    CFI {
        @Override
        public boolean isTickerFieldEmpty(NolTickerAPI ticker) {
            return ticker.getCFI().equals("");
        }
    }, //= 3;
    /**
     * Get refactoredTickerSelector which match the market code of given ticker
     */
    MARKET_CODE {
        @Override
        public boolean isTickerFieldEmpty(NolTickerAPI ticker) {
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
     * Used to ckeck if getting list of refactoredTickerSelector is possible by
     * {@link BossaAPI#getTickers(TypeOfList, NolTickerAPI)}
     *
     * @param ticker whose fields you wish to check
     * @return {@code True} if given field of ticker is not empty
     */
    public abstract boolean isTickerFieldEmpty(NolTickerAPI ticker);
}
