package com.faisal.se328;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by Mitch on 2016-05-13.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "students.db";
    public static final String TABLE_NAME = "students_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "NAME";
    public static final String COL3 = "Surname";
    public static final String COL4 = "FatherName";
    public static final String COL5 = "NationalID";
    public static final String COL6 = "DateOfBirth";
    public static final String COL7 = "Gender";


    /* Constructor */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /* Code runs automatically when the dB is created */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME +
                " (ID INTEGER PRIMARY KEY, " +
                " NAME TEXT, Surname TEXT , FatherName TEXT , NationalID TEXT , DateOfBirth TEXT , Gender TEXT)";
        db.execSQL(createTable);
    }

    /* Every time the dB is updated (or upgraded) */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /* Basic function to add data. REMEMBER: The fields
       here, must be in accordance with those in
       the onCreate method above.
    */
    public boolean addData(String ID ,String Name, String Surname , String FatherName , String NID , String dob , String Gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, ID);
        contentValues.put(COL2, Name);
        contentValues.put(COL3, Surname);
        contentValues.put(COL4, FatherName);
        contentValues.put(COL5, NID);
        contentValues.put(COL6, dob);
        contentValues.put(COL7, Gender);



        long result = db.insert(TABLE_NAME, null, contentValues);

        //if data are inserted incorrectly, it will return -1
        if(result == -1) {return false;} else {return true;}
    }

    /* Returns only one result */
    public Cursor structuredQuery(String ID) {
        SQLiteDatabase db = this.getReadableDatabase(); // No need to write
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL1,
                        COL2, COL3 , COL4 , COL5 , COL6 , COL7}, COL1 + "=?",
                new String[]{ID}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    //TODO: Students need to try to fix this
    public Cursor getSpecificResult(String SID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME+" Where ID =\"" + SID+"\"",null);
        return data;
    }

    // Return everything inside the dB
    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

    // delete a specific row by id
    public boolean deleteData(String student_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL1 + "=" + student_id, null) > 0;
    }

    public boolean updateData(String ID ,String Name, String Surname , String FatherName , String NID , String dob , String Gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, ID);
        contentValues.put(COL2, Name);
        contentValues.put(COL3, Surname);
        contentValues.put(COL4, FatherName);
        contentValues.put(COL5, NID);
        contentValues.put(COL6, dob);
        contentValues.put(COL7, Gender);



        long result = db.update(TABLE_NAME,contentValues,COL1+"=?",new String[]{ID});

        //if data are inserted incorrectly, it will return -1
        if(result == -1) {return false;} else {return true;}
    }
}