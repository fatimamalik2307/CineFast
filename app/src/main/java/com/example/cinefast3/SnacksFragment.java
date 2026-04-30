package com.example.cinefast3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SnacksFragment extends Fragment implements SnackAdapter.OnSnackChangeListener {

    private Movie movie;
    private HashSet<Integer> selectedSeats;
    private List<Snack> snacks = new ArrayList<>();
    private double totalPrice;
    private DatabaseHelper dbHelper;

    public static SnacksFragment newInstance(Movie movie, HashSet<Integer> selectedSeats) {
        return newInstance(movie, selectedSeats, null);
    }

    public static SnacksFragment newInstance(Movie movie, HashSet<Integer> selectedSeats, List<Snack> preSelectedSnacks) {
        SnacksFragment fragment = new SnacksFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie_obj", movie);
        args.putSerializable("selected_seats", selectedSeats);
        if (preSelectedSnacks != null) {
            args.putSerializable("pre_selected_snacks", (Serializable) preSelectedSnacks);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(requireContext());
        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable("movie_obj");
            selectedSeats = (HashSet<Integer>) getArguments().getSerializable("selected_seats");
            List<Snack> pre = (List<Snack>) getArguments().getSerializable("pre_selected_snacks");
            if (pre != null) {
                snacks = pre;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snacks, container, false);

        ListView listView = view.findViewById(R.id.listSnacks);
        Button btnConfirm = view.findViewById(R.id.btnConfirmSnacks);

        // Load snacks from SQLite Database if list is empty
        if (snacks.isEmpty()) {
            snacks = dbHelper.getAllSnacks();
        }

        updateTotal();

        SnackAdapter adapter = new SnackAdapter(snacks, getLayoutInflater(), this);
        listView.setAdapter(adapter);

        btnConfirm.setOnClickListener(v -> {
            TicketSummaryFragment fragment = TicketSummaryFragment.newInstance(movie, selectedSeats, snacks);
            ((MainActivity) getActivity()).loadFragment(fragment);
        });

        ImageButton btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                SeatSelectionFragment fragment = SeatSelectionFragment.newInstance(movie, selectedSeats);
                ((MainActivity) getActivity()).loadFragment(fragment);
            });
        }

        return view;
    }

    @Override
    public void onQuantityChanged() {
        updateTotal();
    }

    private void updateTotal() {
        // Base price calculation (Seats)
        double currentTotal = selectedSeats != null ? selectedSeats.size() * 16.0 : 0; 
        
        // Add snack prices
        if (snacks != null) {
            for (Snack s : snacks) {
                currentTotal += s.getPrice() * s.getQuantity();
            }
        }
        totalPrice = currentTotal;
        // If there was a TextView for total, it should be updated here.
    }
}
