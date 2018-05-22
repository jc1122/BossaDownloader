package app.gui.menu;

import javax.swing.*;


public class MainMenuBarFactory extends AbstractGuiFactory<JMenuBar, JMenu> {
    public MainMenuBarFactory(String name) {
        container = new JMenuBar();
        container.setName(name);

        SimpleMenuFactory factory = new SimpleMenuFactory();

        String[] apiMenuNames = new String[]{"Start", "Logs", "Stop"};
        addComponent(factory.getContainer("API", apiMenuNames));

        String[] accountsMenuNames = new String[]{"Statement"};
        addComponent(factory.getContainer("Accounts", accountsMenuNames));

        String[] tickersMenuNames = new String[]{"Select...", "Save to CSV"};
        addComponent(factory.getContainer("Tickers", tickersMenuNames));

        String[] helpMenuNames = new String[]{"Version"};
        addComponent(factory.getContainer("Help", helpMenuNames));

        nameMapping.putAll(factory.getNameMapping());
        nameMapping.put(name, container);
    }

    @Override
    JMenuBar getContainer(String containerName, String[] componentNames) {
        return null;
    }
}
