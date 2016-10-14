package com.momu.wtfs.item;

/**
 * Created by songmho on 2016-10-15.
 */

public class SavedQstItem {
    String question, date, answer;
    int answerId;

    public String getDate() {
        return date;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getAnswerId() {
        return answerId;
    }

    public SavedQstItem(String question, String date, String answer, int answerId){
        this.question = question;
        this.date = date;
        this.answer = answer;
        this.answerId = answerId;
    }
}
