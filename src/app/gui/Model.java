package app.gui;

import app.API.BossaAPI;

public class Model {
    private BossaAPI.Accounts accounts;
    private BossaAPI.Delay delay;
    private BossaAPI.Order order;
    private BossaAPI.Outlook outlook;
    private BossaAPI.Quotes quotes;
    private BossaAPI.Status status;

    public void startAPI() {
        BossaAPI.InitializeObservables();
        BossaAPI.Initialize();
        //BossaAPI.InitializeObservables();
        setObservables();
    }

    public void stopAPI() {
        System.out.println(BossaAPI.Shutdown());
    }

    private void setObservables() {
        accounts = BossaAPI.Accounts.getInstance();
        delay = BossaAPI.Delay.getInstance();
        order = BossaAPI.Order.getInstance();
        outlook = BossaAPI.Outlook.getInstance();
        quotes = BossaAPI.Quotes.getInstance();
        status = BossaAPI.Status.getInstance();
    }

    public String getAPIversion() {
        return BossaAPI.Get_Version();
    }

}
