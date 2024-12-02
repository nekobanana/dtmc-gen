package com.github.nekobanana.dtmcgenerator.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
    public static Logger logger = Logger.getLogger("MyLog");
    static FileHandler fh;

    static {
        try {
            fh = new FileHandler("log.txt");
            LogManager.getLogManager().reset();
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
