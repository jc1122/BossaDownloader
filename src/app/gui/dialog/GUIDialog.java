package app.gui.dialog;

import app.gui.Model;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.util.logging.Logger;

public class GUIDialog<K extends GUIModel, L extends GUIView, M extends GUIController<K, L>> {
    public static class CurrentClassGetter extends SecurityManager {
        public String getClassName() {
            return getClassContext()[1].getSimpleName();
        }
    }

    protected static final Logger logger =
            Logger.getLogger(new CurrentClassGetter().getClassName());

    protected final JDialog dialog = new JDialog();
    protected K model;
    protected L view;
    protected M controller;

    public GUIDialog(Model model, Class<K> modelClass, Class<L> viewClass, Class<M> controllerClass) {
        Constructor[] constr = controllerClass.getDeclaredConstructors();
        try {
            Constructor<K> constructor = modelClass.getDeclaredConstructor(model.getClass());
            constructor.setAccessible(true);
            this.model = constructor.newInstance(model);

            Constructor<M> constructor1 = controllerClass.getDeclaredConstructor(this.model.getClass(), viewClass.getClass());
            constructor1.setAccessible(true);
            this.controller = constructor1.newInstance(this.model, viewClass);

            this.view = controller.getView();

            this.dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    model.removePropertyListener(GUIDialog.this.model);
                    super.windowClosing(e);
                }
            });
        } catch (Exception e) {
            System.out.println(constr);
            System.out.println(e.getMessage());
            System.out.println("paramter should be: " + constr[0].getParameterTypes()[0].toString() + constr[0].getParameterTypes()[1].toString());
            System.out.println("\n parameter is: " + model.getClass() + viewClass.toString());

            //TODO add exception handling
        }
    }

    public JDialog getDialog() {
        return view.getDialog();
    }
}
