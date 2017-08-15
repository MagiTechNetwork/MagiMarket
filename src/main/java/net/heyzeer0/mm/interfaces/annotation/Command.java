package net.heyzeer0.mm.interfaces.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by HeyZeer0 on 14/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    String name();
    String permission();
    String description();

}
