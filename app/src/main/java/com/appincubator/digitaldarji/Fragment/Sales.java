package com.appincubator.digitaldarji.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;

import com.appincubator.digitaldarji.Adapter.SalesAdapter;
import com.appincubator.digitaldarji.Activity.AddCartActivity;
import com.appincubator.digitaldarji.Interface.Api;
import com.appincubator.digitaldarji.Model.Product;
import com.appincubator.digitaldarji.R;
import com.appincubator.digitaldarji.Activity.TailorPageOfUserActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class Sales extends Fragment {


    public Sales() {
        // Required empty public constructor
    }


    RecyclerView lv_tailor;

    ArrayList<Product> products;

    SalesAdapter adapter;

    String email;

    private static String action = "GET";
    private static String type = "SELL";

    Api api;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales, container, false);

        lv_tailor = view.findViewById(R.id.lv_tailor);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        lv_tailor.setLayoutManager(layoutManager1);

        products = new ArrayList<>();

        email = TailorPageOfUserActivity.email;

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(Api.class);

        getData();

        return view;
    }

    public void getData(){
        Call<ArrayList<Product>> call = api.getProductResponse(action,email,type,"null","null","null","null","null","null");
        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                products = response.body();
                Collections.reverse(products);
                adapter = new SalesAdapter(getActivity(), products, new SalesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Product item, int count) {
                        Intent intent = new Intent(getActivity(),AddCartActivity.class);
                        intent.putExtra("name",item.getName());
                        intent.putExtra("img",item.getImg());
                        intent.putExtra("id",item.get_id());
                        intent.putExtra("to",item.get_id());
                        intent.putExtra("count",count);
                        intent.putExtra("price",item.getPrice());
                        intent.putExtra("act","ADD");
                        intent.putExtra("size","Size S");
                        intent.putExtra("color","Red");
                        intent.putExtra("selleremail",item.getEmail());
                        intent.putExtra("status","null");
                        startActivity(intent);
                    }
                });
                lv_tailor.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {

            }
        });
    }

}
