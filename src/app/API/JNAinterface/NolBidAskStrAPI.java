package app.API.JNAinterface;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Stores list of market quotes (market levels). The list may not be sorted.
 *
 * @see NolBidAskTblAPI
 */
final class NolBidAskStrAPI extends BossaAPIClassWrapper<NolBidAskStrAPI, BossaAPIInterface.NolBidAskStr> {

    private final List<NolBidAskTblAPI> bidAskList;

    NolBidAskStrAPI(BossaAPIInterface.NolBidAskStr nolBidAskStr) {
        super(nolBidAskStr);
        logger.entering(NolBidAskStrAPI.class.getName(), "Constructor");
        bidAskList = BossaAPIClassWrapper.convertPointerToListHelper(wrappee.offersize, wrappee.bidask_table,
                NolBidAskTblAPI.class);
    }

    @NotNull List<NolBidAskTblAPI> getBidask_table() {
        logger.exiting(NolBidAskStrAPI.class.getName(), "getBidask_table", bidAskList);
        return bidAskList;
    }

}
