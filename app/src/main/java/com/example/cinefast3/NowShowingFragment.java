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

        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("The Dark Knight", "Action • 152 min", R.drawable.dark_knight, "https://youtu.be/EXeTwQWrcwY", true));
        movies.add(new Movie("Inception", "Sci-Fi • 148 min", R.drawable.inception, "https://youtu.be/YoHD9XEInc0", true));
        movies.add(new Movie("Interstellar", "Sci-Fi • 169 min", R.drawable.interstellar, "https://youtu.be/zSWdZVtXT7E", true));

        recyclerView.setAdapter(new MovieAdapter(movies, movie -> {
            SeatSelectionFragment fragment = SeatSelectionFragment.newInstance(movie);
            ((MainActivity) getActivity()).loadFragment(fragment);
        }));

        return recyclerView;
    }
}
