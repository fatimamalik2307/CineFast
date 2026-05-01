package com.example.cinefast3;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository {

    public static List<Movie> getMovies(Context context, boolean nowShowing) {
        List<Movie> list = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("movies.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray array = new JSONArray(json);

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                boolean movieStatus = obj.getBoolean("isNowShowing");
                
                if (movieStatus == nowShowing) {
                    Movie movie = new Movie(
                        obj.getString("name"),
                        obj.getString("genre"),
                        obj.getString("poster"),
                        obj.getString("trailerUrl"),
                        movieStatus,
                        obj.optString("date", "01.01.2024"), // Default to past if missing
                        obj.optString("time", "12:00")
                    );
                    
                    int resId = context.getResources().getIdentifier(
                        movie.getPosterName(), "drawable", context.getPackageName());
                    movie.setPosterResource(resId);
                    
                    list.add(movie);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
