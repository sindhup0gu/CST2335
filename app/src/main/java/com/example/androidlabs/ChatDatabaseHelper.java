package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "MessagesDB";
    private static final String DB_TABLE = "Messages_Table";
    //columns
    private static final String COL_MESSAGE = "Message";
    private static final String COL_ISSEND = "IsSend";
    private static final String COL_MESSAGEID = "MessageID";
    //queries
    private static final String CREATE_TABLE = "CREATE TABLE "+DB_TABLE+" ("+COL_MESSAGEID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COL_MESSAGE+" TEXT, "+COL_ISSEND+" BIT);";

    public ChatDatabaseHelper(Context context) {
        super(context, DB_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }

    public boolean insertData(String message, boolean isSend) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_MESSAGE, message);
        if (isSend)
            contentValues.put(COL_ISSEND, 0);
        else
            contentValues.put(COL_ISSEND, 1);
        long result = db.insert(DB_TABLE, null, contentValues);
        return result != -1; //if result = -1 data doesn't insert
    }

    public Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from "+DB_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        Log.e("Database Version Number", Integer.toString(db.getVersion()));
        Log.e("Column Count in cursor", Integer.toString(cursor.getColumnCount()));
//        for (int i = 0; i < ; i++){
//            Log.e("Column Names", String.valueOf(cursor.getColumnNames().toString()));
//        }
        Log.e("Column Names", Arrays.toString(cursor.getColumnNames()));
        Log.e("Count of results", Integer.toString(cursor.getCount()));
        Log.e("Each row of results", DatabaseUtils.dumpCursorToString(cursor));
        return cursor;
    }
}