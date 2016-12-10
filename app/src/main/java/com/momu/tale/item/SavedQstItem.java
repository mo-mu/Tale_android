package com.momu.tale.item;

/**
 * Created by songmho on 2016-10-15.
 */

public class SavedQstItem {
    String question, date, answer;
    int answerId, questionId;

    /**
     *
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @return
     */
    public String getQuestion() {
        return question;
    }

    public int getQuestionId() {
        return questionId;
    }

    /**
     *
     * @return
     */
    public String getAnswer() {
        return answer;
    }

    /**
     *
     * @return
     */
    public int getAnswerId() {
        return answerId;
    }

    /**
     *
     * @param question
     * @param date
     * @param answer
     * @param answerId
     */
    public SavedQstItem(String question, String date, String answer, int answerId, int questionId) {
        this.question = question;
        this.date = date;
        this.answer = answer;
        this.answerId = answerId;
        this.questionId = questionId;
    }
}
