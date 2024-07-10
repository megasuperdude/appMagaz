package com.example.islamdip;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link cartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class cartFragment extends Fragment {
    private static final String SHARED_PREFS = "sharedPrefs";
    private DatabaseReference productDB;
    private DatabaseReference cartDB;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public cartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment cartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static cartFragment newInstance(String param1, String param2) {
        cartFragment fragment = new cartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private DatabaseReference labDB;// Ссылка на базу данных Firebase
    CartAdapter adapterCart;
    RecyclerView recyclerView;
    List itemsTitle = new ArrayList<String>();
    List itemsIds = new ArrayList<String>();
    List itemsPrice = new ArrayList<String>();
    List itemsImage = new ArrayList<String>();
    List itemsSize = new ArrayList<String>();
    List itemsColor = new ArrayList<String>();
    List itemsCart = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_cart, container, false);
        Intent intent = new Intent(rootview.getContext(), Regpage.class);
        SharedPreferences sharedPreferences = rootview.getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String nameStr = sharedPreferences.getString("name", "");

        if (nameStr.equals("")) {
            rootview.getContext().startActivity(intent);
        }

        Button payBtn = rootview.findViewById(R.id.payBtn);

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPay = new Intent(rootview.getContext(), CardPayActivity.class);
                rootview.getContext().startActivity(intentPay);
            }
        });


        cartDB = FirebaseDatabase.getInstance().getReference("Cart");

        //---Берем даны объявления --- START
        cartDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    CartProduct crt = ds.getValue(CartProduct.class);
                    if (nameStr.equals(crt.productUser)) {
                        itemsCart.add(crt.productId);
                    }
                }

                //---Берем даны объявления --- END

                productDB = FirebaseDatabase.getInstance().getReference("Product");

                recyclerView = rootview.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                //---Берем даны объявления --- START
                productDB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()) {
                            Product prd = ds.getValue(Product.class);

                            if (itemsCart.contains(prd.productId)) {
                                itemsIds.add(prd.productId);
                                itemsTitle.add(prd.productTitle);
                                itemsPrice.add(prd.productPrice);
                                itemsImage.add(prd.productImage);
                                itemsSize.add(prd.productSize);
                                itemsColor.add(prd.productColor);
                            }
                        }
                        //---Сообщаем адаптеру что данные изменились

                        adapterCart = new CartAdapter(rootview.getContext(), itemsPrice, itemsTitle, itemsColor, itemsSize, itemsImage);
                        recyclerView.setAdapter(adapterCart);
                        adapterCart.notifyDataSetChanged();

                        //создаем функцию свайпа для удаления карточки и декоратор к нему ---START
                        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                            @Override
                            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                                return false;
                            }

                            @Override
                            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                                labDB = FirebaseDatabase.getInstance().getReference("Cart").child(itemsIds.get(viewHolder.getAdapterPosition()).toString());
                                labDB.removeValue();

                                itemsCart.clear();
                                itemsTitle.clear();
                                itemsPrice.clear();
                                itemsImage.clear();
                                itemsSize.clear();
                                itemsColor.clear();
                                adapterCart.notifyDataSetChanged();

                            }

                            @Override
                            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                                // Отрисовка декоратора при свайпе карточки
                                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                                        .addBackgroundColor(ContextCompat.getColor(rootview.getContext(), R.color.hint_purple))
                                        .addActionIcon(R.drawable.baseline_delete_sweep_24)
                                        .create()
                                        .decorate();

                                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                            }

                        };

                        // Присоединение функции свайпа к RecyclerView
                        ItemTouchHelper helper = new ItemTouchHelper(callback);
                        helper.attachToRecyclerView(recyclerView);
                        //создаем функцию свайпа для удаления карточки и декоратор к нему ---END

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //---Берем даны объявления --- END

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });










        return rootview;
    }
}