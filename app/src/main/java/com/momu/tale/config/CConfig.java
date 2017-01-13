package com.momu.tale.config;

/**
 * 자주 사용하는 상수 관리
 * Created by knulps on 2017. 1. 14..
 */

public class CConfig {
    //폰트
    public static final String FONT_SEOUL_NAMSAN_CL = "fonts/SeoulNamsanCL.ttf";
    public static final String FONT_YANOLJA_YACHE_REGULAR = "fonts/YanoljaYacheRegular.ttf";

    //서버 통신 관련 상수
    static String URL_STRING = "http://52.78.172.143/api/v1/questions";    //question있는 주소

    static int CONNECTION_TIMEOUT = 3000;
    static int DATARETRIVAL_TIMEOUT = 3000;
}
