package app.gui;

import app.bossaAPI.BossaAPI;

public class Model {
    private BossaAPI.AccountsObservable accountsObservable;
    private BossaAPI.DelayObservable delayObservable;
    private BossaAPI.OrderObservable orderObservable;
    private BossaAPI.OutlookObservable outlookObservable;
    private BossaAPI.QuotesObservable quotesObservable;
    private BossaAPI.StatusObservable statusObservable;

    public void startAPI() {
        BossaAPI.Initialize();
        BossaAPI.InitializeObservables();
        setObservables();
    }

    public void stopAPI() {
        System.out.println(BossaAPI.Shutdown());
    }

    private void setObservables() {
        accountsObservable = BossaAPI.AccountsObservable.getInstance();
        delayObservable = BossaAPI.DelayObservable.getInstance();
        orderObservable = BossaAPI.OrderObservable.getInstance();
        outlookObservable = BossaAPI.OutlookObservable.getInstance();
        quotesObservable = BossaAPI.QuotesObservable.getInstance();
        statusObservable = BossaAPI.StatusObservable.getInstance();
    }

    public String getAPIversion() {
        return BossaAPI.Get_Version();
    }

    public BossaAPI.AccountsObservable getAccountsObservable() {
        return accountsObservable;
    }

    public BossaAPI.DelayObservable getDelayObservable() {
        return delayObservable;
    }

    public BossaAPI.OrderObservable getOrderObservable() {
        return orderObservable;
    }

    public BossaAPI.OutlookObservable getOutlookObservable() {
        return outlookObservable;
    }

    public BossaAPI.QuotesObservable getQuotesObservable() {
        return quotesObservable;
    }

    public BossaAPI.StatusObservable getStatusObservable() {
        return statusObservable;
    }
}
