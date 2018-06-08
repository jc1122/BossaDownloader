package app.API.properties;

import app.API.JNAinterface.BossaAPIInterface;
import app.API.nolObjects.NolAggrStatementAPI;
import app.API.nolObjects.NolStatementAPI;

import java.util.Collections;
import java.util.List;

/**
 * Updates account statement information once received data from NOL3. This class handles listeners for {@link NolStatementAPI}
 *
 * @see NolStatementAPI
 */
public final class Accounts extends PropertyAPI<List<NolStatementAPI>> implements BossaAPIInterface.SetCallbackAccountDummy {
    //private NolAggrStatementAPI nolAggrStatementAPI;
    private static final Accounts INSTANCE = new Accounts();

    private Accounts() {
    }

    public static Accounts getInstance() {
        logger.exiting(Accounts.class.getName(), "getInstance");
        return INSTANCE;
    }

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
