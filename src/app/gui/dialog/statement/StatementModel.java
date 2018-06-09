package app.gui.dialog.statement;

import app.API.JNAinterface.NolStatementAPI;
import app.API.JNAinterface.NolTickerAPI;
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
        if (propertyChangeEvent.getPropertyName() == "Quotes" || propertyChangeEvent.getPropertyName() == "Accounts") {
            propertyChangeSupport.firePropertyChange(propertyChangeEvent);
        }
    }

    public List<NolStatementAPI> getAccounts() {
        return (List<NolStatementAPI>) mainModel.getProperty("Accounts");
    }

    public Set<NolTickerAPI> getTickersInFilter() {
        return mainModel.getTickersInFilter();
    }

    public void addTickersToFilter(Set<NolTickerAPI> tickers) {
        mainModel.addTickersToFilter(tickers);
    }

    public void removeTickersFromFilter(Set<NolTickerAPI> isins) {
        mainModel.removeTickersFromFilter(isins);
    }
}
