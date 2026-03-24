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

public class ComingSoonFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setPadding(16, 16, 16, 16);
        recyclerView.setClipToPadding(false);

        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Wonder Woman", "Action • 141 min", R.drawable.wonder, "https://youtu.be/1Q8fG0zokhc", false));
        movies.add(new Movie("Cinderella", "Fantasy • 105 min", R.drawable.cinderella, "https://youtu.be/20DGwLpC4O4", false));
        movies.add(new Movie("Marvel's Avengers", "Action • 143 min", R.drawable.marvel, "https://youtu.be/eOrNdBpGMv8", false));

        recyclerView.setAdapter(new MovieAdapter(movies, movie -> {
            SeatSelectionFragment fragment = SeatSelectionFragment.newInstance(movie);
            ((MainActivity) getActivity()).loadFragment(fragment);
        }));

        return recyclerView;
    }
}
