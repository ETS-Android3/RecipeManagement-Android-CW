package com.wiut12860.recipemanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

import java.util.Date;

public class CookDatesDBHelper extends SQLiteOpenHelper {
    public CookDatesDBHelper(@Nullable Context context) {
        super(context, "dates.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE cook_dates (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, recipe_id INTEGER NOT NULL, cookdate TEXT NOT NULL);");

        db.execSQL("INSERT INTO cook_dates (title, recipe_id, cookdate) VALUES ('New year', 1, date('2022-01-01'));");
        db.execSQL("INSERT INTO cook_dates (title, recipe_id, cookdate) VALUES ('Friends', 1, date('2021-12-25'));");
        db.execSQL("INSERT INTO cook_dates (title, recipe_id, cookdate) VALUES ('Party', 1, date('2022-01-12'));");
        db.execSQL("INSERT INTO cook_dates (title, recipe_id, cookdate) VALUES ('Family gathering', 2, date('2022-02-02'));");
        db.execSQL("INSERT INTO cook_dates (title, recipe_id, cookdate) VALUES ('Party', 2, date('2021-12-30'));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
