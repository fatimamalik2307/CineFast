package com.example.cinefast3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.io.Serializable;
import java.util.List;

public class SummaryFragment extends Fragment {

    private Movie movie;
    private int seatCount;
    private List<Snack> snacks;
    private double totalPrice;

    public static SummaryFragment newInstance(Movie movie, int seatCount, List<Snack> snacks) {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie_obj", movie);
        args.putInt("seat_count", seatCount);
        if (snacks != null) {
            args.putSerializable("snack_list", (Serializable) snacks);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable("movie_obj");
            seatCount = getArguments().getInt("seat_count");
            snacks = (List<Snack>) getArguments().getSerializable("snack_list");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        TextView txtMovieName = view.findViewById(R.id.summaryMovieName);
        TextView txtSeats = view.findViewById(R.id.summarySeats);
        TextView txtPrice = view.findViewById(R.id.summaryTotalPrice);
        Button btnConfirm = view.findViewById(R.id.btnFinalConfirm);
        Button btnHome = view.findViewById(R.id.btnGoHome);

        if (movie != null) {
            txtMovieName.setText(movie.getName());
        }
        txtSeats.setText(String.valueOf(seatCount));

        totalPrice = seatCount * 10.0;
        if (snacks != null) {
            for (Snack s : snacks) {
                totalPrice += s.getPrice() * s.getQuantity();
            }
        }
        txtPrice.setText("$" + String.format("%.2f", totalPrice));

        btnConfirm.setOnClickListener(v -> {
            saveBooking();
            Toast.makeText(getContext(), "Booking Confirmed!", Toast.LENGTH_SHORT).show();
            ((MainActivity) getActivity()).loadFragment(new HomeFragment());
        });

        btnHome.setOnClickListener(v -> {
            ((MainActivity) getActivity()).loadFragment(new HomeFragment());
        });

        return view;
    }

    private void saveBooking() {
        SharedPreferences prefs = getActivity().getSharedPreferences("CineFast", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("last_movie", movie.getName());
        editor.putInt("last_seats", seatCount);
        editor.putFloat("last_price", (float) totalPrice);
        editor.apply();
    }
}
