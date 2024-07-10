package com.example.islamdip;

import static android.app.ProgressDialog.show;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
 * Use the {@link mainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mainFragment extends Fragment {
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

    public mainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static mainFragment newInstance(String param1, String param2) {
        mainFragment fragment = new mainFragment();
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

    MainAdapter adapter;
    List itemsDesc = new ArrayList<String>();
    List itemsPrice = new ArrayList<String>();
    List itemsImage = new ArrayList<String>();
    List itemsProductID = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
        labDB = FirebaseDatabase.getInstance().getReference("Product");

        Spinner spinner = (Spinner) ((MainActivity) this.getActivity()).findViewById(R.id.toolbar).findViewById(R.id.spinnerCategory);
        String[] category = {"Категории", "Зима", "Весна", "Лето", "Осень"};
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, category);
        spinner.setAdapter(adapterCategory);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), category[position], Toast.LENGTH_SHORT).show();
                view.setVisibility(View.GONE);

                if (!category[position].equalsIgnoreCase("Категории")) {

                    //---Берем даны объявления --- START
                    labDB.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            itemsProductID.clear();
                            itemsImage.clear();
                            itemsPrice.clear();
                            itemsDesc.clear();

                            for(DataSnapshot ds: snapshot.getChildren()) {
                                Product prd = ds.getValue(Product.class);
                                if (prd.productCategory.equalsIgnoreCase(category[position])) {
                                    itemsProductID.add(prd.productId);
                                    itemsDesc.add(prd.productDescription);
                                    itemsPrice.add(prd.productPrice);
                                    itemsImage.add(prd.productImage);
                                }

                            }
                            //---Сообщаем адаптеру что данные изменились
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    //---Берем даны объявления --- END
                } else {

                    //---Берем даны объявления --- START
                    labDB.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            itemsProductID.clear();
                            itemsImage.clear();
                            itemsPrice.clear();
                            itemsDesc.clear();

                            for(DataSnapshot ds: snapshot.getChildren()) {
                                Product prd = ds.getValue(Product.class);
                                itemsProductID.add(prd.productId);
                                itemsDesc.add(prd.productDescription);
                                itemsPrice.add(prd.productPrice);
                                itemsImage.add(prd.productImage);
                            }
                            //---Сообщаем адаптеру что данные изменились
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    //---Берем даны объявления --- END

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //---Берем даны объявления --- START
        labDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    Product prd = ds.getValue(Product.class);
                    itemsProductID.add(prd.productId);
                    itemsDesc.add(prd.productDescription);
                    itemsPrice.add(prd.productPrice);
                    itemsImage.add(prd.productImage);
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
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        adapter = new MainAdapter(getActivity(), itemsProductID, itemsImage, itemsPrice, itemsDesc);
        recyclerView.setAdapter(adapter);

        SearchView srv = (SearchView) ((MainActivity) this.getActivity()).findViewById(R.id.toolbar).findViewById(R.id.search_bar);
        srv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.equals("")) {
                    FragmentTransaction tr = getFragmentManager().beginTransaction();
                    tr.replace(R.id.framelayout, new mainFragment());
                    tr.commit();
                }

                List itemsPriceSearch = new ArrayList<String>();
                List itemsImageSearch = new ArrayList<String>();
                List itemsProductIDSearch = new ArrayList<String>();
                List itemsDescSearch = new ArrayList<String>();
                for(Object item : itemsDesc) {
                   if (item.toString().toLowerCase().contains(newText.toLowerCase())) {
                       //itemsDesc.clear();
                       itemsDescSearch.add(item.toString());
                       itemsProductIDSearch.add(itemsProductID.get(itemsDesc.indexOf(item.toString())));
                       itemsImageSearch.add(itemsImage.get(itemsDesc.indexOf(item.toString())));
                       itemsPriceSearch.add(itemsPrice.get(itemsDesc.indexOf(item.toString())));

                       adapter = new MainAdapter(getActivity(), itemsProductIDSearch, itemsImageSearch, itemsPriceSearch, itemsDescSearch);
                       recyclerView.setAdapter(adapter);
                   }
                }

                return false;
            }


        });

        //Button test = rootview.findViewById(R.id.test);
        //test.setOnClickListener(new View.OnClickListener() {
           // @Override
            //public void onClick(View v) {
              //  String productID = getRandomNumberUsingNextInt(1000, 9999);
                //Product newProduct = new Product(productID, "", "Шорты", "https://opis-cdn.tinkoffjournal.ru/mercury/in-out-best-anime-2024.xpkywdtrtwqf..jpg?preset=image_760w_2x", "Обычные мужитские шорты",
                  //      "XXL", "red", "", "", "no");
                //labDB.child(productID).setValue(newProduct);
            //}
       //});

        return rootview;
    }
}