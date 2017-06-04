package com.momu.tale.database;

/**
 * Created by songm on 2017-02-23.
 */

public class Answer {
    String answer, created_at;
    int qId, aId;

    public int getaId() {
        return aId;
    }

    public int getqId() {
        return qId;
    }

    public String getAnswer() {
        return answer;
    }

    public String getCreated_at() {
        return created_at;
    }

    public Answer() {

    }

    public Answer(int aId, int qId, String answer, String created_at) {
        this.aId = aId;
        this.qId = qId;
        this.answer = answer;
        this.created_at = created_at;
    }

//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("aId", aId);
//        result.put("qId", qId);
//        result.put("answer", answer);
//        result.put("created_at", created_at);
//
//        return result;
//    }
}
