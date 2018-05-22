package app.gui.dialog;

public class GUIController<K extends GUIModel, L extends GUIView> {
    protected K model;
    protected L view;

    protected GUIController(K model, Class<L> viewClass) {
        this.model = model;
        try {
            this.view = viewClass.getConstructor(this.getClass(), model.getClass()).newInstance(this, model);
        } catch (Exception e) {
            //TODO add error handling
        }
    }

    protected L getView() {
        return view;
    }
}
