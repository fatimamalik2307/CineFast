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

public class MovieListFragment extends Fragment {

    private static final String ARG_IS_NOW_SHOWING = "is_now_showing";
    private boolean isNowShowing;

    public static MovieListFragment newInstance(boolean isNowShowing) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_NOW_SHOWING, isNowShowing);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isNowShowing = getArguments().getBoolean(ARG_IS_NOW_SHOWING);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setPadding(16, 16, 16, 16);
        recyclerView.setClipToPadding(false);

        List<Movie> movies = new ArrayList<>();
        if (isNowShowing) {
            movies.add(new Movie("The Dark Knight", "Action • 152 min", "dark_knight", "https://youtu.be/EXeTwQWrcwY", true));
            movies.add(new Movie("Inception", "Sci-Fi • 148 min", "inception", "https://youtu.be/YoHD9XEInc0", true));
            movies.add(new Movie("Interstellar", "Sci-Fi • 169 min", "interstellar", "https://youtu.be/zSWdZVtXT7E", true));
        } else {
            movies.add(new Movie("Wonder Woman", "Action • 141 min", "wonder", "https://youtu.be/1Q8fG0zokhc", false));
            movies.add(new Movie("Cinderella", "Fantasy • 105 min", "cinderella", "https://youtu.be/20DGwLpC4O4", false));
            movies.add(new Movie("Marvel's Avengers", "Action • 143 min", "marvel", "https://youtu.be/eOrNdBpGMv8", false));
        }

        // Set resource IDs manually for these hardcoded movies since this fragment still uses a hardcoded list
        for (Movie m : movies) {
            int resId = getContext().getResources().getIdentifier(m.getPosterName(), "drawable", getContext().getPackageName());
            m.setPosterResource(resId);
        }

        recyclerView.setAdapter(new MovieAdapter(movies, movie -> {
            SeatSelectionFragment fragment = SeatSelectionFragment.newInstance(movie);
            ((MainActivity) getActivity()).loadFragment(fragment);
        }));

        return recyclerView;
    }
}
