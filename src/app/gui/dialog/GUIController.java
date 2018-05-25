package app.gui.dialog;

import javax.swing.*;
import java.lang.reflect.Constructor;

/**
 * Controller part of MVC pattern. Provides basic functionality - creates and displays View
 * @see GUIView
 * @see GUIModel
 * @param <K>
 * @param <L>
 */
public class GUIController<K extends GUIModel, L extends GUIView> {
    protected K model;
    protected L view;

    protected GUIController(K model, Class<L> viewClass) {
        this.model = model;
        try {
            Constructor<L> constructor = viewClass.getDeclaredConstructor(this.getClass(), model.getClass());
            constructor.setAccessible(true);
            this.view = constructor.newInstance(this, model);

        } catch (Exception e) {
            //TODO add error handling
            System.out.println(e.getMessage());
        }
        SwingUtilities.invokeLater(view::createGUI);
    }

    protected L getView() {
        return view;
    }
}
