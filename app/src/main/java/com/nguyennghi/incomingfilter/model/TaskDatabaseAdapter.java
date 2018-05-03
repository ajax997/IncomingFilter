package com.nguyennghi.incomingfilter.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.LongSparseArray;
import com.nguyennghi.incomingfilter.FilterUnit;
import com.nguyennghi.incomingfilter.UnitType;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nguyennghi on 4/26/18 8:23 PM
 */
public class TaskDatabaseAdapter implements Serializable {
    SQLiteDatabase sqLiteDatabase;
    SQLiteHelper sqLiteHelper;
    String[] selector = {SQLiteHelper.COLUMN_ID,
            SQLiteHelper.COLUMN_BLOCKING_BY,
            SQLiteHelper.COLUMN_PROVIDER,
            SQLiteHelper.COLUMN_NUMBER,
            SQLiteHelper.COLUMN_BLOCKING_INCOMING_CALL,
            SQLiteHelper.COLUMN_BLOCKING_INCOMING_MESS,
            SQLiteHelper.COLUMN_BLOCKING_INCOMING_ACTION,
            SQLiteHelper.COLUMN_CALL_AUTO_SMS,
            SQLiteHelper.COLUMN_MESS_AUTO_SMS,
            SQLiteHelper.COLUMN_AUTO_TEXT_CONTENT,
            SQLiteHelper.COLUMN_ENABLE
    };

    public TaskDatabaseAdapter(Context context)
    {
        this.sqLiteHelper = new SQLiteHelper(context);

    }
    public void open()
    {
        sqLiteDatabase = this.sqLiteHelper.getWritableDatabase();
    }
    public void close()
    {
        sqLiteDatabase.close();
    }
    public void addNewFilter(FilterUnit filterUnit)
    {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_BLOCKING_BY, String.valueOf(filterUnit.unitType));
        values.put(SQLiteHelper.COLUMN_PROVIDER, filterUnit.provider);
        values.put(SQLiteHelper.COLUMN_NUMBER, filterUnit.num);
        values.put(SQLiteHelper.COLUMN_BLOCKING_INCOMING_CALL, filterUnit.blocking_incoming_calls);
        values.put(SQLiteHelper.COLUMN_BLOCKING_INCOMING_MESS, filterUnit.blocking_incoming_mess);
        values.put(SQLiteHelper.COLUMN_BLOCKING_INCOMING_ACTION, filterUnit.incoming_call_action);
        values.put(SQLiteHelper.COLUMN_CALL_AUTO_SMS, filterUnit.call_auto_sms);
        values.put(SQLiteHelper.COLUMN_MESS_AUTO_SMS,filterUnit.call_auto_sms);
        values.put(SQLiteHelper.COLUMN_AUTO_TEXT_CONTENT, filterUnit.auto_text_content);
        values.put(SQLiteHelper.COLUMN_ENABLE, filterUnit.enable);
        sqLiteDatabase.insert(SQLiteHelper.TASK_TABLE,null, values);
       String sql = values.toString();
    }

    public void deleteFilter(int id)
    {
        sqLiteDatabase.delete(SQLiteHelper.TASK_TABLE,SQLiteHelper.COLUMN_ID+" = "+ String.valueOf(id), null );
        sqLiteDatabase.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=-1 WHERE NAME='tasks';");
        ArrayList<FilterUnit> list = listUnitsWithoutId();
        sqLiteDatabase.execSQL("DELETE FROM tasks");
        for(FilterUnit filterUnit: list)
        {
            addNewFilter(filterUnit);
        }
    }

    public void updateFilter(FilterUnit filterUnit)
    {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_BLOCKING_BY, String.valueOf(filterUnit.unitType));
        values.put(SQLiteHelper.COLUMN_PROVIDER, filterUnit.provider);
        values.put(SQLiteHelper.COLUMN_NUMBER, filterUnit.num);
        values.put(SQLiteHelper.COLUMN_BLOCKING_INCOMING_CALL, filterUnit.blocking_incoming_calls);
        values.put(SQLiteHelper.COLUMN_BLOCKING_INCOMING_MESS, filterUnit.blocking_incoming_mess);
        values.put(SQLiteHelper.COLUMN_BLOCKING_INCOMING_ACTION, filterUnit.incoming_call_action);
        values.put(SQLiteHelper.COLUMN_CALL_AUTO_SMS, filterUnit.call_auto_sms);
        values.put(SQLiteHelper.COLUMN_MESS_AUTO_SMS,filterUnit.call_auto_sms);
        values.put(SQLiteHelper.COLUMN_AUTO_TEXT_CONTENT, filterUnit.auto_text_content);
        values.put(SQLiteHelper.COLUMN_ENABLE, filterUnit.enable);

        sqLiteDatabase.update(SQLiteHelper.TASK_TABLE, values, SQLiteHelper.COLUMN_ID + " = "+ String.valueOf(filterUnit.id + 1),null);
    }

    public ArrayList<FilterUnit> listUnits()
    {
        ArrayList<FilterUnit> filterUnits = new ArrayList<>();
        //Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TASK_TABLE, selector, null, null, null,null, null);
       Cursor cursor = sqLiteDatabase.rawQuery("select * from tasks", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            FilterUnit filterUnit = new FilterUnit();
            //TODO
            filterUnit.setId(Integer.valueOf(cursor.getString(0))-1);
            filterUnit.setUnitType(UnitType.valueOf(cursor.getString(1)));
            filterUnit.setProvider(cursor.getString(2));
            filterUnit.setNum(cursor.getString(3));
            filterUnit.setBlocking_incoming_calls(Boolean.valueOf(cursor.getString(4)));
            filterUnit.setBlocking_incoming_mess(Boolean.valueOf(cursor.getString(5)));
            filterUnit.setIncoming_call_action(Integer.valueOf(cursor.getString(6)));
            filterUnit.setCall_auto_sms(Boolean.valueOf(cursor.getString(7)));
            filterUnit.setMess_auto_sms(Boolean.valueOf(cursor.getString(8)));
            filterUnit.setAuto_text_content(cursor.getString(9));
            filterUnit.setEnable(Boolean.valueOf(cursor.getString(10)));

            filterUnits.add(filterUnit);
            cursor.moveToNext();
        }
        return filterUnits;
    }

    public ArrayList<FilterUnit> listUnitsWithoutId()
    {
        ArrayList<FilterUnit> filterUnits = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TASK_TABLE, selector, null, null, null,null, null);
      //  Cursor cursor = sqLiteDatabase.rawQuery("select * from tasks", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            FilterUnit filterUnit = new FilterUnit();
            //TODO
            filterUnit.setId(Integer.valueOf(cursor.getString(0)));
            filterUnit.setUnitType(UnitType.valueOf(cursor.getString(1)));
            filterUnit.setProvider(cursor.getString(2));
            filterUnit.setNum(cursor.getString(3));
            filterUnit.setBlocking_incoming_calls(Boolean.valueOf(cursor.getString(4)));
            filterUnit.setBlocking_incoming_mess(Boolean.valueOf(cursor.getString(5)));
            filterUnit.setIncoming_call_action(Integer.valueOf(cursor.getString(6)));
            filterUnit.setCall_auto_sms(Boolean.valueOf(cursor.getString(7)));
            filterUnit.setMess_auto_sms(Boolean.valueOf(cursor.getString(8)));
            filterUnit.setAuto_text_content(cursor.getString(9));
            filterUnit.setEnable(Boolean.valueOf(cursor.getString(10)));

            filterUnits.add(filterUnit);
            cursor.moveToNext();
        }
        return filterUnits;
    }
}
