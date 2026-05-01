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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class TicketSummaryFragment extends Fragment {

    private Movie movie;
    private HashSet<Integer> selectedSeats;
    private List<Snack> snacks;
    private double totalPrice;
    private boolean cameFromSnacks;

    public static TicketSummaryFragment newInstance(Movie movie, HashSet<Integer> selectedSeats, List<Snack> snacks) {
        TicketSummaryFragment fragment = new TicketSummaryFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie_obj", movie);
        args.putSerializable("selected_seats", selectedSeats);
        if (snacks != null) {
            args.putSerializable("snack_list", (Serializable) snacks);
            args.putBoolean("came_from_snacks", true);
        } else {
            args.putBoolean("came_from_snacks", false);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable("movie_obj");
            selectedSeats = (HashSet<Integer>) getArguments().getSerializable("selected_seats");
            snacks = (List<Snack>) getArguments().getSerializable("snack_list");
            cameFromSnacks = getArguments().getBoolean("came_from_snacks");
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

        // Add Ticket Rows with Row/Col formatting
        double ticketPricePerSeat = 16.0;
        int count = 1;
        StringBuilder ticketNames = new StringBuilder();
        if (selectedSeats != null) {
            for (Integer seatIndex : selectedSeats) {
                String seatLabel = formatSeat(seatIndex);
                addRow(layoutTickets, "Ticket " + count + " (" + seatLabel + ")", "16.00 USD");
                ticketNames.append(seatLabel).append(count < selectedSeats.size() ? ", " : "");
                count++;
            }
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
            View lblSnacks = view.findViewById(R.id.lblSmaks);
            if (lblSnacks != null) lblSnacks.setVisibility(View.GONE);
        }

        int seatCount = (selectedSeats != null) ? selectedSeats.size() : 0;
        totalPrice = (seatCount * ticketPricePerSeat) + snacksTotal;
        txtGrandTotal.setText(String.format(Locale.US, "$%.2f", totalPrice));

        btnBack.setOnClickListener(v -> {
            if (cameFromSnacks) {
                SnacksFragment fragment = SnacksFragment.newInstance(movie, selectedSeats, snacks);
                ((MainActivity) getActivity()).loadFragment(fragment);
            } else {
                SeatSelectionFragment fragment = SeatSelectionFragment.newInstance(movie, selectedSeats);
                ((MainActivity) getActivity()).loadFragment(fragment);
            }
        });

        btnFinish.setOnClickListener(v -> {
            saveBookingToFirebase(ticketNames.toString());
            saveBookingToSharedPreferences();
            
            Toast.makeText(getContext(), "Booking Confirmed & Saved!", Toast.LENGTH_SHORT).show();
            
            String msg = "🎬 CineFAST Ticket\n\n" +
                    "Movie: " + (movie != null ? movie.getName() : "N/A") + "\n" +
                    "Seats: " + ticketNames.toString() + "\n" +
                    "TOTAL: " + String.format(Locale.US, "$%.2f", totalPrice);
            
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Send Ticket via"));
            
            ((MainActivity) getActivity()).loadFragment(new HomeFragment());
        });

        return view;
    }

    private void saveBookingToFirebase(String seatsFormatted) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) return;

        String userId = auth.getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("bookings").child(userId);
        
        String bookingId = database.push().getKey();
        
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        
        // MOCK FUTURE DATE FOR TESTING
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 7); // Set date to 7 days in the future
        
        String futureDate = sdfDate.format(cal.getTime());
        String futureTime = sdfTime.format(cal.getTime());

        Booking booking = new Booking(
            bookingId,
            userId,
            movie != null ? movie.getName() : "Unknown",
            movie != null ? movie.getPosterName() : "img_1",
            seatsFormatted,
            totalPrice,
            futureDate,
            futureTime
        );

        if (bookingId != null) {
            database.child(bookingId).setValue(booking);
        }
    }

    private void saveBookingToSharedPreferences() {
        if (movie == null) return;
        SharedPreferences prefs = getActivity().getSharedPreferences("CineFast", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("last_movie", movie.getName());
        editor.putInt("last_seats", (selectedSeats != null) ? selectedSeats.size() : 0);
        editor.putFloat("last_price", (float) totalPrice);
        editor.apply();
    }

    private String formatSeat(int index) {
        int rowIdx = index / 9;
        int colIdx = index % 9;
        char rowLetter = (char) ('A' + rowIdx);
        int seatNum = (colIdx < 4) ? colIdx + 1 : colIdx;
        return "Row " + rowLetter + ", Seat " + seatNum;
    }

    private void addRow(LinearLayout container, String leftText, String rightText) {
        RelativeLayout row = new RelativeLayout(getContext());
        row.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        row.setPadding(0, 8, 0, 8);

        TextView txtLeft = new TextView(getContext());
        txtLeft.setText(leftText);
        txtLeft.setTextColor(Color.parseColor("#9CA3AF"));
        txtLeft.setTextSize(14);

        TextView txtRight = new TextView(getContext());
        txtRight.setText(rightText);
        txtRight.setTextColor(Color.WHITE);
        txtRight.setTypeface(null, Typeface.BOLD);
        txtRight.setTextSize(14);

        RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        txtRight.setLayoutParams(rightParams);

        row.addView(txtLeft);
        row.addView(txtRight);
        container.addView(row);
    }
}
