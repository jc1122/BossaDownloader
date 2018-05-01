package app.gui;

import javax.swing.*;


class MainMenuBarFactory extends AbstractGuiFactory<JMenuBar, JMenu> {
    MainMenuBarFactory(String name) {
        container = new JMenuBar();
        container.setName(name);

        SimpleMenuFactory factory = new SimpleMenuFactory();

        String[] apiMenuNames = new String[]{"Start", "Logs", "Stop"};
        addComponent(factory.getContainer("API", apiMenuNames));

        String[] accountsMenuNames = new String[]{"Statement"};
        addComponent(factory.getContainer("Accounts", accountsMenuNames));

        String[] helpMenuNames = new String[]{"Version"};
        addComponent(factory.getContainer("Help", helpMenuNames));
    }

    @Override
    JMenuBar getContainer(String containerName, String[] componentNames) {
        return null;
    }
}
