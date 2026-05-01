package com.example.cinefast3;

public class Booking {
    public String bookingId;
    public String userId;
    public String movieName;
    public String posterName;
    public String seats;
    public double totalPrice;
    public String date;
    public String time;

    public Booking() {
        // Default constructor required for calls to DataSnapshot.getValue(Booking.class)
    }

    public Booking(String bookingId, String userId, String movieName, String posterName, String seats, double totalPrice, String date, String time) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.movieName = movieName;
        this.posterName = posterName;
        this.seats = seats;
        this.totalPrice = totalPrice;
        this.date = date;
        this.time = time;
    }
}
