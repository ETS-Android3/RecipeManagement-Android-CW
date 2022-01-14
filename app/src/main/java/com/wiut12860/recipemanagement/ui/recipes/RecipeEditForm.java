package com.wiut12860.recipemanagement.ui.recipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.wiut12860.recipemanagement.R;
import com.wiut12860.recipemanagement.RecipesDBHelper;


public class RecipeEditForm extends AppCompatActivity {
    private long recipeId;
    private EditText etTitle;
    private Spinner spCategory;
    private EditText etDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_edit_form);

        Intent i = getIntent();
        recipeId = i.getLongExtra("recipe_id", 0);

        etTitle = findViewById(R.id.recipe_title);
        spCategory = findViewById(R.id.category);
        etDesc = findViewById(R.id.recipe_description);


        if (recipeId == 0) {
            // Create action
            setTitle("Create recipe");
        } else {
            // Edit action
            Button submit = findViewById(R.id.submit_recipe);
            submit.setText(getResources().getString(R.string.update));
            setTitle("Edit recipe");

            // 1. Get given recipe from the DB.
            RecipesDBHelper dbHelper = new RecipesDBHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor result = db.query("recipes", null, "_id = ?", new String[]{String.valueOf(recipeId)}, null, null, null, null);


            if (result.moveToNext()) {
                etTitle.setText(result.getString(result.getColumnIndexOrThrow("title")));
                spCategory.setSelection(getIndex(spCategory, result.getString(result.getColumnIndexOrThrow("category"))));
                etDesc.setText(result.getString(result.getColumnIndexOrThrow("description")));
            }

        }
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    public void createOrUpdateRecipe(View view) {
        String title = etTitle.getText().toString();
        String desc = etDesc.getText().toString();
        String category = spCategory.getSelectedItem().toString();

        if (title.trim().equals("") || desc.trim().equals("") || category.trim().equals("")) {
            Toast.makeText(this, "Inputs can't be empty!", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("description", desc);
            values.put("category", category);

            if (category.equalsIgnoreCase("Main dish")) {
                values.put("imgpath", Integer.toString(R.drawable.recipe_main));
            } else if (category.equalsIgnoreCase("Side dish")) {
                values.put("imgpath", Integer.toString(R.drawable.recipe_side));
            } else if (category.equalsIgnoreCase("Salad")) {
                values.put("imgpath", Integer.toString(R.drawable.recipe_salad));
            } else if (category.equalsIgnoreCase("Drink")) {
                values.put("imgpath", Integer.toString(R.drawable.recipe_drink));
            } else if (category.equalsIgnoreCase("Dessert")) {
                values.put("imgpath", Integer.toString(R.drawable.recipe_dessert));
            } else {
                values.put("imgpath", Integer.toString(R.drawable.logo));
            }

            RecipesDBHelper dbHelper = new RecipesDBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            if (recipeId == 0) {
                // Create
                long id = db.insert("recipes", null, values);
                if (id > 0) {
                    // Successfully inserted
                    Toast.makeText(this, "Recipe was added!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    // There was an error
                    Toast.makeText(this, "Error, try again!!!", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Edit
                db.update("recipes", values, "_id = ?", new String[]{String.valueOf(recipeId)});
                finish();
            }
        }
    }


}