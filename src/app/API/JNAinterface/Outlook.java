package app.API.JNAinterface;

/**
 * Updates diagnostic data from NOL3. This class handles listeners for {@link Outlook}
 *
 * @see Outlook
 */
public final class Outlook extends PropertyAPI<String> implements BossaAPIInterface.SetCallbackOutlookDummy {
    //private String outlook;
    private static final Outlook INSTANCE = new Outlook();

    private Outlook() {
    }

    public static Outlook getInstance() {
        logger.exiting(Outlook.class.getName(), "getInstance");
        return INSTANCE;
    }

        @Override
        public void invoke(String outlook) {
            logger.exiting(this.getClass().getName(), "invoke");
            String oldVal = Outlook.this.property;
            Outlook.this.property = outlook;
            Outlook.this.propertyChangeSupport.firePropertyChange("Outlook", oldVal, outlook);
        }
}
