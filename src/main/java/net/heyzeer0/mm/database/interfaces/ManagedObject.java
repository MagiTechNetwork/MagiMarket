package net.heyzeer0.mm.database.interfaces;

import net.heyzeer0.mm.database.MarketData;

/**
 * Created by HeyZeer0 on 15/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public interface ManagedObject {

    void delete();

    void save();

    default void deleteAsync() {
        MarketData.queue(this::delete);
    }

    default void saveAsync() {
        MarketData.queue(this::save);
    }

}
