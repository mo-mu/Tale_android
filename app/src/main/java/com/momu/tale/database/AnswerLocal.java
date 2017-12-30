package com.momu.tale.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.momu.tale.SqliteHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by songmho on 2017. 12. 2..
 */

public class AnswerLocal {
    private SqliteHelper helper;
    private SQLiteDatabase db;
    private String sql;

    public AnswerLocal(SqliteHelper helper){
        this.helper = helper;

    }

    /**
     * answer save to local method
     * @param o data (json format)
     */
    public void saveAnsLocal(JSONObject o){
        db = helper.getWritableDatabase();
        try {
            sql = "insert into answer (question_id, user_id, a, created_at) " +
                    "values (" + o.getInt("question_id") + ", 0, '" + o.getString("a") + "', '" + o.getString("created_at") + "');";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.execSQL(sql);

        return;
    }

    public void updateAnsLocal(JSONObject o){
        db = helper.getWritableDatabase();
        try {
            sql = "update answer set a = '" + o.getString("a") + "' where id=" + o.getInt("answer_id") + ";";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.execSQL(sql);
    }

    public void delAnsLocal(int i){
        sql = "delete from answer where id=" + i+ ";";
        db.execSQL(sql);
    }

    /**
     * 오늘의 답변 있는지 확인하는 메소드
     *
     * @param today 오늘 날짜
     * @return boolean true : 있을 경우, false : 없을 경우
     */
    public boolean searchTodayAsw(String today){
        db = helper.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from answer where created_at='" + today + "';", null);
            while (cursor.moveToNext())
                if (cursor.getString(4) != null)
                    return true;       //있으면 true
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return false;
    }

}
