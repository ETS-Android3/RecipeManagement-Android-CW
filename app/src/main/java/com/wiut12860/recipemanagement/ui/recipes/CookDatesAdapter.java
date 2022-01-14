package com.wiut12860.recipemanagement.ui.recipes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wiut12860.recipemanagement.CookDatesDBHelper;
import com.wiut12860.recipemanagement.R;

public class CookDatesAdapter extends RecyclerView.Adapter<CookDatesAdapter.ViewHolder> {
    private final Context context;
    private Cursor data;
    private final long recipeId;
    private final RecyclerView rvDates;
    private final TextView tvEmpty;


    public CookDatesAdapter(Context c, Cursor data, long recipe_id, RecyclerView rvDates, TextView tvEmpty) {
        this.context = c;
        this.data = data;
        this.recipeId = recipe_id;
        this.rvDates = rvDates;
        this.tvEmpty = tvEmpty;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDatesTitle;
        private final TextView tvDatesDate;
        private final ImageButton ibDelete;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            tvDatesTitle = (TextView) view.findViewById(R.id.cook_dates_title);
            tvDatesDate = (TextView) view.findViewById(R.id.cook_dates_date);
            ibDelete = (ImageButton) view.findViewById(R.id.cook_dates_delete);
        }

        public TextView getTvDatesTitle() {
            return tvDatesTitle;
        }

        public TextView getTvDatesDate() {
            return tvDatesDate;
        }

        public ImageButton getIbDelete() {
            return ibDelete;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(CookDatesAdapter.this.context)
                .inflate(R.layout.cook_dates_list_item, viewGroup, false);


        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final long primaryKey = getItemId(position);
        data.moveToPosition(position);
        final Cursor c = getItem(position);
        viewHolder.getTvDatesTitle().setText(c.getString(c.getColumnIndexOrThrow("title")));
        viewHolder.getTvDatesDate().setText(c.getString(c.getColumnIndexOrThrow("cookdate")));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CookDatesAdapter.this.context, CookDateEditForm.class);
                i.putExtra("recipe_id", recipeId);
                i.putExtra("date_id", primaryKey);
                context.startActivity(i);
            }
        });

        viewHolder.getIbDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Get the database
                CookDatesDBHelper dbHelper = new CookDatesDBHelper(v.getContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // 2. Delete from the DB`
                db.delete("cook_dates", "_id = ?", new String[]{String.valueOf(primaryKey)});

                data = db.query(
                        "cook_dates",
                        null,
                        "recipe_id = ?",
                        new String[]{String.valueOf(recipeId)},
                        null,
                        null,
                        null,
                        null
                );
                notifyDataSetChanged();

                if (data.getCount() == 0) {
                    rvDates.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                } else {
                    rvDates.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.GONE);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.getCount();
    }

    public Cursor getItem(int position) {
        data.moveToPosition(position);
        return data;
    }

    @Override
    public long getItemId(int position) {
        Cursor c = getItem(position);
        return c.getLong(c.getColumnIndexOrThrow("_id"));
    }
}
