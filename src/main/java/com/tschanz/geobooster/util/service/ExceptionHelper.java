package com.tschanz.geobooster.util.service;

import java.util.ArrayList;


public class ExceptionHelper {
    public static String getErrorText(Throwable e, String delimiter) {
        var causes = new ArrayList<String>();
        causes.add(e.getMessage());
        var parentCause = e.getCause();
        while (parentCause != null) {
            causes.add(parentCause.getMessage());
            parentCause = parentCause.getCause();
        }

        return String.join(delimiter, causes);
    }
}
