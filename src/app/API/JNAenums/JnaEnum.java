package app.API.JNAenums;

/**
 * Maps C type JNAenums to Java JNAenums. See <a href="http://technofovea.com/blog/archives/815">here</a> for details.
 *
 * @param <T> mapped enum should implement JnaEnum
 */
//http://technofovea.com/blog/archives/815
public interface JnaEnum<T> {
    /**
     * Returns ordinal value
     *
     * @return ordinal value of enum
     */
    int getIntValue();

    /**
     * Convert ordinal to enum
     *
     * @param i ordinal
     * @return enum value
     */
    T getForValue(int i);
}
