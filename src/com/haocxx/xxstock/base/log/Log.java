package com.haocxx.xxstock.base.log;

/**
 * Created by Haocxx
 * on 2021-1-24
 */
public class Log {

    public static void debug(String tag, String info) {
        debug(tag + " " + info);
    }

    public static void debug(String info) {
        System.out.println(info);
    }
}
