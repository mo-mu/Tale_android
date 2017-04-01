package com.momu.tale.utility;

import android.util.Log;

/**
 * 로그 관리 클래스<br>
 * 릴리즈할 때는 각 함수 내용을 주석처리 해 준다.
 * Created by Knulps on 2017-01-13.
 */
public class LogHelper {
    private static final boolean DEBUG_MODE = false;

    /**
     * Log.e 관리 클래스
     *
     * @param tag 태그
     * @param e   내용
     */
    public static void e(String tag, String e) {
        if (DEBUG_MODE) Log.e(tag, e);
    }

    /**
     * Log.d 관리 클래스
     *
     * @param tag 태그
     * @param e   내용
     */
    public static void d(String tag, String e) {
        if (DEBUG_MODE) Log.d(tag, e);
    }

    /**
     * e.printStackTrace 관리 클래스
     *
     * @param e 예외
     */
    public static void errorStackTrace(Exception e) {
        if (DEBUG_MODE) e.printStackTrace();
    }
}
