package com.momu.tale.config;

/**
 * 자주 사용하는 상수 관리
 * Created by knulps on 2017. 1. 14..
 */

public class CConfig {
    //폰트 모음
    public static final String FONT_SEOUL_NAMSAN_CL = "fonts/SeoulNamsanCL.ttf";
    public static final String FONT_YANOLJA_YACHE_REGULAR = "fonts/YanoljaYacheRegular.ttf";

    //REQUEST CODE
    public static final int RESULT_QST_LIST = 11;
    public static final int RESULT_DETAIL = 12;
    public static final int RESULT_MODIFY = 13;

    //서버 통신 관련 상수 (미사용)
    public static final String URL_STRING = "http://52.78.172.143/api/v1/questions";    //question있는 주소
    static int CONNECTION_TIMEOUT = 3000;
    static int DATARETRIVAL_TIMEOUT = 3000;
}
