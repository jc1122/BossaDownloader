package app.API.JNAinterface;

import app.API.PublicAPI.Position;
import app.API.PublicAPI.Statement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores information about trading account.
 */
final class NolStatementAPI extends BossaAPIClassWrapper<NolStatementAPI, BossaAPIInterface.NolStatement> implements Statement {
    //private List<NolFundAPI> fundList;
    private final Map<String, Double> fundMap;
    private final List<Position> positionList;

    private final String name;
    private final String type;
    private final boolean ikeStatus;

    //accessed through reflection
    private NolStatementAPI(BossaAPIInterface.NolStatement nolStatement) {
        super(nolStatement);
        logger.entering(NolStatementAPI.class.getName(), "Constructor");

        name = new String(wrappee.name).trim();
        ikeStatus = new String(wrappee.ike).trim().equals("T");
        type = new String(wrappee.type).trim();

        List<NolFundAPI> fundList = BossaAPIClassWrapper.convertPointerToListHelper(wrappee.sizefund, wrappee.ptrfund, NolFundAPI.class);
        positionList = (List<Position>)(List<?>)BossaAPIClassWrapper.convertPointerToListHelper(wrappee.sizepos, wrappee.ptrpos, NolPosAPI.class);

        fundMap = new HashMap<>();
        for (NolFundAPI fund : fundList) {
            fundMap.put(fund.getName(), Double.parseDouble(fund.getValue()));
        }
        logger.exiting(NolStatementAPI.class.getName(), "Constructor");
    }

    /**
     * Account number of currently active account.
     *
     * @return account number
     */
    @Override
    @NotNull
    public String getName() {
        logger.exiting(NolStatementAPI.class.getName(), "getName");
        return name;
    }


    /**
     * {@code True} if account is IKE (Indywidualne Konto Emerytalne), {@code false} if not.
     *
     * @return ike status
     */
    @Override
    public boolean getIke() {
        logger.exiting(NolStatementAPI.class.getName(), "getIke");
        return ikeStatus;
    }

    /**
     * Returns account type:
     * {@code M} - cash market account <br>
     * {@code P} - derivates
     *
     * @return account type
     */
    @Override
    @NotNull
    public String getType() {
        logger.exiting(NolStatementAPI.class.getName(), "getType");
        return type;
    }

    /**
     * Returns map of funds associated with account.
     *
     * @return map
     * @see NolFundAPI
     */
    @Override
    @Contract(pure = true)
    public Map<String, Double> getFundMap() {
        logger.exiting(NolStatementAPI.class.getName(), "getFundMap", fundMap);
        return Collections.unmodifiableMap(fundMap);
    }

    /**
     * Return list of papers in portfolio.
     *
     * @return positions
     */
    @Override
    @Contract(pure = true)
    public List<Position> getPositions() {
        logger.exiting(NolStatementAPI.class.getName(), "getPositions", fundMap);
        return (List<Position>)(List<?>)positionList;
    }

    @Override
    public String toString() {
        return "NolStatementAPI \n" +
                "\nName: " + getName() +
                "\nIKE: " + getIke() +
                "\nType: " + getType() +
                "\nFunds: " + getFundMap() +
                "\nPositions: " + getPositions();
    }
}
