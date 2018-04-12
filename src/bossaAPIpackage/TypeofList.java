package bossaAPIpackage;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public enum TypeofList implements JnaEnum<TypeofList> {
    Undef,// = -1;
    All, //= 0;
    Symbol, //= 1;
    ISIN, //= 2;
    CFI, //= 3;
    MarketCode; //= 4;

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
}
