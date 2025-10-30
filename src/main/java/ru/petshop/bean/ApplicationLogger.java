package ru.petshop.bean;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class ApplicationLogger {

    private final Logger logger = LogManager.getRootLogger();

    public void error(Exception ex) {
        logger.error("", ex);
    }

    public void info(String str) {
        logger.info(str);
    }

    private boolean ifInfoLevel() {
        return (logger.getLevel().compareTo(Level.INFO) >= 0);
    }

    private boolean ifDebugLevel() {
        return (logger.getLevel().compareTo(Level.DEBUG) >= 0);
    }

    private String getStringFromObjectArray(Object[] objectArray) {

        StringBuilder stringBuilder = new StringBuilder();

        for (Object object : objectArray) {
            String str = (object == null) ? "" : object.toString();
            stringBuilder = stringBuilder.append(str).append(" ");
        }

        return stringBuilder.toString();
    }

    public void info(Object ... objectArray) {

        if (ifInfoLevel()) {

            String str = getStringFromObjectArray(objectArray);

            logger.info(str);
        }
    }

    public void debug(Object ... objectArray) {

        if (ifDebugLevel()) {

            String str = getStringFromObjectArray(objectArray);

            logger.debug(str);
        }
    }
}
