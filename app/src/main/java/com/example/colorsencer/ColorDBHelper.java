package com.example.colorsencer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ColorDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "colors.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_COLORS = "colors";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_COLOR_VALUE = "color_value";
    private static final String COLUMN_RGB_VALUE = "rgb_value";

    public ColorDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_COLORS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COLOR_VALUE + " INTEGER, " +
                COLUMN_RGB_VALUE + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLORS);
        onCreate(db);
    }

    public void addColor(int colorValue, String rgbValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COLOR_VALUE, colorValue);
        values.put(COLUMN_RGB_VALUE, rgbValue);
        Log.d("Database", "ColorValue: " + colorValue);
        Log.d("Database", "RGBValue: " + rgbValue);
        long newRowId = db.insert(TABLE_COLORS, null, values);
        db.close();

        if (newRowId != -1) {
            Log.d("Database", "Color inserted into database. Row ID: " + newRowId);
        } else {
            Log.e("Database", "Failed to insert color into database.");
        }
    }
    public List<ColorData> getAllColors() {
        List<ColorData> colorList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {COLUMN_ID, COLUMN_COLOR_VALUE, COLUMN_RGB_VALUE}; // Include all required columns in the query
        Cursor cursor = db.query(TABLE_COLORS, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") int colorValue = cursor.getInt(cursor.getColumnIndex(COLUMN_COLOR_VALUE));
                @SuppressLint("Range") String rgbValue = cursor.getString(cursor.getColumnIndex(COLUMN_RGB_VALUE));

                ColorData colorData = new ColorData(id, colorValue, rgbValue);
                colorList.add(colorData);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return colorList;
    }
    public void deleteColor(int colorId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COLORS, COLUMN_ID + "=?", new String[]{String.valueOf(colorId)});
        db.close();
    }
}