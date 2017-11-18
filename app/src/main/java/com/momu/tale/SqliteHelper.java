package com.momu.tale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SqliteHelper
 * Created by songmho on 2016-09-30.
 */
public class SqliteHelper extends SQLiteOpenHelper {
    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String answer = "create table answer(id integer primary key autoincrement not null,question_id integer,user_id integer, a text, created_at text);";
        String question = "create table question(id integer primary key autoincrement not null, q text, created_at text, q_ja text);";
        db.execSQL(answer);
        db.execSQL(question);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String answer = "drop table answer";
        String question = "drop table question";
        db.execSQL(answer);
        db.execSQL(question);
        onCreate(db);
    }
}