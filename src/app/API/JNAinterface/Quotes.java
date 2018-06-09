package app.API.JNAinterface;

/**
 * Updates information about quotes. This class handles listeners for {@link NolRecentInfoAPI}
 *
 * @see NolRecentInfoAPI
 */
final class Quotes extends PropertyAPI<NolRecentInfoAPI> {

    private static final Quotes INSTANCE = new Quotes();
    private static final QuotesCallbackHelper CALLBACK_HELPER = INSTANCE.new QuotesCallbackHelper();

    //private NolRecentInfoAPI nolRecentInfoAPI;

    private Quotes() {
    }

    static Quotes getInstance() {
        logger.exiting(Quotes.class.getName(), "getInstance");
        return INSTANCE;
    }

    static QuotesCallbackHelper getCallbackHelper() {
        return CALLBACK_HELPER;
    }

    class QuotesCallbackHelper implements BossaAPIInterface.SetCallbackDummy {
        @Override
        public void invoke(BossaAPIInterface.NolRecentInfo nolrecentinfo) {
            logger.entering(this.getClass().getName(), "invoke");
            NolRecentInfoAPI oldValue = Quotes.this.property;
            Quotes.this.property = new NolRecentInfoAPI(nolrecentinfo);
            Quotes
                    .this
                    .propertyChangeSupport
                    .firePropertyChange("Quotes", oldValue, property);
            logger.exiting(this.getClass().getName(), "invoke", property);
        }
    }
}
