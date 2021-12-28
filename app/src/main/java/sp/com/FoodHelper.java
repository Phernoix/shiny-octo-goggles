package sp.com;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "foodlist.db";
    private static final int SCHEME_VERSION = 1;

    public FoodHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE food_table(_id INTEGER PRIMARY KEY AUTOINCREMENT,foodRating REAL, foodName TEXT , foodDescription TEXT, foodImage);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Cursor getAll() {
        return (getReadableDatabase().rawQuery(
                "SELECT _id, foodRating, foodName, foodDescription,foodImage FROM food_table ORDER BY foodName", null));

    }

    public Cursor getById(String id) {
        String[] args = {id};

        return (getReadableDatabase().rawQuery(
                "SELECT _id, foodRating, foodName, foodDescription," +
                        "foodImage FROM food_table WHERE _ID=?", args));

    }

    public void insert(Float foodRating, String foodName, String foodDescription, byte[] foodImage) {
        ContentValues cv = new ContentValues();

        cv.put("foodRating", foodRating);
        cv.put("foodName", foodName);
        cv.put("foodDescription", foodDescription);
        cv.put("foodImage", foodImage);

        getWritableDatabase().insert("food_table", "foodName", cv);

    }

    public void insertNoImage(Float foodRating, String foodName, String foodDescription) {
        ContentValues cv = new ContentValues();
        cv.put("foodRating", foodRating);
        cv.put("foodName", foodName);
        cv.put("foodDescription", foodDescription);

        getWritableDatabase().insert("food_table", "foodName", cv);

    }

    public void update(String id, Float foodRating, String foodName, String foodDescription) {
        ContentValues cv = new ContentValues();
        String[] args = {id};
        cv.put("foodRating", foodRating);
        cv.put("foodName", foodName);
        cv.put("foodDescription", foodDescription);

        getWritableDatabase().update("food_table", cv, "_ID=?", args);
    }

    public String getID(Cursor c) {
        return (c.getString(0));
    }

    public Float getFoodRating(Cursor c) {
        return (c.getFloat(1));
    }

    public String getFoodName(Cursor c) {
        return (c.getString(2));
    }

    public String getFoodDescription(Cursor c) {
        return (c.getString(3));
    }

    public byte[] getFoodImage(Cursor c) {
        return (c.getBlob(4));
    }

}
