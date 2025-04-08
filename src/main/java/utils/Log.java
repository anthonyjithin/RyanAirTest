package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//wrapper for creating logger instance for log4j
public class Log {
    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}