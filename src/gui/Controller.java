package gui;

public class Controller {
    Model model;
    View view;

    public Controller(Model model) {
        this.model = model;
        view = new View(this, model);
        view.createGUI();
        view.disableStopApiMenuItem();
    }

    ;

    public void start() {
        model.start();
    }
}
