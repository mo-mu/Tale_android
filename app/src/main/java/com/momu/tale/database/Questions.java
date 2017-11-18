package com.momu.tale.database;

/**
 * Created by songm on 2017-01-07.
 */

public class Questions {
    String q;
    int id;
    String created_at, updated_at;
    String q_ja;

    public int getId() {
        return id;
    }

    public String getQ() {
        return q;
    }

    public String getQ_ja() {
        return q_ja;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public Questions() {

    }

    /**
     * 질문
     *
     * @param id         질문 id
     * @param q          질문 내용
     * @param created_at 생성시간
     * @param updated_at
     * @param q_ja 일본어
     */
    public Questions(int id, String q, String created_at, String updated_at, String q_ja) {
        this.id = id;
        this.q = q;
        this.q_ja = q_ja;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.q_ja = q_ja;
    }

}