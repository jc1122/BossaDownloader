package gui;

import bossaAPIpackage.BossaAPI;
import bossaAPIpackage.Nol3State;

public class Model {
    BossaAPI api;
    Nol3State nol3State;

    public void start() {
        api.SetCallbackStatus(new BossaAPI.SetCallbackStatusDummyAPI() {
            @Override
            public void invoke(Nol3State var) {
                nol3State = var;
            }
        });
        System.out.println("Im here " + nol3State);
    }
}
