package app.datadownloader;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class LogHandlerFilter implements Filter {
    @Override
    public boolean isLoggable(LogRecord record) {
        //record.getSourceClassName().split();
        return false;
    }
}
