package com.wiut12860.recipemanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class RecipesDBHelper extends SQLiteOpenHelper {
    public RecipesDBHelper(@Nullable Context context) {
        super(context, "recipes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String rmain = Integer.toString(R.drawable.recipe_main);
        String rdrink = Integer.toString(R.drawable.recipe_drink);
        String rsalad = Integer.toString(R.drawable.recipe_salad);
        String rdessert = Integer.toString(R.drawable.recipe_dessert);

        db.execSQL("CREATE TABLE recipes (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, description TEXT NOT NULL, category TEXT NOT NULL, imgpath TEXT );");

        db.execSQL("INSERT INTO recipes (title, category, description, imgpath) VALUES ('Roasted Chicken', 'Main dish', 'My grandmothers recipe for roasted chicken. We are German and she used to do it this way all the time. I never have had a chicken this juicy before; this little trick works and makes the people eating it go silent. Its funny. We nibble on the celery after.', " + rmain + ");");
        db.execSQL("INSERT INTO recipes (title, category, description, imgpath) VALUES ('Candy Cane Cocoa', 'Drink', 'The rich flavor of chocolate combines so well with peppermint. This is the perfect drink to sip while trimming the tree.', " + rdrink + ");");
        db.execSQL("INSERT INTO recipes (title, category, description, imgpath) VALUES ('Sugar cookie', 'Dessert', 'Find easy recipes for sugar cookies that are perfect for decorating, plus recipes for colored sugar, frosting, and more!', " + rdessert + ");");
        db.execSQL("INSERT INTO recipes (title, category, description, imgpath) VALUES ('All Kale Caesar', 'Salad', 'This kale salad recipe is very quick and simple. Pour remaining dressing into a container, cover, and refrigerate up to 2 weeks.', " + rsalad + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
