package app.gui.dialog.statement;

import app.API.BossaAPI;
import app.gui.Model;
import app.gui.dialog.GUIModel;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Set;

public class StatementModel extends GUIModel {
    StatementModel(Model model) {
        super(model);
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getPropertyName() == "Quotes" ||propertyChangeEvent.getPropertyName() == "Accounts") {
            propertyChangeSupport.firePropertyChange(propertyChangeEvent);
        }
    }

    public List<BossaAPI.NolStatementAPI> getAccounts() {
        return (List<BossaAPI.NolStatementAPI>) mainModel.getProperty("Accounts");
    }

    public Set<String> getTickerISINSinFilter() {
        return mainModel.getTickerISINSInFilter();
    }

    public void addToFilter(Set<String> isins) {
        mainModel.addToFilter(isins);
    }

    public void removeFromFilter(Set<String> isins) {
        mainModel.removeFromFilter(isins);
    }
}
