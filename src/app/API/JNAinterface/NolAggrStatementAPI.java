package app.API.JNAinterface;

import org.jetbrains.annotations.Contract;

import java.util.List;

/**
 * Contains list of statements for all accessible accounts.
 *
 * @see NolStatementAPI
 */
final class NolAggrStatementAPI extends BossaAPIClassWrapper<NolAggrStatementAPI, BossaAPIInterface.NolAggrStatement> {

    private final List<NolStatementAPI> statementList;

    NolAggrStatementAPI(BossaAPIInterface.NolAggrStatement nolAggrStatement) {
        super(nolAggrStatement);
        logger.entering(NolAggrStatementAPI.class.getName(), "Constructor");
        this.statementList = BossaAPIClassWrapper.convertPointerToListHelper(wrappee.size, wrappee.ptrstate, NolStatementAPI.class);
        logger.exiting(NolAggrStatementAPI.class.getName(), "Constructor");
    }

    /**
     * Get list of statements for all accessible accounts
     *
     * @return list of statements
     */
    @Contract(pure = true)
    List<NolStatementAPI> getStatements() {
        logger.exiting(NolAggrStatementAPI.class.getName(), "getProperty", statementList);
        return statementList;
    }

}
