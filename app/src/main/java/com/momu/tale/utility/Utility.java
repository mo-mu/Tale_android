package com.momu.tale.utility;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * 유틸리티
 * Created by knulps on 2017. 3. 25..
 */

public class Utility {
    /**
     * 네트워크 연결 여부 확인
     * @return 네트워크 연결 여부
     */
    public static boolean isNetworkConnected(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
