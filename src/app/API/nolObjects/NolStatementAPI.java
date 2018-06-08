package app.API.nolObjects;

import app.API.JNAinterface.BossaAPIInterface;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores information about trading account.
 */
public final class NolStatementAPI extends BossaAPIClassWrapper<NolStatementAPI, BossaAPIInterface.NolStatement> {
    //private List<NolFundAPI> fundList;
    private Map<String, Double> fundMap;
    private List<NolPosAPI> positionList;

    String name, type;
    boolean ikeStatus;

    private NolStatementAPI(BossaAPIInterface.NolStatement nolStatement) {
        logger.entering(NolStatementAPI.class.getName(), "Constructor");
        this.wrappee = nolStatement;
        name = new String(wrappee.name).trim();
        ikeStatus = new String(wrappee.ike).trim().equals("T");
        type = new String(wrappee.type).trim();

        List<NolFundAPI> fundList = BossaAPIClassWrapper.convertPointerToListHelper(wrappee.sizefund, wrappee.ptrfund, NolFundAPI.class);
        positionList = BossaAPIClassWrapper.convertPointerToListHelper(wrappee.sizepos, wrappee.ptrpos, NolPosAPI.class);

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
    @Contract(pure = true)
    public Map<String, Double> getFundMap() {
        logger.exiting(NolStatementAPI.class.getName(), "getFundMap", fundMap);
        return fundMap;
    }

    /**
     * Return list of papers in portfolio.
     *
     * @return positions
     */
    @Contract(pure = true)
    public List<NolPosAPI> getPositions() {
        logger.exiting(NolStatementAPI.class.getName(), "getPositions", fundMap);
        return positionList;
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
