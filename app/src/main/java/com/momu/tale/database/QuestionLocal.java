package com.momu.tale.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.momu.tale.SqliteHelper;

import java.util.Locale;

/**
 * Created by songmho on 2017. 12. 2..
 */

public class QuestionLocal {
    private SqliteHelper helper;
    private SQLiteDatabase db;
    private String sql;

    public QuestionLocal(SqliteHelper helper){
        this.helper = helper;

    }

    public int getQstCount() {
        db = helper.getReadableDatabase();
        Cursor cursor = null;
        int totalQst = 0;
        try {
            cursor = db.rawQuery("select count(id) from question;", null);    //총 Question 수 가져오기
            while (cursor.moveToNext())
                totalQst = cursor.getInt(0);
                return totalQst;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }

        return totalQst;
    }

    public String getQst(int id, String language) {

        db = helper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from question where id=" + id + ";", null);
            while (cursor.moveToNext()) {
                if(language.compareTo("ko")==0) {
                    Log.d("asdfasdf",cursor.getString(1));
                    return cursor.getString(1);
                }
                else if(language.compareTo("ja")==0) {
                    return cursor.getString(3);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return "";
    }
}
