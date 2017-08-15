package net.heyzeer0.mm.profiles;

import net.heyzeer0.mm.interfaces.CommandExec;
import net.heyzeer0.mm.interfaces.annotation.Command;

/**
 * Created by HeyZeer0 on 14/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class CommandProfile {

    CommandExec executor;
    Command annotation;

    public CommandProfile(CommandExec executor, Command annotation) {
        this.executor = executor;
        this.annotation = annotation;
    }

    public CommandExec getExecutor() {
        return executor;
    }

    public Command getAnnotation() {
        return annotation;
    }

}
