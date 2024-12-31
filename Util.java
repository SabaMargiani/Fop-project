public class Util {

    public static String currentLocation() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // stackTrace[0] is the current method call (getStackTrace), we want the caller (stackTrace[2])
        StackTraceElement element = stackTrace[2];

        String fileName = element.getFileName();
        String methodName = element.getMethodName();
        int lineNumber = element.getLineNumber();

        String currentLocation = fileName + " -> " + methodName + " (Line " + lineNumber + ")";
        return currentLocation;
    }

    public static void printError(String location, String message) {
        System.out.println("Problem in [" + location + "]: " + message);
    }
}
