package app.API;

import app.API.enums.JnaEnum;
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
    },// = -1;
    /**
     * Get all refactoredTickerSelector
     */
    ALL {
    }, //= 0;
    /**
     * Get refactoredTickerSelector which match name of given ticker
     */
    SYMBOL {
    }, //= 1;
    /**
     * Get refactoredTickerSelector which match the ISIN of given ticker
     */
    ISIN {
    }, //= 2;
    /**
     * Get refactoredTickerSelector which match the CFI of given ticker
     */
    CFI {
    }, //= 3;
    /**
     * Get refactoredTickerSelector which match the market code of given ticker
     */
    MARKET_CODE {
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

}
