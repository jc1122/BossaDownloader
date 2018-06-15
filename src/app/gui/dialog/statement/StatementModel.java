package app.gui.dialog.statement;

import app.API.PublicAPI.Statement;
import app.gui.Model;
import app.gui.dialog.GUIModel;

import java.beans.PropertyChangeEvent;
import java.util.List;

public class StatementModel extends GUIModel {
    StatementModel(Model model) {
        super(model);
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getPropertyName() == "Accounts") {
            propertyChangeSupport.firePropertyChange(propertyChangeEvent);
        }
    }

    public List<Statement> getAccounts() {
        return (List<Statement>) mainModel.getProperty("Accounts").getValue();
    }
}