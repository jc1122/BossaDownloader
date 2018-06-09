package app.API.JNAinterface;

/**
 * Updates information about quotes. This class handles listeners for {@link NolRecentInfoAPI}
 *
 * @see NolRecentInfoAPI
 */
public final class Quotes extends PropertyAPI<NolRecentInfoAPI> implements BossaAPIInterface.SetCallbackDummy {

    private static final Quotes INSTANCE = new Quotes();
    //private NolRecentInfoAPI nolRecentInfoAPI;

    private Quotes() {
    }

    public static Quotes getInstance() {
        logger.exiting(Quotes.class.getName(), "getInstance");
        return INSTANCE;
    }

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
