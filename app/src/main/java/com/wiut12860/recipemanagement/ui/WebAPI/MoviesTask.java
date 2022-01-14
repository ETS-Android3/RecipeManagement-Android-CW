package com.wiut12860.recipemanagement.ui.WebAPI;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.wiut12860.recipemanagement.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MoviesTask extends AsyncTask<String, Void, MatrixCursor> {

    private static final String API_KEY = "2050ac8db077eda8b90b74a0f1423975";
    private final ListView lv;

    public MoviesTask(ListView lvMovies) {
        this.lv = lvMovies;
    }

    @Override
    protected MatrixCursor doInBackground(String... page) {
        MatrixCursor mc = new MatrixCursor(new String[]{"_id", "title", "poster_path"}); // properties from the JSONObjects

        try {
            String urlString = String.format(
                    "https://api.themoviedb.org/3/movie/now_playing?api_key=%s&language=en-US&page=%s",
                    API_KEY, page[0]
            );

            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder builder = new StringBuilder();
            String inputString;

            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }

            String response = builder.toString();

            JSONObject topLevel = new JSONObject(response);
            JSONArray results = topLevel.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);
                int id = movie.getInt("id");
                String title = movie.getString("title");
                String poster = "https://image.tmdb.org/t/p/w500" + movie.getString("poster_path");

                try {
                    URL imgURL = new URL(poster);
                    Bitmap image = BitmapFactory.decodeStream(imgURL.openConnection().getInputStream());

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] img = bos.toByteArray();

                    mc.addRow(new Object[]{id, title, img});
                } catch (IOException e) {
                    System.out.println(e);
                }
            }


            urlConnection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return mc;
    }

    @Override
    protected void onPostExecute(MatrixCursor movies) {


        String[] fromColumns = {"title", "poster_path"};
        int[] inToFields = {R.id.movie_title, R.id.movie_img};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(lv.getContext(), R.layout.movie_list_item, movies, fromColumns, inToFields);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.movie_img) {
                    ImageView IV = (ImageView) view;
                    byte[] image = cursor.getBlob(columnIndex);
                    Bitmap bp=BitmapFactory.decodeByteArray(image, 0, image.length);
                    IV.setImageBitmap(bp);
                    return true;
                }
                return false;
            }
        });

        lv.setAdapter(adapter);
    }


}
