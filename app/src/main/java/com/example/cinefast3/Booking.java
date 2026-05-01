package com.example.cinefast3;

import java.util.List;

public class Booking {
    public String userId;
    public String movieName;
    public String seats;
    public double totalPrice;
    public String date;
    public String time;

    public Booking() {
        // Default constructor required for calls to DataSnapshot.getValue(Booking.class)
    }

    public Booking(String userId, String movieName, String seats, double totalPrice, String date, String time) {
        this.userId = userId;
        this.movieName = movieName;
        this.seats = seats;
        this.totalPrice = totalPrice;
        this.date = date;
        this.time = time;
    }
}
