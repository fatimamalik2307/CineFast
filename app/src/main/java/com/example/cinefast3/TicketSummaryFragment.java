package com.example.cinefast3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class TicketSummaryFragment extends Fragment {

    private Movie movie;
    private int seatCount;
    private List<Snack> snacks;
    private double totalPrice;

    public static TicketSummaryFragment newInstance(Movie movie, int seatCount, List<Snack> snacks) {
        TicketSummaryFragment fragment = new TicketSummaryFragment();
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
        View view = inflater.inflate(R.layout.activity_summary, container, false);

        ImageButton btnBack = view.findViewById(R.id.btnBack);
        ImageView imgPoster = view.findViewById(R.id.imgPoster);
        TextView txtMovie = view.findViewById(R.id.txtMovie);
        TextView txtMeta = view.findViewById(R.id.txtMeta);
        LinearLayout layoutTickets = view.findViewById(R.id.layoutTickets);
        LinearLayout layoutSnacks = view.findViewById(R.id.layoutSnacks);
        TextView txtGrandTotal = view.findViewById(R.id.txtGrandTotal);
        Button btnFinish = view.findViewById(R.id.btnFinish);

        if (movie != null) {
            txtMovie.setText(movie.getName());
            txtMeta.setText(movie.getGenre());
            imgPoster.setImageResource(movie.getPosterResource());
        }

        // Add Ticket Rows
        double ticketPricePerSeat = 16.0;
        for (int i = 0; i < seatCount; i++) {
            addRow(layoutTickets, "Ticket " + (i + 1), "16.00 USD");
        }

        // Add Snack Rows
        double snacksTotal = 0;
        if (snacks != null) {
            for (Snack s : snacks) {
                if (s.getQuantity() > 0) {
                    double itemTotal = s.getPrice() * s.getQuantity();
                    addRow(layoutSnacks, "X" + s.getQuantity() + " " + s.getName(), String.format(Locale.US, "%.2f USD", itemTotal));
                    snacksTotal += itemTotal;
                }
            }
        }

        if (layoutSnacks.getChildCount() == 0) {
            view.findViewById(R.id.lblSmaks).setVisibility(View.GONE);
        }

        totalPrice = (seatCount * ticketPricePerSeat) + snacksTotal;
        txtGrandTotal.setText(String.format(Locale.US, "$%.2f", totalPrice));

        btnBack.setOnClickListener(v -> getActivity().onBackPressed());

        btnFinish.setOnClickListener(v -> {
            saveBooking();
            Toast.makeText(getContext(), "Booking Confirmed!", Toast.LENGTH_SHORT).show();
            
            // Share Ticket logic
            String msg = "🎬 CineFAST Ticket\n\n" +
                    "Movie: " + (movie != null ? movie.getName() : "N/A") + "\n" +
                    "Seats: " + seatCount + "\n" +
                    "TOTAL: " + String.format(Locale.US, "$%.2f", totalPrice);
            
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
            sendIntent.setType("text/plain");
            
            startActivity(Intent.createChooser(sendIntent, "Send Ticket via"));
            
            // Go back to home
            ((MainActivity) getActivity()).loadFragment(new HomeFragment());
        });

        return view;
    }

    private void addRow(LinearLayout container, String leftText, String rightText) {
        RelativeLayout row = new RelativeLayout(getContext());
        row.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        row.setPadding(0, 8, 0, 8);

        TextView txtLeft = new TextView(getContext());
        txtLeft.setText(leftText);
        txtLeft.setTextColor(Color.parseColor("#9CA3AF"));
        txtLeft.setTextSize(15);

        TextView txtRight = new TextView(getContext());
        txtRight.setText(rightText);
        txtRight.setTextColor(Color.WHITE);
        txtRight.setTypeface(null, Typeface.BOLD);
        txtRight.setTextSize(15);

        RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        txtRight.setLayoutParams(rightParams);

        row.addView(txtLeft);
        row.addView(txtRight);
        container.addView(row);
    }

    private void saveBooking() {
        if (movie == null) return;
        SharedPreferences prefs = getActivity().getSharedPreferences("CineFast", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("last_movie", movie.getName());
        editor.putInt("last_seats", seatCount);
        editor.putFloat("last_price", (float) totalPrice);
        editor.apply();
    }
}
