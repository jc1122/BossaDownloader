package app.gui;

import app.API.BossaAPI;

import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Model {
    private Map<String, BossaAPI.PropertyAPI> propertyMap = BossaAPI.getPropertyMap();

    private Set<String> tickersInFilter = new HashSet<>();


    public void addToFilter(Set<String> isins) {
        BossaAPI.AddToFilter(isins, false);
    }

    public void removeFromFilter(Set<String> isins) {
        BossaAPI.RemoveFromFilter(isins, false);
    }

    public void startAPI() {
        BossaAPI.Initialize();
    }

    public void stopAPI() {
        System.out.println(BossaAPI.Shutdown());
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
