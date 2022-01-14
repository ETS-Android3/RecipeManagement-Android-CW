package com.wiut12860.recipemanagement.ui.recipes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wiut12860.recipemanagement.R;
import com.wiut12860.recipemanagement.RecipesDBHelper;
import com.wiut12860.recipemanagement.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ListView lvRecipes;
    private Cursor result;
    private RecipesDBHelper dbHelper;
    private SQLiteDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // List view
        lvRecipes = root.findViewById(R.id.recipes);
        lvRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), RecipeDetails.class);
                i.putExtra("recipe_id", id);
                startActivity(i);
            }
        });

        dbHelper = new RecipesDBHelper(getActivity());
        db = dbHelper.getReadableDatabase();


        // Spinner filter
        Spinner spFilter = root.findViewById(R.id.spinner_filter);
        spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (position == 0) {
                    getAll();
                } else {
                    result = db.query(
                            "recipes",
                            null,
                            "category = ?",
                            new String[]{spFilter.getItemAtPosition(position).toString()},
                            null,
                            null,
                            null,
                            null
                    );
                }

                refreshView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getAll();
                refreshView();
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        getAll();
        refreshView();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void refreshView() {
        RecipesAdapter adapter = new RecipesAdapter(getActivity(), result);
        lvRecipes.setAdapter(adapter);
    }

    public void getAll() {
        result = db.query(
                "recipes",
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}