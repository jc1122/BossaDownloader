package app.API.nolObjects;

import app.API.JNAinterface.BossaAPIInterface;

/**
 * Contains information about funds in portfolio.
 * <p>
 * This is just a helper used in {@link NolStatementAPI}<br>
 * <br>
 * Possible names:<br>
 * <br>
 * For cash accounts: <br>
 * CashRecivables - total cash on account <br>
 * MaxBuy - maximum buying power <br>
 * MaxOtpBuy - maximum buying power on delayed payment <br>
 * LiabilitiesLimitMax - amount of cash to reach buying limit <br>
 * RecivablesBlocked - cash blocked for pending orders <br>
 * Recivables - cash <br>
 * Liabilities - cash to deduct from account <br>
 * <br>
 * For derivates accounts: <br>
 * Deposit - total deposit on account <br>
 * BlockedDeposit - deposit blocked for orders<br>
 * FreeDeposit - free cash in deposit<br>
 * SecSafetiesUsed - //TODO check what it is<br>
 * SecSafeties - //TODO check what it is<br>
 * OptionBonus - cash received as option bonus<br>
 * <br>
 * For all accounts:<br>
 * Cash - cash available for orders
 * CashBlocked - cash blocked for pending orders
 * SecValueSum - sum of values of owned securities
 * PortfolioValue - total portfolio value
 *
 * @see NolStatementAPI
 */
public final class NolFundAPI extends BossaAPIClassWrapper<NolFundAPI, BossaAPIInterface.NolFund> {

    String name, value;

    private NolFundAPI(BossaAPIInterface.NolFund nolFund) {
        this.wrappee = nolFund;
        name = new String(wrappee.name).trim();
        value = new String(wrappee.value).trim();
    }

    public String getName() {
        logger.exiting(NolFundAPI.class.getName(), "getName");
        return name;
    }

    /**
     * @return cash value
     */
    public String getValue() {
        logger.exiting(NolFundAPI.class.getName(), "getValue");
        return value;
    }

}
