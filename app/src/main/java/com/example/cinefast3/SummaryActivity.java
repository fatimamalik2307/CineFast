package com.example.cinefast3;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_summary);

        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageView imgPoster = findViewById(R.id.imgPoster);

        TextView txtMovie = findViewById(R.id.txtMovie);
        TextView txtMeta = findViewById(R.id.txtMeta);

        LinearLayout layoutTickets = findViewById(R.id.layoutTickets);
        LinearLayout layoutSnacks = findViewById(R.id.layoutSnacks);
        TextView txtGrandTotal = findViewById(R.id.txtGrandTotal);

        TextView txtTheater = findViewById(R.id.txtTheater);
        TextView txtHall = findViewById(R.id.txtHall);
        TextView txtDate = findViewById(R.id.txtDate);
        TextView txtTime = findViewById(R.id.txtTime);

        Button btnFinish = findViewById(R.id.btnFinish);

        Intent in = getIntent();

        String movie = in.getStringExtra("movie");
        if (movie == null) movie = "Movie";

        int ticketPricePerSeat = 16; 
        double snacksTotal = in.getDoubleExtra("snacks", 0);
        String seatListStr = in.getStringExtra("seatList");
        
        int popQty = in.getIntExtra("popQty", 0);
        int nachQty = in.getIntExtra("nachQty", 0);
        int drinkQty = in.getIntExtra("drinkQty", 0);
        int candyQty = in.getIntExtra("candyQty", 0);

        txtMovie.setText(movie);

        if (movie.contains("Interstellar")) {
            imgPoster.setImageResource(R.drawable.interstellar);
            txtMeta.setText("Sci-Fi • 169 min");
        } else if (movie.contains("Inception")) {
            imgPoster.setImageResource(R.drawable.inception);
            txtMeta.setText("Sci-Fi • 148 min");
        } else if (movie.contains("The Dark Knight")) {
            imgPoster.setImageResource(R.drawable.dark_knight);
            txtMeta.setText("Action • 152 min");
        } else if (movie.contains("The Shawshank Redemption")) {
            imgPoster.setImageResource(R.drawable.shawshank);
            txtMeta.setText("Drama • 142 min");
        } else {
            txtMeta.setText("Action • 120 min");
        }

        txtTheater.setText("Stars Mall");
        txtHall.setText("Hall 01");
        txtDate.setText("13 Apr");
        txtTime.setText("22:15");

        int seatCount = 0;
        StringBuilder ticketSummary = new StringBuilder();
        if (seatListStr != null && !seatListStr.isEmpty() && !seatListStr.equals("None")) {
            String clean = seatListStr.replace("[", "").replace("]", "").trim();
            if (!clean.isEmpty()) {
                String[] ids = clean.split(",");
                for (String idStr : ids) {
                    try {
                        int id = Integer.parseInt(idStr.trim());
                        String seat = formatSeat(id);
                        addRow(layoutTickets, seat, ticketPricePerSeat + " USD");
                        ticketSummary.append(seat).append(", ");
                        seatCount++;
                    } catch (Exception ignored) {}
                }
            }
        }

        if (popQty > 0) addRow(layoutSnacks, "X" + popQty + " Popcorn", (popQty * 8.99) + " USD");
        if (nachQty > 0) addRow(layoutSnacks, "X" + nachQty + " Nachos", (nachQty * 7.99) + " USD");
        if (drinkQty > 0) addRow(layoutSnacks, "X" + drinkQty + " Drink", (drinkQty * 5.99) + " USD");
        if (candyQty > 0) addRow(layoutSnacks, "X" + candyQty + " Candy", (candyQty * 6.99) + " USD");

        if (layoutSnacks.getChildCount() == 0) {
            findViewById(R.id.lblSmaks).setVisibility(View.GONE);
        }

        double totalTickets = seatCount * ticketPricePerSeat;
        double grand = totalTickets + snacksTotal;
        txtGrandTotal.setText(String.format(Locale.US, "$%.2f", grand));

        btnBack.setOnClickListener(v -> finish());
        
        final String fMovie = movie;
        final String fTickets = ticketSummary.toString();
        final double fGrand = grand;

        btnFinish.setOnClickListener(v -> {
            String msg = "🎬 CineFAST Ticket\n\n" +
                    "Movie: " + fMovie + "\n" +
                    "Theater: Stars Mall\n" +
                    "Seats: " + fTickets + "\n" +
                    "TOTAL: " + String.format(Locale.US, "$%.2f", fGrand);

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp"); 

            try {
                startActivity(sendIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                // If WhatsApp is not installed, open the generic chooser
                Intent chooser = Intent.createChooser(sendIntent, "Send Ticket via");
                startActivity(chooser);
            }
        });
    }

    private String formatSeat(int index) {
        int rowIdx = index / 9;
        int colIdx = index % 9;
        char rowLetter = (char) ('A' + rowIdx);
        int seatNum = (colIdx < 4) ? colIdx + 1 : colIdx;
        return "Row " + rowLetter + ", Seat " + seatNum;
    }

    private void addRow(LinearLayout container, String leftText, String rightText) {
        RelativeLayout row = new RelativeLayout(this);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        row.setPadding(0, 8, 0, 8);

        TextView txtLeft = new TextView(this);
        txtLeft.setText(leftText);
        txtLeft.setTextColor(Color.parseColor("#9CA3AF"));
        txtLeft.setTextSize(15);

        TextView txtRight = new TextView(this);
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
}
