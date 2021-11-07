package ru.mgvk.simplegateway;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class MyFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm:ss.SSS");

        return String.format("[%s, %s, %s]: %s\n",
                record.getLevel(),
                format.format(new Date(record.getMillis())),
                record.getLoggerName(),
                record.getMessage());
    }
}
