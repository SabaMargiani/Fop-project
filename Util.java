public class Util {

    public static String currentLocation() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // stackTrace[0] is the current method call (getStackTrace), we want the caller (stackTrace[2])
        StackTraceElement element = stackTrace[2];

        String fileName = element.getFileName();
        String methodName = element.getMethodName();
        int lineNumber = element.getLineNumber();

        String currentLocation = "Current location: " + fileName + " -> " + methodName + " (Line " + lineNumber + ")";
        return currentLocation;
    }

    
}
