package com.example.islamdip;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private static final String SHARED_PREFS = "sharedPrefs";
    private DatabaseReference labDB;
    private LayoutInflater layoutInflater;
    private List<String> prices;
    private List<String> desc;
    private List<String> img;
    private List<String> productID;

    MainAdapter(Context context, List<String> productID, List<String> img, List<String> price, List<String> desc){
        this.layoutInflater = LayoutInflater.from(context);
        this.productID = productID;
        this.img = img;
        this.prices = price;
        this.desc = desc;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.main_card,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        // bind the textview with data received

        String price = prices.get(i);
        String description = desc.get(i);
        String imageUrl = img.get(i);
        String id = productID.get(i);

        viewHolder.cardDesc.setText(description);
        viewHolder.cardPrice.setText(price + " тг");
        viewHolder.addCartBtn.setContentDescription(id);

        Picasso.get().load(imageUrl).into(viewHolder.cardImg);

    }

    @Override
    public int getItemCount() {
        return desc.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cardImg;
        TextView cardDesc, cardPrice;
        ImageButton addCartBtn;
        ConstraintLayout mainCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            labDB = FirebaseDatabase.getInstance().getReference("Cart");

            cardImg = itemView.findViewById(R.id.cardImg);
            cardPrice = itemView.findViewById(R.id.cardPrice);
            cardDesc = itemView.findViewById(R.id.cardDesc);
            addCartBtn = itemView.findViewById(R.id.addCartBtn);

            mainCard = itemView.findViewById(R.id.mainCard);

            mainCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), DetailsActivity.class);
                    intent.putExtra("productID", addCartBtn.getContentDescription());
                    itemView.getContext().startActivity(intent);
                }
            });

            addCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(itemView.getContext(), Regpage.class);
                    SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    String nameStr = sharedPreferences.getString("name", "");

                    if (nameStr.equals("")) {
                        itemView.getContext().startActivity(intent);
                    } else {
                        CartProduct newCart = new CartProduct((String) addCartBtn.getContentDescription(), nameStr);
                        labDB.child(addCartBtn.getContentDescription().toString()).setValue(newCart);
                        Toast.makeText(itemView.getContext(), "Товар добавлен в корзину", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
