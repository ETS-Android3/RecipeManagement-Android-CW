package com.wiut12860.recipemanagement.ui.recipes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wiut12860.recipemanagement.CookDatesDBHelper;
import com.wiut12860.recipemanagement.R;
import com.wiut12860.recipemanagement.RecipesDBHelper;

import java.util.Date;

public class RecipeDetails extends AppCompatActivity {

    private long recipeId;
    private RecyclerView.Adapter adapter;
    private CookDatesDBHelper datesDBHelper;
    private SQLiteDatabase dbDates;
    private Cursor result;

    private RecipesDBHelper recipesDBHelper;
    private SQLiteDatabase dbRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        setTitle("Recipe Details");

        Intent i = getIntent();
        recipeId = i.getLongExtra("recipe_id", 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "Click title to edit!", Toast.LENGTH_SHORT).show();

        // recipe details
        recipesDBHelper = new RecipesDBHelper(this);
        dbRecipes = recipesDBHelper.getReadableDatabase();

        Cursor recipe = dbRecipes.query(
                "recipes",
                null,
                "_id = ?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                null,
                "1"
        );

        ImageView ivImg = findViewById(R.id.recipe_img);
        ivImg.setClipToOutline(true);
        TextView tvTitle = findViewById(R.id.recipe_title);
        TextView tvCategory = findViewById(R.id.recipe_category);
        TextView tvDesc = findViewById(R.id.recipe_desc);

        if (recipe.moveToNext()) {
            ivImg.setImageResource(Integer.parseInt(recipe.getString(recipe.getColumnIndexOrThrow("imgpath"))));
            tvTitle.setText(recipe.getString(recipe.getColumnIndexOrThrow("title")));
            tvCategory.setText(recipe.getString(recipe.getColumnIndexOrThrow("category")));
            tvDesc.setText(recipe.getString(recipe.getColumnIndexOrThrow("description")));
        }

        // Cook dates db and listview
        datesDBHelper = new CookDatesDBHelper(this);
        dbDates = datesDBHelper.getReadableDatabase();


        result = dbDates.query(
                "cook_dates",
                null,
                "recipe_id = ?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                null,
                null
        );


        RecyclerView rvCookDates = findViewById(R.id.cook_dates);
        TextView tvEmpty = findViewById(R.id.empty);
        if (result.getCount() == 0) {
            rvCookDates.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            rvCookDates.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }

        rvCookDates.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CookDatesAdapter(this, result, recipeId, rvCookDates, tvEmpty);
        rvCookDates.setAdapter(adapter);

    }

    public void openDateEditForm(View view) {
        Intent i = new Intent(this, CookDateEditForm.class);
        i.putExtra("recipe_id", recipeId);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void deleteRecipe(MenuItem item) {
        dbRecipes.delete("recipes", "_id = ?", new String[]{String.valueOf(recipeId)});
        finish();
    }

    public void updateRecipe(MenuItem item) {
        Intent i = new Intent(this, RecipeEditForm.class);
        i.putExtra("recipe_id", recipeId);
        startActivity(i);
    }
}