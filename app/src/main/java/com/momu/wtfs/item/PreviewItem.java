package com.momu.wtfs.item;

/**
 * PreviewItem.<br>
 *     지난이야기 리스트 아이템
 * Created by songmho on 2016-10-01.
 */

public class PreviewItem {
    String date, question;
    int count, questionId;

    /**
     * getCount.<br>
     * @return int 현재 Question의 Answer 갯수 반환
     */
    public int getCount() {
        return count;
    }

    /**
     * getDate.<br>
     * @return String date를 반환
     */
    public String getDate() {
        return date;
    }

    /**
     * getQuestion <br>
     * @return String Question 반환
     */
    public String getQuestion() {
        return question;
    }

    public int getQuestionId() {
        return questionId;
    }

    /**
     * constructor (생성자)<br>
     * @param count 현재 Question의 Answer 갯수
     * @param date 현재 Question에서 최신 Answer의 날짜
     * @param question 현재 Question
     * @param questionId 현재 question Id
     */
    public PreviewItem(int count, String date, String question, int questionId){
        this.count=count;
        this.date=date;
        this.question =question;
        this.questionId=questionId;
    }
}
