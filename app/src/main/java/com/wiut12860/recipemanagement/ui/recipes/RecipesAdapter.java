package com.wiut12860.recipemanagement.ui.recipes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wiut12860.recipemanagement.R;

public class RecipesAdapter extends BaseAdapter {
    private final Context context;
    private Cursor data;

    RecipesAdapter(Context c, Cursor data) {
        this.context = c;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.getCount();
    }

    @Override
    public Cursor getItem(int position) {
        data.moveToPosition(position);
        return data;
    }

    @Override
    public long getItemId(int position) {
        Cursor c = getItem(position);
        return c.getLong(c.getColumnIndexOrThrow("_id"));
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.recipe_list_item, null);
        }

        final Cursor c = getItem(position);
        String title = c.getString(c.getColumnIndexOrThrow("title"));
        String category = c.getString(c.getColumnIndexOrThrow("category"));
        String imgpath = c.getString(c.getColumnIndexOrThrow("imgpath"));

        TextView tvTitle = view.findViewById(R.id.title);
        tvTitle.setText(title);
        TextView tvDesc = view.findViewById(R.id.category);
        tvDesc.setText(view.getResources().getString(R.string.category) + category);
        ImageView img = view.findViewById(R.id.img);
        img.setImageResource(Integer.parseInt(imgpath));
        LinearLayout ll = view.findViewById(R.id.recipe);
        ll.setClipToOutline(true);
        return view;
    }
}

