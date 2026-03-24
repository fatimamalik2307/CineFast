package com.example.cinefast3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        ImageView menuMore = view.findViewById(R.id.menu_more);

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) return MovieListFragment.newInstance(true);
                return MovieListFragment.newInstance(false);
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(position == 0 ? "Now Showing" : "Coming Soon");
        }).attach();

        menuMore.setOnClickListener(v -> showLastBooking());

        return view;
    }

    private void showLastBooking() {
        SharedPreferences prefs = getActivity().getSharedPreferences("CineFast", Context.MODE_PRIVATE);
        String movie = prefs.getString("last_movie", null);
        
        if (movie == null) {
            new AlertDialog.Builder(getContext())
                .setTitle("Last Booking")
                .setMessage("No previous booking found.")
                .setPositiveButton("OK", null)
                .show();
        } else {
            int seats = prefs.getInt("last_seats", 0);
            double price = prefs.getFloat("last_price", 0);
            
            new AlertDialog.Builder(getContext())
                .setTitle("Last Booking")
                .setMessage("Movie: " + movie + "\nSeats: " + seats + "\nTotal Price: $" + price)
                .setPositiveButton("OK", null)
                .show();
        }
    }
}
