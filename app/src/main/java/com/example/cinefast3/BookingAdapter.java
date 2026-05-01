package com.example.cinefast3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private final List<Booking> bookings;
    private final OnCancelClickListener cancelClickListener;

    public interface OnCancelClickListener {
        void onCancelClick(Booking booking);
    }

    public BookingAdapter(List<Booking> bookings, OnCancelClickListener cancelClickListener) {
        this.bookings = bookings;
        this.cancelClickListener = cancelClickListener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.txtName.setText(booking.movieName);
        holder.txtDateTime.setText(booking.date + " • " + booking.time);
        
        // Count tickets
        int ticketCount = 0;
        if (booking.seats != null && !booking.seats.isEmpty()) {
            ticketCount = booking.seats.split(",").length;
        }
        holder.txtTickets.setText(ticketCount + (ticketCount == 1 ? " Ticket" : " Tickets"));

        // Load poster with improved fallback
        if (booking.posterName != null) {
            int resId = holder.itemView.getContext().getResources().getIdentifier(
                    booking.posterName, "drawable", holder.itemView.getContext().getPackageName());
            
            if (resId != 0) {
                holder.imgPoster.setImageResource(resId);
            } else {
                holder.imgPoster.setImageResource(R.drawable.cinefast_logo);
            }
        } else {
            // This handles the "P" issue for old data
            holder.imgPoster.setImageResource(R.drawable.cinefast_logo);
        }

        holder.btnCancel.setOnClickListener(v -> cancelClickListener.onCancelClick(booking));
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView txtName, txtDateTime, txtTickets;
        ImageButton btnCancel;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            txtName = itemView.findViewById(R.id.txtMovieName);
            txtDateTime = itemView.findViewById(R.id.txtDateTime);
            txtTickets = itemView.findViewById(R.id.txtTickets);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}
