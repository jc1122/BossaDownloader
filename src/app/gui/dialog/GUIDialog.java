package app.gui.dialog;

import app.gui.Model;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

    public GUIDialog(Model model, Class<K> modelClass, Class<M> controllerClass) {
        try {
            this.model = modelClass.getConstructor(Model.class).newInstance(model);
            this.controller = controllerClass.getConstructor(Model.class).newInstance(model);
            this.view = controller.getView();

            this.dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    model.removePropertyListener(GUIDialog.this.model);
                    super.windowClosing(e);
                }
            });
        } catch (Exception e) {
            //TODO add exception handling
        }
    }

    public JDialog getDialog() {
        return view.getDialog();
    }
}
