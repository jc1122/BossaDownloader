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

    public NolAggrStatementAPI(BossaAPIInterface.NolAggrStatement nolAggrStatement) {
        logger.entering(NolAggrStatementAPI.class.getName(), "Constructor");
        this.wrappee = nolAggrStatement;
        this.statementList = BossaAPIClassWrapper.convertPointerToListHelper(wrappee.size, wrappee.ptrstate, NolStatementAPI.class);
        logger.exiting(NolAggrStatementAPI.class.getName(), "Constructor");
    }

    /**
     * Get list of statements for all accessible accounts
     *
     * @return list of statements
     */
    @Contract(pure = true)
    public List<NolStatementAPI> getStatements() {
        logger.exiting(NolAggrStatementAPI.class.getName(), "getProperty", statementList);
        return statementList;
    }

}
