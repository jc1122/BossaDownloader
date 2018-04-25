package app.gui;

import javax.swing.*;

//remember to initialize container in subclass!
public abstract class AbstractGuiFactory<T extends JComponent, K extends JComponent> {
    protected T container;

    abstract T getContainer(String containerName, String[] componentNames);

    T getContainer(String containerName, K[] components) {
        container.setName(containerName);
        for (K component : components) {
            addComponent(component);
        }
        return container;
    }

    //if container is created in constructor:
    T getContainer() {
        return container;
    }

    //use this method to add to container, to maintain typesafety
    void addComponent(K component) {
        container.add(component);
    }
}
