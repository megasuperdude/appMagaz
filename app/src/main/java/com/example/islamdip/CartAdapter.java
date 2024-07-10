package com.example.islamdip;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private static final String SHARED_PREFS = "sharedPrefs";
    private LayoutInflater layoutInflater;
    private List<String> prices;
    private List<String> titles;
    private List<String> colors;
    private List<String> sizes;
    private List<String> img;

    public CartAdapter(Context context, List<String> prices, List<String> titles, List<String> colors, List<String> sizes, List<String> img) {
        this.layoutInflater = LayoutInflater.from(context);
        this.prices = prices;
        this.titles = titles;
        this.colors = colors;
        this.sizes = sizes;
        this.img = img;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.cart_card,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        // bind the textview with data received

        String price = prices.get(i);
        String title = titles.get(i);
        String imageUrl = img.get(i);
        String size = sizes.get(i);
        String color = colors.get(i);

        viewHolder.cardTitle.setText(title);
        viewHolder.cardPrice.setText(price + " тг");
        viewHolder.cardSize.setText("Размер: " + size);
        viewHolder.cardColor.setText("Цвет: " + color);

        Picasso.get().load(imageUrl).into(viewHolder.cardImg);

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cardImg;
        TextView cardTitle, cardPrice, cardSize, cardColor, counter;
        ImageButton minus, plus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardImg = itemView.findViewById(R.id.imageView);
            cardPrice = itemView.findViewById(R.id.cardPrice);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardSize = itemView.findViewById(R.id.cardSize);
            cardColor = itemView.findViewById(R.id.cardColor);
            counter = itemView.findViewById(R.id.counter);

            minus = itemView.findViewById(R.id.minus);
            plus = itemView.findViewById(R.id.plus);

            minus.setOnClickListener((View v) -> {
                if (!counter.getText().toString().equals("0")) {
                    counter.setText(String.valueOf(Integer.parseInt(counter.getText().toString()) - 1));
                }
            });
            plus.setOnClickListener((View v) -> {
                counter.setText(String.valueOf(Integer.parseInt(counter.getText().toString()) + 1));
            });

        }
    }
}
