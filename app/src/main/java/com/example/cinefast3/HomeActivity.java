package com.example.cinefast3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    LinearLayout btnToday, btnTomorrow;
    View dotToday, dotTomorrow;
    TextView txtToday, txtTomorrow;

    Button btnBook1, btnBook2, btnBook3, btnBook4;
    Button btnTrailer1, btnTrailer2, btnTrailer3, btnTrailer4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        btnToday = findViewById(R.id.btnToday);
        btnTomorrow = findViewById(R.id.btnTomorrow);

        dotToday = findViewById(R.id.dotToday);
        dotTomorrow = findViewById(R.id.dotTomorrow);

        txtToday = findViewById(R.id.txtToday);
        txtTomorrow = findViewById(R.id.txtTomorrow);

        btnToday.setOnClickListener(v -> selectToday());
        btnTomorrow.setOnClickListener(v -> selectTomorrow());


        btnBook1 = findViewById(R.id.btnBook1);
        btnBook2 = findViewById(R.id.btnBook2);
        btnBook3 = findViewById(R.id.btnBook3);
        btnBook4 = findViewById(R.id.btnBook4);

        btnTrailer1 = findViewById(R.id.btnTrailer1);
        btnTrailer2 = findViewById(R.id.btnTrailer2);
        btnTrailer3 = findViewById(R.id.btnTrailer3);
        btnTrailer4 = findViewById(R.id.btnTrailer4);


        btnBook1.setOnClickListener(v -> openSeats("The Dark Knight"));
        btnBook2.setOnClickListener(v -> openSeats("Inception"));
        btnBook3.setOnClickListener(v -> openSeats("Interstellar"));
        btnBook4.setOnClickListener(v -> openSeats("The Shawshank Redemption"));


        btnTrailer1.setOnClickListener(v ->
                openTrailer("https://youtu.be/EXeTwQWrcwY"));

        btnTrailer2.setOnClickListener(v ->
                openTrailer("https://youtu.be/YoHD9XEInc0"));

        btnTrailer3.setOnClickListener(v ->
                openTrailer("https://youtu.be/zSWdZVtXT7E"));

        btnTrailer4.setOnClickListener(v ->
                openTrailer("https://youtu.be/PLl99Dyc6iQ"));
    }



    private void selectToday() {
        btnToday.setBackgroundResource(R.drawable.bg_date_selected);
        btnTomorrow.setBackgroundResource(R.drawable.bg_date_unselected);

        dotToday.setBackgroundResource(R.drawable.circle_selected);
        dotTomorrow.setBackgroundResource(R.drawable.circle_unselected);

        txtToday.setTextColor(0xFFFFFFFF);
        txtTomorrow.setTextColor(0xFF9CA3AF);
    }

    private void selectTomorrow() {
        btnTomorrow.setBackgroundResource(R.drawable.bg_date_selected);
        btnToday.setBackgroundResource(R.drawable.bg_date_unselected);

        dotTomorrow.setBackgroundResource(R.drawable.circle_selected);
        dotToday.setBackgroundResource(R.drawable.circle_unselected);

        txtTomorrow.setTextColor(0xFFFFFFFF);
        txtToday.setTextColor(0xFF9CA3AF);
    }



    private void openSeats(String movie) {
        Intent i = new Intent(this, SeatSelectionActivity.class);
        i.putExtra("movie", movie);
        startActivity(i);
    }

    private void openTrailer(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
