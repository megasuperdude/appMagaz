package com.example.islamdip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private DatabaseReference labDB;
    String imageUrl, title, desc, price, material, size, color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        labDB = FirebaseDatabase.getInstance().getReference("Product");

        Bundle arguments = getIntent().getExtras();
        String productID = arguments.getString("productID");

        TextView titleTextV = findViewById(R.id.prodTitle);
        TextView descriptionTextV = findViewById(R.id.prodDescription);
        TextView priceTextV = findViewById(R.id.prodPrice);
        TextView materialTextV = findViewById(R.id.prodMaterial);
        TextView sizeTextV = findViewById(R.id.prodSize);
        TextView colorTextV = findViewById(R.id.prodColor);

        ImageView imageView = findViewById(R.id.imageView);


        //---Берем даны объявления --- START
        labDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    Product prd = ds.getValue(Product.class);
                    if (productID.equals(prd.productId)) {
                        imageUrl = prd.productImage;
                        title = prd.productTitle;
                        desc = prd.productDescription;
                        price = prd.productPrice;
                        material = prd.productMaterial;
                        size = prd.productSize;
                        color = prd.productColor;
                        break;
                    }
                }

                Picasso.get().load(imageUrl).into(imageView);
                titleTextV.setText(title);
                descriptionTextV.setText(desc);
                priceTextV.setText(price + " ТГ");
                materialTextV.setText(material);
                sizeTextV.setText(size);
                colorTextV.setText(color);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //---Берем даны объявления --- END
        System.out.println("2 - price - > " + price);

    }
}