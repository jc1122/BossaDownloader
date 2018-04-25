package app.gui;

import javax.swing.*;

class SimpleMenuFactory extends AbstractGuiFactory<JMenu, JMenuItem> {
    @Override
    JMenu getContainer(String containerName, String[] componentNames) {
        container = new JMenu(containerName);
        container.setName(containerName);
        for (String componentName : componentNames) {
            JMenuItem component = new JMenuItem(componentName);
            addComponent(component);
        }
        return container;
    }
}
