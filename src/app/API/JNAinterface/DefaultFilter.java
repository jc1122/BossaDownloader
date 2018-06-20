package app.API.JNAinterface;

import app.API.PublicAPI.Ticker;

public class DefaultFilter extends BaseFilter<Ticker> {

    public DefaultFilter() {
        super(BossaAPI.MasterFilter.getInstance());
    }
}
