
package com.uubee.prepay.util;

import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DebugLog {
    static String className;
    static String methodName;
    static int lineNumber;

    private DebugLog() {
    }

    public static boolean isDebuggable() {
        return true;
    }

    private static String createLog(String log) {
        return "[" + methodName + ":" + lineNumber + "]" + log;
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(String message) {
        if(isDebuggable()) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.e(className, createLog(message));
        }
    }

    public static void i(String message) {
        if(isDebuggable()) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.i(className, createLog(message));
        }
    }

    public static void d(String message) {
        if(isDebuggable()) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.d(className, createLog(message));
        }
    }

    public static void v(String message) {
        if(isDebuggable()) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.v(className, createLog(message));
        }
    }

    public static void w(String message) {
        if(isDebuggable()) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.w(className, createLog(message));
        }
    }

    public static void wtf(String message) {
        if(isDebuggable()) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.wtf(className, createLog(message));
        }
    }

    public static void f(String fileName, String message) {
        if(isDebuggable()) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.e(className, createLog(fileName));
            File fileDir = new File(Environment.getExternalStorageDirectory(), "/DebugLog/");
            if(fileDir.exists() || fileDir.mkdirs()) {
                File logFile = new File(fileDir, fileName + ".txt");
                FileWriter fileOutputStream = null;

                try {
                    if(logFile.exists() || logFile.createNewFile()) {
                        fileOutputStream = new FileWriter(logFile, true);
                        fileOutputStream.write(message);
                        fileOutputStream.flush();
                        return;
                    }
                } catch (Exception var15) {
                    var15.printStackTrace();
                    return;
                } finally {
                    if(fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException var14) {
                            var14.printStackTrace();
                        }
                    }

                }

            }
        }
    }
}
