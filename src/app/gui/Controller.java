package app.gui;

import app.bossaAPI.BossaAPI;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

public class Controller {
    Model model;
    View view;

    public Controller(Model model) {
        this.model = model;
        view = new View(this, model);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view.createGUI();
                view.disableStopApiMenuItem();
            }
        });
    }

    public void startAPI() {
        model.startAPI();
        view.enableStopApiMenuItem();
        view.disableStartApiMenuItem();
    }

    public void stopAPI() {
        model.stopAPI();
        view.disableStopApiMenuItem();
        view.enableStartApiMenuItem();
    }

    //TODO add status bar and logging of init functions
//TODO add GUI exception handling
    //TODO add window with statement
    public class OutlookObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            BossaAPI.OutlookObservable observable = (BossaAPI.OutlookObservable) o;
            System.out.println(observable.getOutlook());
        }
    }

    public class OrderObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            BossaAPI.OrderObservable observable = (BossaAPI.OrderObservable) o;
            System.out.println("order info: " + observable.getNolOrderReportAPI());
        }
    }

    public class QuotesObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            BossaAPI.QuotesObservable info = (BossaAPI.QuotesObservable) o;
            System.out.println("quote: " + info.getNolRecentInfoAPI());
        }
    }
}
