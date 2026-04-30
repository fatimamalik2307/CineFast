package com.example.cinefast3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cinefast.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_SNACKS = "snacks";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_COLOR = "color";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SNACKS_TABLE = "CREATE TABLE " + TABLE_SNACKS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_IMAGE + " TEXT,"
                + COLUMN_DESC + " TEXT,"
                + COLUMN_COLOR + " INTEGER" + ")";
        db.execSQL(CREATE_SNACKS_TABLE);
        
        // Insert initial data
        insertInitialSnacks(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SNACKS);
        onCreate(db);
    }

    private void insertInitialSnacks(SQLiteDatabase db) {
        addSnack(db, "Popcorn", 8.99, "popcorn", "Large / Buttered", 0xFFFF9800);
        addSnack(db, "Nachos", 7.99, "nachos", "With Cheese Dip", 0xFFFF7043);
        addSnack(db, "Soft Drink", 5.99, "drink", "Large / Any Flavor", 0xFFE91E63);
        addSnack(db, "Candy Mix", 6.99, "candy", "Assorted Candies", 0xFF9C27B0);
    }

    private void addSnack(SQLiteDatabase db, String name, double price, String image, String desc, int color) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_DESC, desc);
        values.put(COLUMN_COLOR, color);
        db.insert(TABLE_SNACKS, null, values);
    }

    public List<Snack> getAllSnacks(Context context) {
        List<Snack> snackList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SNACKS, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESC));
                int color = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COLOR));

                int resId = context.getResources().getIdentifier(image, "drawable", context.getPackageName());
                
                Snack snack = new Snack(name, desc, price, resId, color);
                snackList.add(snack);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return snackList;
    }
}
