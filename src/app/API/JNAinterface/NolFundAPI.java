package app.API.JNAinterface;

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
final class NolFundAPI extends BossaAPIClassWrapper<NolFundAPI, BossaAPIInterface.NolFund> {

    private final String name;
    private final String value;

    //this constructor is accessed by reflection
    private NolFundAPI(BossaAPIInterface.NolFund nolFund) {
        super(nolFund);
        name = new String(wrappee.name).trim();
        value = new String(wrappee.value).trim();
    }

    String getName() {
        logger.exiting(NolFundAPI.class.getName(), "getName");
        return name;
    }

    /**
     * @return cash value
     */
    String getValue() {
        logger.exiting(NolFundAPI.class.getName(), "getValue");
        return value;
    }

}
