package com.example.cinefast3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    private LinearLayout btnToday, btnTomorrow;
    private View dotToday, dotTomorrow;
    private TextView txtToday, txtTomorrow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btnToday = view.findViewById(R.id.btnToday);
        btnTomorrow = view.findViewById(R.id.btnTomorrow);
        dotToday = view.findViewById(R.id.dotToday);
        dotTomorrow = view.findViewById(R.id.dotTomorrow);
        txtToday = view.findViewById(R.id.txtToday);
        txtTomorrow = view.findViewById(R.id.txtTomorrow);

        btnToday.setOnClickListener(v -> selectToday());
        btnTomorrow.setOnClickListener(v -> selectTomorrow());

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        ImageView menuMore = view.findViewById(R.id.menu_more);

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) return new NowShowingFragment();
                return new ComingSoonFragment();
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(position == 0 ? "NOW SHOWING" : "COMING SOON");
        }).attach();

        menuMore.setOnClickListener(v -> showLastBooking());

        return view;
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
