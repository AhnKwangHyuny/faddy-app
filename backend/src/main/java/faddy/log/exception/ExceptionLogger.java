package faddy.log.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionLogger {
    public static void logException(Exception e) {
        e.printStackTrace();

        StackTraceElement[] stackTrace = e.getStackTrace();
        if (stackTrace.length > 0) {
            StackTraceElement element = stackTrace[0];
            System.err.println(String.format("Exception occurred in %s.%s at line %d: %s",
                    element.getClassName(), element.getMethodName(), element.getLineNumber(), e.getMessage()));
        } else {
            System.err.println("Exception occurred: " + e.getMessage());
        }
        e.printStackTrace();
    }
}