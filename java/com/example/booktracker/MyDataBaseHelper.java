package com.example.booktracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDataBaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME= "Books.db";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_NAME="my_tracker";
    private static final String COLUMN_ID="_id";
    private static final String COLUMN_TITLE="book_title";
    private static final String COLUMN_AUTHOR="book_author";
    private static final String COLUMN_PAGES="book_pages";
    private static final String COLUMN_NOTE="_note";

    public MyDataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE "+ TABLE_NAME +
                " ("+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_TITLE + " TEXT, " +
                COLUMN_AUTHOR + " TEXT, " +
                COLUMN_PAGES + " INTEGER, " +
                COLUMN_NOTE + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    void addBook(String title, String author, int pages, String note ){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGES, pages);
        cv.put(COLUMN_NOTE, note);

        long result= db.insert(TABLE_NAME, null, cv);

        if(result == -1){
            Toast.makeText(context, "Failed",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Added Successfully!",Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData(){
        String query= "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db= this.getReadableDatabase();

        Cursor cursor =null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateBook(String row_id,String title, String author, int pages, String note){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGES, pages);
        cv.put(COLUMN_NOTE, note);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Updated Successfully!",Toast.LENGTH_SHORT).show();
        }
    }

    void deleteBook(String row_id){
        SQLiteDatabase db= this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Deleted Successfully!",Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllBooks(){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, null, null);
        if(result == -1){
            Toast.makeText(context, "Failed to delete all books", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "All books deleted successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}