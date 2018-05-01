package app.gui;

import app.API.BossaAPI;

import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Model {
    private BossaAPI.Accounts accounts;
    private BossaAPI.Delay delay;
    private BossaAPI.Order order;
    private BossaAPI.Outlook outlook;
    private BossaAPI.Quotes quotes;
    private BossaAPI.Status status;

    private Set<String> tickersInFilter = new HashSet<>();

    private String isinSetToString(Set<String> isins) {
        StringBuilder filterFormat = new StringBuilder();
        for (String isin : isins) {
            filterFormat.append(isin);
        }
        return filterFormat.toString();
    }

    public void addToFilter(Set<String> isins) {
        tickersInFilter.addAll(isins);
        String filterFormat = isinSetToString(isins);
        BossaAPI.AddToFilter(filterFormat, false);
    }

    public void removeFromFilter(Set<String> isins) {
        if (tickersInFilter.removeAll(isins)) {
            BossaAPI.AddToFilter(isinSetToString(isins), false);
        }
    }

    public void startAPI() {
        BossaAPI.InitializeObservers();
        BossaAPI.Initialize();
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

    public void addAccountsListener(PropertyChangeListener listener) {
        accounts.addPropertyChangeListener(listener);
    }

    public void addQuotesListener(PropertyChangeListener listener) {
        quotes.addPropertyChangeListener(listener);
    }

    public List<BossaAPI.NolStatementAPI> getStatements() {
        return accounts.getProperty();
    }
}
