package com.example.cinefast3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;

public class SeatSelectionActivity extends AppCompatActivity {

    GridLayout grid;
    TextView txtMovie;
    Button btnSnacks, btnBookOnly;

    HashSet<Integer> selectedSeats = new HashSet<>();
    int seatPrice = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        grid = findViewById(R.id.gridSeats);
        txtMovie = findViewById(R.id.txtMovie);
        btnSnacks = findViewById(R.id.btnSnacks);
        btnBookOnly = findViewById(R.id.btnBookOnly);

        String movie = getIntent().getStringExtra("movie");
        if (movie != null) {
            txtMovie.setText(movie);
        }

        createSeats();
        updateCount();

        btnSnacks.setOnClickListener(v -> goSnacks());
        btnBookOnly.setOnClickListener(v -> goSummary());

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    private void createSeats() {
        grid.removeAllViews();
        grid.setColumnCount(9);
        grid.setRowCount(8);

        for (int i = 0; i < 72; i++) {
            int row = i / 9;
            int col = i % 9;

            if (col == 4) {
                android.view.View gap = new android.view.View(this);
                GridLayout.LayoutParams pGap = new GridLayout.LayoutParams();
                pGap.width = 40;
                pGap.height = 80;
                gap.setLayoutParams(pGap);
                grid.addView(gap);
                continue;
            }

            Button seat = new Button(this);
            GridLayout.LayoutParams p = new GridLayout.LayoutParams();
            p.width = 80;
            p.height = 80;
            p.setMargins(8, 8, 8, 8);
            seat.setLayoutParams(p);
            seat.setText("");

            if ((row == 0 || row == 7) && (col == 0 || col == 8)) {
                seat.setVisibility(android.view.View.INVISIBLE);
                grid.addView(seat);
                continue;
            }

            boolean isBooked = (i == 10 || i == 20 || i == 35 || i == 42 || i == 50 || i == 60 || i == 15);

            if (isBooked) {
                seat.setBackgroundResource(R.drawable.circle_selected_red);
                seat.setEnabled(false);
                seat.setTag("BOOKED");
            } else {
                seat.setBackgroundResource(R.drawable.circle_unselected);
                seat.setTag("AVAILABLE");

                int finalI = i;
                seat.setOnClickListener(v -> toggleSeat(seat, finalI));
            }

            grid.addView(seat);
        }
    }

    private void toggleSeat(Button seat, int index) {
        if (selectedSeats.contains(index)) {
            selectedSeats.remove(index);
            seat.setBackgroundResource(R.drawable.circle_unselected);
        } else {
            selectedSeats.add(index);
            seat.setBackgroundResource(R.drawable.circle_selected);
        }

        updateCount();
    }

    private void updateCount() {
        int count = selectedSeats.size();
        btnSnacks.setEnabled(count > 0);
        btnBookOnly.setEnabled(count > 0);
    }

    private void goSnacks() {
        Intent i = new Intent(this, SnacksActivity.class);
        i.putExtra("movie", txtMovie.getText().toString());
        i.putExtra("seats", selectedSeats.size());
        i.putExtra("price", selectedSeats.size() * seatPrice);
        i.putExtra("seatList", selectedSeats.toString());

        startActivity(i);
    }

    private void goSummary() {
        Intent i = new Intent(this, SummaryActivity.class);
        i.putExtra("movie", txtMovie.getText().toString());
        i.putExtra("seats", selectedSeats.size());
        i.putExtra("price", selectedSeats.size() * seatPrice);
        i.putExtra("seatList", selectedSeats.toString());

        startActivity(i);
    }
}
