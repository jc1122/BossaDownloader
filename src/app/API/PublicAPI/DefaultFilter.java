package app.API.PublicAPI;

import app.API.JNAinterface.BossaAPI;

public class DefaultFilter extends BaseFilter<Ticker> {

    public DefaultFilter() {
        super(BossaAPI.MasterFilter.getInstance());
    }
}
