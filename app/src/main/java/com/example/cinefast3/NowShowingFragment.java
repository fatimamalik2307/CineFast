package com.example.cinefast3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class NowShowingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setPadding(16, 16, 16, 16);
        recyclerView.setClipToPadding(false);

        List<Movie> movies = loadMoviesFromJson();

        recyclerView.setAdapter(new MovieAdapter(movies, movie -> {
            SeatSelectionFragment fragment = SeatSelectionFragment.newInstance(movie);
            ((MainActivity) getActivity()).loadFragment(fragment);
        }));

        return recyclerView;
    }

    private List<Movie> loadMoviesFromJson() {
        List<Movie> list = new ArrayList<>();
        try {
            InputStream is = getContext().getAssets().open("movies.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray array = new JSONArray(json);

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                boolean isNowShowing = obj.getBoolean("isNowShowing");
                
                if (isNowShowing) {
                    Movie movie = new Movie(
                        obj.getString("name"),
                        obj.getString("genre"),
                        obj.getString("poster"),
                        obj.getString("trailerUrl"),
                        true
                    );
                    
                    // Convert poster name to drawable resource ID
                    int resId = getContext().getResources().getIdentifier(
                        movie.getPosterName(), "drawable", getContext().getPackageName());
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
