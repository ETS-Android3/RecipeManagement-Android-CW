package com.wiut12860.recipemanagement.ui.recipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.wiut12860.recipemanagement.CookDatesDBHelper;
import com.wiut12860.recipemanagement.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class CookDateEditForm extends AppCompatActivity {

    private long recipeId;
    private long dateId;
    private EditText etTitle;
    private DatePicker dpDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_date_edit_form);

        Intent i = getIntent();
        recipeId = i.getLongExtra("recipe_id", 0);
        dateId = i.getLongExtra("date_id", 0);

        etTitle = findViewById(R.id.editDateTitle);
        dpDate = findViewById(R.id.datePicker);
        dpDate.setMinDate(new Date().getTime());

        if (dateId == 0) {
            // Create action
            setTitle("Create date");
        } else {
            // Edit action
            Button submit = findViewById(R.id.save);
            submit.setText(getResources().getString(R.string.update));
            setTitle("Edit cook date");

            // 1. Get given cook date from the DB.
            CookDatesDBHelper dbHelper = new CookDatesDBHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor result = db.query("cook_dates", null, "_id = ?", new String[]{String.valueOf(dateId)}, null, null, null, null);

            if (result.moveToNext()) {
                etTitle.setText(result.getString(result.getColumnIndexOrThrow("title")));
                String dtStr = result.getString(result.getColumnIndexOrThrow("cookdate"));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final Calendar c = Calendar.getInstance();
                try {
                    c.setTime(sdf.parse(dtStr));
                    dpDate.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void createOrUpdateNote(View view) {
        // Get user input
        String title = etTitle.getText().toString();

        int day = dpDate.getDayOfMonth();
        int month = dpDate.getMonth();
        int year = dpDate.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatedDate = sdf.format(calendar.getTime());

        if (title.trim().equals("")) {
            Toast.makeText(this, "Reference can't be empty!", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("cookdate", formatedDate);
            values.put("recipe_id", recipeId);

            CookDatesDBHelper dbHelper = new CookDatesDBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            if (dateId == 0) {
                // Create
                long id = db.insert("cook_dates", null, values);
                if (id > 0) {
                    // Successfully inserted
                    Toast.makeText(this, "Date was added!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    // There was an error
                    Toast.makeText(this, "Error, try again!!!", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Edit
                db.update("cook_dates", values, "_id = ?", new String[]{String.valueOf(dateId)});
                finish();
            }

        }
    }
}