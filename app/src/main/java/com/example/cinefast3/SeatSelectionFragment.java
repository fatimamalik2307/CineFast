package com.example.cinefast3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.HashSet;

public class SeatSelectionFragment extends Fragment {

    private GridLayout grid;
    private TextView txtMovie;
    private Button btnSnacks, btnBookOnly, btnWatchTrailer, btnComingSoon;
    private View layoutNowShowing, layoutComingSoon;
    private View legendBooked, legendYours;
    
    private HashSet<Integer> selectedSeats = new HashSet<>();
    private double seatPrice = 16.0;
    private Movie movie;

    public static SeatSelectionFragment newInstance(Movie movie) {
        return newInstance(movie, null);
    }

    public static SeatSelectionFragment newInstance(Movie movie, HashSet<Integer> preSelectedSeats) {
        SeatSelectionFragment fragment = new SeatSelectionFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie_obj", movie);
        if (preSelectedSeats != null) {
            args.putSerializable("pre_selected_seats", preSelectedSeats);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable("movie_obj");
            HashSet<Integer> pre = (HashSet<Integer>) getArguments().getSerializable("pre_selected_seats");
            if (pre != null) {
                selectedSeats = pre;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seat_selection, container, false);

        grid = view.findViewById(R.id.gridSeats);
        txtMovie = view.findViewById(R.id.txtMovie);
        
        btnSnacks = view.findViewById(R.id.btnSnacks);
        btnBookOnly = view.findViewById(R.id.btnBookOnly);
        btnWatchTrailer = view.findViewById(R.id.btnWatchTrailer);
        btnComingSoon = view.findViewById(R.id.btnComingSoon);
        
        layoutNowShowing = view.findViewById(R.id.layoutNowShowingButtons);
        layoutComingSoon = view.findViewById(R.id.layoutComingSoonButtons);

        legendBooked = view.findViewById(R.id.legendBooked);
        legendYours = view.findViewById(R.id.legendYours);

        if (movie != null) {
            txtMovie.setText(movie.getName());
            
            if (movie.isNowShowing()) {
                layoutNowShowing.setVisibility(View.VISIBLE);
                layoutComingSoon.setVisibility(View.GONE);
                
                legendBooked.getBackground().setTint(0xFFFF0000); // Red
                legendYours.getBackground().setTint(0xFF00FF00);   // Green
            } else {
                layoutNowShowing.setVisibility(View.GONE);
                layoutComingSoon.setVisibility(View.VISIBLE);
                
                legendBooked.getBackground().setTint(0xFFFFFFFF);
                legendYours.getBackground().setTint(0xFFFFFFFF);
                
                btnWatchTrailer.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailerUrl()));
                    startActivity(intent);
                });
            }
        }

        createSeats();
        updateCount();

        btnSnacks.setOnClickListener(v -> goSnacks());
        btnBookOnly.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Booking Confirmed!", Toast.LENGTH_SHORT).show();
            goSummary();
        });

        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    private void createSeats() {
        grid.removeAllViews();
        grid.setColumnCount(9);
        grid.setRowCount(8);

        for (int i = 0; i < 72; i++) {
            int row = i / 9;
            int col = i % 9;

            if (col == 4) {
                View gap = new View(getContext());
                GridLayout.LayoutParams pGap = new GridLayout.LayoutParams();
                pGap.width = 40;
                pGap.height = 80;
                gap.setLayoutParams(pGap);
                grid.addView(gap);
                continue;
            }

            Button seat = new Button(getContext());
            GridLayout.LayoutParams p = new GridLayout.LayoutParams();
            p.width = 80;
            p.height = 80;
            p.setMargins(8, 8, 8, 8);
            seat.setLayoutParams(p);
            seat.setText("");

            if ((row == 0 || row == 7) && (col == 0 || col == 8)) {
                seat.setVisibility(View.INVISIBLE);
                grid.addView(seat);
                continue;
            }

            boolean isBooked = (i == 10 || i == 20 || i == 35 || i == 42 || i == 50 || i == 60 || i == 15);

            if (isBooked && movie != null && movie.isNowShowing()) {
                seat.setBackgroundResource(R.drawable.circle_unselected);
                seat.getBackground().setTint(0xFFFF0000); 
                seat.setEnabled(false);
            } else {
                seat.setBackgroundResource(R.drawable.circle_unselected);
                if (selectedSeats.contains(i)) {
                    seat.getBackground().setTint(0xFF00FF00); // Green for selection
                } else {
                    seat.getBackground().setTint(0xFFFFFFFF); // White for available
                }

                if (movie != null && movie.isNowShowing()) {
                    int finalI = i;
                    seat.setOnClickListener(v -> toggleSeat(seat, finalI));
                } else {
                    seat.setEnabled(false); 
                }
            }

            grid.addView(seat);
        }
    }

    private void toggleSeat(Button seat, int index) {
        if (selectedSeats.contains(index)) {
            selectedSeats.remove(index);
            seat.getBackground().setTint(0xFFFFFFFF); 
        } else {
            selectedSeats.add(index);
            seat.getBackground().setTint(0xFF00FF00); 
        }
        updateCount();
    }

    private void updateCount() {
        int count = selectedSeats.size();
        btnSnacks.setEnabled(count > 0);
        btnBookOnly.setEnabled(count > 0);
    }

    private void goSnacks() {
        SnacksFragment fragment = SnacksFragment.newInstance(movie, selectedSeats);
        ((MainActivity) getActivity()).loadFragment(fragment);
    }

    private void goSummary() {
        TicketSummaryFragment fragment = TicketSummaryFragment.newInstance(movie, selectedSeats, null);
        ((MainActivity) getActivity()).loadFragment(fragment);
    }
}
