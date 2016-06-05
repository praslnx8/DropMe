package com.prasilabs.dropme.backend.debug;

import com.prasilabs.dropme.backend.core.CoreController;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

/**
 * Created by prasi on 11/3/16.
 */
public class ConsoleLog
{
    private static final boolean isLogConsole = true;
    private static final boolean isInfoConsole = true;
    private static final boolean isErrorConsole = true;
    private static final boolean isWarnConsole = true;

    public static void l(String tag, String message)
    {
        if(isLogConsole && CoreController.isDebug)
        {
            Logger.getLogger(tag).info(message);
        }
    }

    public static void is(String tag, String message)
    {
        if(isInfoConsole)
        {
            Logger.getLogger(tag).info(message);
        }
    }

    public static void w(String tag, String message)
    {
        if(isWarnConsole) {
            Logger.getLogger(tag).warning(message);
        }
    }

    public static void s(String tag, String message)
    {
        if(isWarnConsole) {
            Logger.getLogger(tag).severe(message);
        }
    }
    public static void e(Exception e)
    {
        if(isErrorConsole)
        {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));

            Logger.getLogger("Exception").severe(errors.toString());
        }
    }
}
