package com.hills.mcs_02;

import android.util.Log;

public class LogUtils {
    static String className;
    static String methodName;
    static int lineNumber;

    /** Determine whether it can be debugged */
    public static boolean IS_DEBUGGABLE() {
        return BuildConfig.DEBUG;
    }

    private static String createLog(String log ) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("==").append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")==:");
        buffer.append(log);
        return buffer.toString();
    }

    /** Get the file name, method name, and number of lines */
    private static void GET_METHOD_NAME(StackTraceElement[] sElements){
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(String message){
        if (!IS_DEBUGGABLE())
            return;
        GET_METHOD_NAME(new Throwable().getStackTrace());
        Log.e(className, createLog(message));
    }

    public static void i(String message){
        if (!IS_DEBUGGABLE())
            return;
        GET_METHOD_NAME(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void d(String message){
        if (!IS_DEBUGGABLE())
            return;
        GET_METHOD_NAME(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void v(String message){
        if (!IS_DEBUGGABLE())
            return;
        GET_METHOD_NAME(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public static void w(String message){
        if (!IS_DEBUGGABLE())
            return;
        GET_METHOD_NAME(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }
}
