package app.gui.menu;

import javax.swing.*;
import java.util.Map;
import java.util.HashMap;

//remember to initialize container in subclass!
//be sure that components have names
abstract class AbstractGuiFactory<T extends JComponent, K extends JComponent> {
    T container;
    Map<String, JComponent> nameMapping = new HashMap<>();

    /**
     * Implementation should initialize {@code container} to appropriate class.
     * @param containerName set container name to this value
     * @param componentNames set component names to this value
     * @return
     */
    abstract T getContainer(String containerName, String[] componentNames);

    @SuppressWarnings("unused")
    T getContainer(String containerName, K[] components) {
        container.setName(containerName);
        for (K component : components) {
            addComponent(component);
            if(nameMapping.putIfAbsent(component.getName(), component) != null) {
                throw new IllegalArgumentException("component name already mapped! Use different name.");
            }
        }
        nameMapping.put(containerName, container);
        return container;
    }

    //if container is created in constructor:
    public T getContainer() {
        return container;
    }

    //use this method to add to container, to maintain type safety
    void addComponent(K component) {
        container.add(component);
    }

    public Map<String, JComponent> getNameMapping() {
        return nameMapping;
    }

    public JComponent getComponent(String name) {
        return nameMapping.get(name);
    }
}
