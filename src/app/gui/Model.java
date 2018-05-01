package app.gui;

import app.API.BossaAPI;

import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Model {
    private Map<String, BossaAPI.PropertyAPI> propertyMap = new HashMap<>();

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
        propertyMap.put("Accounts", BossaAPI.Accounts.getInstance());
        propertyMap.put("Delay", BossaAPI.Delay.getInstance());
        propertyMap.put("Order", BossaAPI.Order.getInstance());
        propertyMap.put("Outlook", BossaAPI.Outlook.getInstance());
        propertyMap.put("Quotes", BossaAPI.Quotes.getInstance());
        propertyMap.put("Status", BossaAPI.Status.getInstance());
    }

    public String getAPIversion() {
        return BossaAPI.Get_Version();
    }

    public void addPropertyListener(PropertyChangeListener listener) {
        for (String property : propertyMap.keySet()) {
            propertyMap.get(property).addPropertyChangeListener(listener);
        }
    }

    public Object getProperty(String property) {
        return propertyMap.get(property).getProperty();
    }
}
