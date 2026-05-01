package com.example.cinefast3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyBookingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private List<Booking> bookingList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_bookings, container, false);

        // Header Back Button
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        recyclerView = view.findViewById(R.id.rvMyBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("bookings").child(userId);

        adapter = new BookingAdapter(bookingList, this::onCancelClick);
        recyclerView.setAdapter(adapter);

        fetchBookings();

        return view;
    }

    private void fetchBookings() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Booking booking = dataSnapshot.getValue(Booking.class);
                    if (booking != null) {
                        bookingList.add(booking);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onCancelClick(Booking booking) {
        if (canCancel(booking)) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Cancel Booking")
                    .setMessage("Are you sure you want to cancel this booking?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteBooking(booking))
                    .setNegativeButton("No", null)
                    .show();
        } else {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Cannot Cancel")
                    .setMessage("This booking is in the past and cannot be canceled.")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    private boolean canCancel(Booking booking) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            String bookingDateTime = booking.date + " " + booking.time;
            Date bDate = sdf.parse(bookingDateTime);
            Date now = new Date();
            return bDate != null && bDate.after(now);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void deleteBooking(Booking booking) {
        databaseReference.child(booking.bookingId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful() && getContext() != null) {
                Toast.makeText(getContext(), "Booking Cancelled Successfully", Toast.LENGTH_SHORT).show();
            } else if (getContext() != null) {
                Toast.makeText(getContext(), "Failed to cancel booking", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
