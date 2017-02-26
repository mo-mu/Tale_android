package com.momu.tale;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by songm on 2017-02-24.
 */

public class MySharedPreference {
    private SharedPreferences pref;

    /**
     * 동기화 상태 변경하는 메소드
     *
     * @param isSync
     */
    public void changeSync(boolean isSync) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isSync", isSync).apply();
    }

    /**
     * 동기화 여부 가져오는 메소드
     *
     * @return saved value
     */
    public boolean getIsSync() {
        return pref.getBoolean("isSync", true);
    }

    /**
     * 동기화 설정값 제거하는 메소드
     */
    public void removeSync() {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("isSync").apply();
    }

    /**
     * Constructor
     *
     * @param context
     */
    public MySharedPreference(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }
}
