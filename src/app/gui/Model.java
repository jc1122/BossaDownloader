package app.gui;

import app.bossaAPI.BossaAPI;

public class Model {
    BossaAPI.AccountsObservable accountsObservable;
    BossaAPI.DelayObservable delayObservable;
    BossaAPI.OrderObservable orderObservable;
    BossaAPI.OutlookObservable outlookObservable;
    BossaAPI.QuotesObservable quotesObservable;
    BossaAPI.StatusObservable statusObservable;

    public void startAPI() {
        BossaAPI.Get_Version();
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
}
