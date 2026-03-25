package com.example.cinefast3;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class SnackAdapter extends BaseAdapter {
    private List<Snack> snacks;
    private LayoutInflater inflater;
    private OnSnackChangeListener listener;

    public interface OnSnackChangeListener {
        void onQuantityChanged();
    }

    public SnackAdapter(List<Snack> snacks, LayoutInflater inflater, OnSnackChangeListener listener) {
        this.snacks = snacks;
        this.inflater = inflater;
        this.listener = listener;
    }

    @Override
    public int getCount() { return snacks.size(); }
    @Override
    public Object getItem(int i) { return snacks.get(i); }
    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_snack, viewGroup, false);
        }
        Snack snack = snacks.get(i);
        
        ImageView img = view.findViewById(R.id.imgSnack);
        TextView name = view.findViewById(R.id.txtSnackName);
        TextView desc = view.findViewById(R.id.txtSnackDesc);
        TextView price = view.findViewById(R.id.txtSnackPrice);
        TextView qty = view.findViewById(R.id.txtQuantity);
        MaterialButton plus = view.findViewById(R.id.btnPlus);
        MaterialButton minus = view.findViewById(R.id.btnMinus);

        name.setText(snack.getName());
        desc.setText(snack.getDescription());
        price.setText("$" + String.format("%.2f", snack.getPrice()));
        qty.setText(String.valueOf(snack.getQuantity()));
        img.setImageResource(snack.getImageResource());
        img.setBackgroundTintList(ColorStateList.valueOf(snack.getColorRes()));

        plus.setOnClickListener(v -> {
            snack.setQuantity(snack.getQuantity() + 1);
            qty.setText(String.valueOf(snack.getQuantity()));
            listener.onQuantityChanged();
        });

        minus.setOnClickListener(v -> {
            if (snack.getQuantity() > 0) {
                snack.setQuantity(snack.getQuantity() - 1);
                qty.setText(String.valueOf(snack.getQuantity()));
                listener.onQuantityChanged();
            }
        });

        return view;
    }
}
