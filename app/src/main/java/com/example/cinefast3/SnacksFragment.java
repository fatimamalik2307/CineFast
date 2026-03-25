package com.example.cinefast3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class SnacksFragment extends Fragment implements SnackAdapter.OnSnackChangeListener {

    private Movie movie;
    private int seatCount;
    private List<Snack> snacks = new ArrayList<>();
    private TextView txtTotal;
    private double totalPrice;

    public static SnacksFragment newInstance(Movie movie, int seatCount) {
        SnacksFragment fragment = new SnacksFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie_obj", movie);
        args.putInt("seat_count", seatCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable("movie_obj");
            seatCount = getArguments().getInt("seat_count");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snacks, container, false);

        ListView listView = view.findViewById(R.id.listSnacks);
        txtTotal = view.findViewById(R.id.txtTotalPrice);
        Button btnConfirm = view.findViewById(R.id.btnConfirmSnacks);

        // Clear and add required snacks with colors from Assignment 1
        snacks.clear();
        snacks.add(new Snack("Popcorn", "Large / Buttered", 8.99, R.drawable.popcorn, 0xFFFF9800));
        snacks.add(new Snack("Nachos", "With Cheese Dip", 7.99, R.drawable.nachos, 0xFFFF7043));
        snacks.add(new Snack("Soft Drink", "Large / Any Flavor", 5.99, R.drawable.drink, 0xFFE91E63));
        snacks.add(new Snack("Candy Mix", "Assorted Candies", 6.99, R.drawable.candy, 0xFF9C27B0));

        updateTotal();

        SnackAdapter adapter = new SnackAdapter(snacks, getLayoutInflater(), this);
        listView.setAdapter(adapter);

        btnConfirm.setOnClickListener(v -> {
            TicketSummaryFragment fragment = TicketSummaryFragment.newInstance(movie, seatCount, snacks);
            ((MainActivity) getActivity()).loadFragment(fragment);
        });

        return view;
    }

    @Override
    public void onQuantityChanged() {
        updateTotal();
    }

    private void updateTotal() {
        double currentTotal = seatCount * 10.0; // Base ticket price
        for (Snack s : snacks) {
            currentTotal += s.getPrice() * s.getQuantity();
        }
        totalPrice = currentTotal;
        txtTotal.setText("Total: $" + String.format("%.2f", totalPrice));
    }
}
