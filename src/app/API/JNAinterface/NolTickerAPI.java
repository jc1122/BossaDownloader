package app.API.JNAinterface;

import app.API.PublicAPI.Ticker;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Stores data of single ticker. Each field is a {@link String}.
 */
final class NolTickerAPI extends BossaAPIClassWrapper<NolTickerAPI, BossaAPIInterface.NolTicker> implements Ticker {
    private final String isin;
    private final String name;
    private final String marketCode;
    private final String cfi;
    private final String group;

    NolTickerAPI(BossaAPIInterface.NolTicker nolTicker) {
        super(nolTicker);
        isin = new String(nolTicker.Isin).trim();
        name = new String(nolTicker.Name).trim();
        marketCode = new String(nolTicker.MarketCode).trim();
        cfi = new String(nolTicker.CFI).trim();
        group = new String(nolTicker.Group).trim();
    }

    /**
     * Returns international securities identification number. Ex. "PLDEBCA00016"
     *
     * @return ISIN - international securities identification number
     */
    @Override
    @NotNull
    public String getIsin() {
        logger.exiting(NolTickerAPI.class.getName(), "getIsin");
        return isin;
    }

    /**
     * Returns short name of ticker. Ex. "DEBICA"
     *
     * @return ticker name
     */
    @Override
    @NotNull
    public String getName() {
        logger.exiting(NolTickerAPI.class.getName(), "getName");
        return name;
    }

    /**
     * Returns market code of ticker. Ex. "NM"
     * <br>
     * Possible market codes: <br>
     * NM - cash market//TODO fill descriptions <br>
     * LE -<br>
     * IX - index<br>
     * IN -<br>
     * DN -<br>
     *
     * @return success or error message
     */
    @Override
    @NotNull
    public String getMarketCode() {
        logger.exiting(NolTickerAPI.class.getName(), "getMarketCode");
        return marketCode;
    }

    /**
     * Returns type of instrument.
     * Returns empty String in API version 1.0.0.70
     *
     * @return CFI
     */
    @Override
    @NotNull
    public String getCFI() {
        logger.exiting(NolTickerAPI.class.getName(), "getCFI");
        return cfi;
    }

    /**
     * Returns parent stock index or group of ticker. Ex. "01"
     * <br>
     * Possible groups:<br>
     * 01 - WIG20 (Continuous listing)<br>
     * 02 - mWIG40 (Continuous listing)<br>
     * 03 - sWIG80 (Continuous listing)<br>
     * 10 - other (Continuous listing)<br>
     * 20 - double fixing<br>
     * 40 - ncIndex (Continuous listing)<br>
     * 45 - ncIndex (double fixing)<br>
     * 60 - Catalyst (bonds in PLN)<br>
     * 62 - bonds in EUR<br>
     * 70 - Catalyst (continuous listing) in PLN<br>
     * 72 - GPW ATS (alternate trading system) in EUR<br>
     * 80 - certificates (continuous listing)<br>
     * 85 - ETF (exchange traded fund)<br>
     * 94 - suspended (continuous listing)<br>
     * 96 - suspended (levarage)<br>
     * 97 - suspended (double fixing)<br>
     * 98 - suspended (ATS)<br>
     * 99 - suspended (ATS double fixing)<br>
     * R1 - certificates turbo<br>
     * R2 - certificates<br>
     * X1 - WSE indexes<br>
     * Q1 - certificates<br>
     * Q6 - structured bonds<br>
     * F1 - index futures<br>
     * F2 - stock futures<br>
     * F3 - currency<br>
     * F4 - interest rate futures<br>
     * F5 - commodity futures<br>
     *
     * @return group
     */
    @Override
    @NotNull
    public String getGroup() {
        logger.exiting(NolTickerAPI.class.getName(), "getGroup");
        return group;
    }

    @Override
    public String toString() {
        return "\nNolTickerAPI \n" +
                "ISIN: " + getIsin() + "\n" +
                "\nName: " + getName() +
                "\nMarket code: " + getMarketCode() +
                "\nCFI: " + getCFI() +
                "\nGroup: " + getGroup() + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NolTickerAPI that = (NolTickerAPI) o;
        return Objects.equals(isin, that.isin);
    }

    @Override
    public int hashCode() {

        return Objects.hash(isin);
    }
}
