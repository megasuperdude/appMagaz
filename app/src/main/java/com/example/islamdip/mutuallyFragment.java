package com.example.islamdip;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mutuallyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mutuallyFragment extends Fragment {

    private DatabaseReference labDB;
    public String getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return String.valueOf(random.nextInt(max - min) + min);
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public mutuallyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mutuallyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static mutuallyFragment newInstance(String param1, String param2) {
        mutuallyFragment fragment = new mutuallyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    MutuallyAdapter adapter;
    List itemsIds = new ArrayList<String>();
    List itemsTitle = new ArrayList<String>();
    List itemsPrice = new ArrayList<String>();
    List itemsImage = new ArrayList<String>();
    List itemsSize = new ArrayList<String>();
    List itemsColor = new ArrayList<String>();
    List itemsProgress = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_mutually, container, false);
        labDB = FirebaseDatabase.getInstance().getReference("Mutually");

        //---Берем даны объявления --- START
        labDB.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                itemsIds.clear();
                itemsTitle.clear();
                itemsPrice.clear();
                itemsImage.clear();
                itemsSize.clear();
                itemsColor.clear();
                itemsProgress.clear();

                for(DataSnapshot ds: snapshot.getChildren()) {
                    MutuallyProduct prd = ds.getValue(MutuallyProduct.class);
                    itemsIds.add(prd.productId);
                    itemsTitle.add(prd.productTitle);
                    itemsPrice.add(prd.productPrice);
                    itemsImage.add(prd.productImage);
                    itemsSize.add(prd.productSize);
                    itemsColor.add(prd.productColor);
                    itemsProgress.add(prd.productCounter);
                }
                //---Сообщаем адаптеру что данные изменились
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //---Берем даны объявления --- END

        RecyclerView recyclerView = rootview.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new MutuallyAdapter(getActivity(), itemsPrice, itemsTitle, itemsIds, itemsColor, itemsSize, itemsProgress, itemsImage);
        recyclerView.setAdapter(adapter);


        return rootview;
    }
}