package app.API.JNAinterface;

import java.util.Collections;
import java.util.List;

/**
 * Updates account statement information once received data from NOL3. This class handles listeners for {@link NolStatementAPI}
 *
 * @see NolStatementAPI
 */
final class Accounts extends PropertyAPI<List<NolStatementAPI>> {
    //private NolAggrStatementAPI nolAggrStatementAPI;
    private static final Accounts INSTANCE = new Accounts();
    private static final AccountsCallbackHelper CALLBACK_HELPER = INSTANCE.new AccountsCallbackHelper();
    private Accounts() {
    }

    static Accounts getInstance() {
        logger.exiting(Accounts.class.getName(), "getInstance");
        return INSTANCE;
    }

    static AccountsCallbackHelper getCallbackHelper() {
        return CALLBACK_HELPER;
    }

    final class AccountsCallbackHelper implements BossaAPIInterface.SetCallbackAccountDummy {
        @Override
        public void invoke(BossaAPIInterface.NolAggrStatement nolAggrStatement) {
            logger.exiting(this.getClass().getName(), "invoke");
            List<NolStatementAPI> oldVal = Accounts.this.property;
            Accounts.this.property = new NolAggrStatementAPI(nolAggrStatement).getStatements();
            List<NolStatementAPI> statementList = (oldVal == null) ? Collections.emptyList() : oldVal;
            Accounts
                    .this
                    .propertyChangeSupport
                    .firePropertyChange(
                            "Accounts", statementList, Accounts.this.property);
        }
    }
}
