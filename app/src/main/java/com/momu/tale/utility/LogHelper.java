package com.momu.tale.utility;

import android.util.Log;

/**
 * 로그 관리 클래스
 * Created by Knulps on 2017-01-13.
 */
public class LogHelper {
    /**
     * Log.e 관리 클래스
     *
     * @param tag 태그
     * @param e   내용
     */
    public static void e(String tag, String e) {
        LogHelper.e(tag, e);
    }

    /**
     * Log.d 관리 클래스
     *
     * @param tag 태그
     * @param e   내용
     */
    public static void d(String tag, String e) {
        Log.d(tag, e);
    }

    /**
     * e.printStackTrace 관리 클래스
     *
     * @param e 예외
     */
    public static void errorStackTrace(Exception e) {
        e.printStackTrace();
    }
}
