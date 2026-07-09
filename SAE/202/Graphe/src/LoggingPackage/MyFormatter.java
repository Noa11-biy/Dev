package LoggingPackage;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class MyFormatter extends Formatter {


    @Override
    public String format(LogRecord record) {
        return String.format(
                "%1$tF %1$tT | %2$s | %3$s | %4$s | %5$s%n",
                Date.from(record.getInstant()),
                record.getLevel().getName(),
                record.getLoggerName(),
                record.getSourceMethodName(),
                formatMessage(record)
        );
    }
}
