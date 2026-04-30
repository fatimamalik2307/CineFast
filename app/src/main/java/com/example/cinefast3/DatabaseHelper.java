package com.example.cinefast3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CineFast.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_SNACKS = "snacks";

    // Columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_COLOR = "color";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_SNACKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_IMAGE + " INTEGER, " +
                COLUMN_COLOR + " INTEGER)";
        db.execSQL(createTable);
        
        insertInitialSnacks(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SNACKS);
        onCreate(db);
    }

    private void insertInitialSnacks(SQLiteDatabase db) {
        addSnack(db, "Popcorn", "Large / Buttered", 8.99, R.drawable.popcorn, 0xFFFF9800);
        addSnack(db, "Nachos", "With Cheese Dip", 7.99, R.drawable.nachos, 0xFFFF7043);
        addSnack(db, "Soft Drink", "Large / Any Flavor", 5.99, R.drawable.drink, 0xFFE91E63);
        addSnack(db, "Candy Mix", "Assorted Candies", 6.99, R.drawable.candy, 0xFF9C27B0);
    }

    private void addSnack(SQLiteDatabase db, String name, String desc, double price, int image, int color) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, desc);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_COLOR, color);
        db.insert(TABLE_SNACKS, null, values);
    }

    public List<Snack> getAllSnacks() {
        List<Snack> snackList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SNACKS, null);

        if (cursor.moveToFirst()) {
            do {
                Snack snack = new Snack(
                        cursor.getString(1), // name
                        cursor.getString(2), // description
                        cursor.getDouble(3), // price
                        cursor.getInt(4),    // image
                        cursor.getInt(5)     // color
                );
                snackList.add(snack);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return snackList;
    }
}
