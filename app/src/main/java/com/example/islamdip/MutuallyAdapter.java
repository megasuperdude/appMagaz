package com.example.islamdip;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MutuallyAdapter extends RecyclerView.Adapter<MutuallyAdapter.ViewHolder> {
    private static final String SHARED_PREFS = "sharedPrefs";
    private DatabaseReference labDB;// Ссылка на базу данных Firebase
    private LayoutInflater layoutInflater;
    private List<String> prices;
    private List<String> titles;
    private List<String> ids;
    private List<String> colors;
    private List<String> sizes;
    private List<String> progressTexts;
    private List<String> img;

    public MutuallyAdapter(Context context, List<String> prices, List<String> titles, List<String> ids, List<String> colors, List<String> sizes, List<String> progressTexts, List<String> img) {
        this.layoutInflater = LayoutInflater.from(context);;
        this.prices = prices;
        this.ids = ids;
        this.titles = titles;
        this.colors = colors;
        this.sizes = sizes;
        this.progressTexts = progressTexts;
        this.img = img;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.mutually_card,viewGroup,false);
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
        String progress = progressTexts.get(i);
        String id = ids.get(i);

        viewHolder.cardTitle.setText(title);
        viewHolder.cardPrice.setText(price);
        viewHolder.cardSize.setText("Размер: " + size);
        viewHolder.cardColor.setText("Цвет: " + color);
        viewHolder.cardProgress.setText(progress + "/8");
        viewHolder.cardImg.setContentDescription(id);

        viewHolder.progressBar.setProgress(Integer.parseInt(progress));

        Picasso.get().load(imageUrl).into(viewHolder.cardImg);

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cardImg;
        TextView cardTitle, cardPrice, cardSize, cardColor, cardProgress;
        ProgressBar progressBar;
        Button progressBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            labDB = FirebaseDatabase.getInstance().getReference("Mutually");

            cardImg = itemView.findViewById(R.id.imageView);
            cardPrice = itemView.findViewById(R.id.cardPrice);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardSize = itemView.findViewById(R.id.cardSize);
            cardColor = itemView.findViewById(R.id.cardColor);
            cardProgress = itemView.findViewById(R.id.progress_text);

            progressBar = itemView.findViewById(R.id.progressBar);
            progressBtn = itemView.findViewById(R.id.progress_btn);

            progressBtn.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), Regpage.class);
                    SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    String nameStr = sharedPreferences.getString("name", "");

                    if (nameStr.equals("")) {
                        itemView.getContext().startActivity(intent);
                    }



                    String savedSubs = sharedPreferences.getString(nameStr, "");

                    if (savedSubs.contains(cardImg.getContentDescription().toString())) {
                        String productID = cardImg.getContentDescription().toString();
                        DatabaseReference hopperRef0 = labDB.child(productID);
                        Map<String, Object> hopperUpdates0 = new HashMap<>();
                        String counter = String.valueOf(progressBar.getProgress() - 1);
                        hopperUpdates0.put("productCounter", counter);
                        hopperRef0.updateChildren(hopperUpdates0);
                        notifyDataSetChanged();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(nameStr, savedSubs.replace(cardImg.getContentDescription().toString(), ""));
                        editor.apply();
                        Toast.makeText(itemView.getContext(), "Отмена участия", Toast.LENGTH_SHORT).show();

                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(nameStr, savedSubs + cardImg.getContentDescription().toString());
                        editor.apply();

                        String productID = cardImg.getContentDescription().toString();
                        DatabaseReference hopperRef0 = labDB.child(productID);
                        Map<String, Object> hopperUpdates0 = new HashMap<>();
                        String counter = String.valueOf(progressBar.getProgress() + 1);
                        hopperUpdates0.put("productCounter", counter);
                        hopperRef0.updateChildren(hopperUpdates0);
                        notifyDataSetChanged();
                        Toast.makeText(itemView.getContext(), "Вы участник", Toast.LENGTH_SHORT).show();
                    }

                    System.out.println("counter - > " + progressBar.getProgress());
                    if (progressBar.getProgress() >= 7) {
                        String productID = cardImg.getContentDescription().toString();
                        labDB = FirebaseDatabase.getInstance().getReference("Mutually");

                        labDB.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot ds: snapshot.getChildren()) {
                                    MutuallyProduct prd = ds.getValue(MutuallyProduct.class);
                                    if (productID.equalsIgnoreCase(prd.productId)) {
                                        Product newProduct = new Product(productID, prd.productPrice, prd.productTitle,  prd.productImage,prd.productDescription,
                                                prd.productSize, prd.productColor, prd.productMaterial, prd.productCategory, prd.productStatus);
                                        labDB = FirebaseDatabase.getInstance().getReference("Product");
                                        labDB.child(productID).setValue(newProduct);
                                        labDB = FirebaseDatabase.getInstance().getReference("Mutually").child(productID);
                                        labDB.removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                }
            });


        }
    }
}
