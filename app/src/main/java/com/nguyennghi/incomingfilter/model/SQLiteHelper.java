package com.nguyennghi.incomingfilter.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nguyennghi on 4/26/18 8:00 PM
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String dbName = "incomingFilter.db";
    public static final int VERSION = 2;
    public static final String TASK_TABLE = "tasks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BLOCKING_BY = "blocking_by";
    public static final String COLUMN_PROVIDER = "provider";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_BLOCKING_INCOMING_CALL = "blocking_incoming_calls";
    public static final String COLUMN_BLOCKING_INCOMING_MESS = "blocking_incoming_mess";
    public static final String COLUMN_BLOCKING_INCOMING_ACTION = "incoming_call_action";
    public static final String COLUMN_CALL_AUTO_SMS = "call_auto_sms";
    public static final String COLUMN_MESS_AUTO_SMS = "mess_auto_sms";
    public static final String COLUMN_AUTO_TEXT_CONTENT = "auto_text_content";
    public static final String COLUMN_ENABLE = "enable";

    public static final String create_table = "CREATE TABLE "+ TASK_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            COLUMN_BLOCKING_BY + " TEXT NOT NULL, "+
            COLUMN_PROVIDER + " TEXT NOT NULL, " +
            COLUMN_NUMBER + " TEXT NOT NULL, "+
            COLUMN_BLOCKING_INCOMING_CALL + " TEXT NOT NULL, "+
            COLUMN_BLOCKING_INCOMING_MESS + " TEXT NOT NULL, "+
            COLUMN_BLOCKING_INCOMING_ACTION + " TEXT NOT NULL, "+
            COLUMN_CALL_AUTO_SMS + " EXT NOT NULL, "+
            COLUMN_MESS_AUTO_SMS + " TEXT NOT NULL, "+
            COLUMN_AUTO_TEXT_CONTENT + " TEXT NOT NULL,"+
            COLUMN_ENABLE + " TEXT NOT NULL);"
            ;


    SQLiteHelper(Context context)
    {
        super(context, dbName, null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TASK_TABLE);
        this.onCreate(db);
    }
}
