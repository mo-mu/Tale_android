package com.momu.tale.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by knulps on 2017. 2. 11..
 */

public class AppPreference {
    /**
     * 로그인 여부 저장
     * @param isLogined 로그인 여부
     */
    public static void saveIsLogined(Context mContext, boolean isLogined) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogined", isLogined);
        editor.apply();
    }

    /**
     * 로그인 여부 불러옴
     * @return 로그인 여부
     */
    public static boolean loadIsLogined(Context mContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getBoolean("isLogined", false);
    }

    /**
     * 토글 ON 여부 저장
     * @param isOn 로그인 여부
     */
    public static void saveIsToggleOn(Context mContext, boolean isOn) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isToggleOn", isOn);
        editor.apply();
    }

    /**
     * 토글 상태 불러옴
     * @return 로그인 여부
     */
    public static boolean loadIsToggleON(Context mContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getBoolean("isToggleOn", false);
    }
}
