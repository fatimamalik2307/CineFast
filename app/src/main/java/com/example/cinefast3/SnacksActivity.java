package com.example.cinefast3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SnacksActivity extends AppCompatActivity {

    int pop = 0, nach = 0, drink = 0, candy = 0;

    double POP_PRICE = 8.99;
    double NACH_PRICE = 7.99;
    double DRINK_PRICE = 5.99;
    double CANDY_PRICE = 6.99;

    TextView popQty, nachQty, drinkQty, candyQty;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_snacks);


        popQty = findViewById(R.id.popQty);
        nachQty = findViewById(R.id.nachQty);
        drinkQty = findViewById(R.id.drinkQty);
        candyQty = findViewById(R.id.candyQty);

        Button popPlus = findViewById(R.id.popPlus);
        Button popMinus = findViewById(R.id.popMinus);
        Button nachPlus = findViewById(R.id.nachPlus);
        Button nachMinus = findViewById(R.id.nachMinus);
        Button drinkPlus = findViewById(R.id.drinkPlus);
        Button drinkMinus = findViewById(R.id.drinkMinus);
        Button candyPlus = findViewById(R.id.candyPlus);
        Button candyMinus = findViewById(R.id.candyMinus);

        Button btnConfirm = findViewById(R.id.btnConfirmSnacks);


        popPlus.setOnClickListener(v -> {
            pop++;
            popQty.setText(String.valueOf(pop));
        });

        popMinus.setOnClickListener(v -> {
            if (pop > 0) pop--;
            popQty.setText(String.valueOf(pop));
        });


        nachPlus.setOnClickListener(v -> {
            nach++;
            nachQty.setText(String.valueOf(nach));
        });

        nachMinus.setOnClickListener(v -> {
            if (nach > 0) nach--;
            nachQty.setText(String.valueOf(nach));
        });


        drinkPlus.setOnClickListener(v -> {
            drink++;
            drinkQty.setText(String.valueOf(drink));
        });

        drinkMinus.setOnClickListener(v -> {
            if (drink > 0) drink--;
            drinkQty.setText(String.valueOf(drink));
        });

        candyPlus.setOnClickListener(v -> {
            candy++;
            candyQty.setText(String.valueOf(candy));
        });

        candyMinus.setOnClickListener(v -> {
            if (candy > 0) candy--;
            candyQty.setText(String.valueOf(candy));
        });

        btnConfirm.setOnClickListener(v -> confirm());
    }

    private void confirm() {

        double snacksTotal =
                pop * POP_PRICE +
                        nach * NACH_PRICE +
                        drink * DRINK_PRICE +
                        candy * CANDY_PRICE;

        Intent in = getIntent();

        Intent i = new Intent(this, SummaryActivity.class);
        i.putExtra("movie", in.getStringExtra("movie"));
        i.putExtra("seats", in.getIntExtra("seats", 0));
        i.putExtra("price", in.getIntExtra("price", 0));
        i.putExtra("seatList", in.getStringExtra("seatList")); // PASSING SEAT LIST
        i.putExtra("snacks", snacksTotal);
        i.putExtra("popQty", pop);
        i.putExtra("nachQty", nach);
        i.putExtra("drinkQty", drink);
        i.putExtra("candyQty", candy);


        startActivity(i);
    }
}
