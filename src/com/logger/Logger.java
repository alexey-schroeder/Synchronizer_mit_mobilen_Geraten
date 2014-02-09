package com.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 09.02.14
 * Time: 19:38
 * To change this template use File | Settings | File Templates.
 */
public class Logger {
    private static boolean writeInConsole = true;
    private static boolean writeInFile = true;
    private static String logFileName = "synchronizerLog.txt";
    private static String timeFormat = "dd.MM.yyyy HH:mm:ss.SSS";
    public static void log(String text){
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.getDefault());
        text = sdf.format(new Date()) + " " + text;
        if(writeInConsole){
            System.out.println(text);
        }
        if(writeInFile){
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(logFileName, true)));
                out.println(text);
                out.close();
            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        }
    }

    public static boolean isWriteInConsole() {
        return writeInConsole;
    }

    public static void setWriteInConsole(boolean writeInConsole) {
        Logger.writeInConsole = writeInConsole;
    }

    public static boolean isWriteInFile() {
        return writeInFile;
    }

    public static void setWriteInFile(boolean writeInFile) {
        Logger.writeInFile = writeInFile;
    }

    public static String getLogFileName() {
        return logFileName;
    }

    public static void setLogFileName(String logFileName) {
        Logger.logFileName = logFileName;
    }

    public static String getTimeFormat() {
        return timeFormat;
    }

    public static void setTimeFormat(String timeFormat) {
        Logger.timeFormat = timeFormat;
    }
}
