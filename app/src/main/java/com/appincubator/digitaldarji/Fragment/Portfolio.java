package com.appincubator.digitaldarji.Fragment;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;

import com.appincubator.digitaldarji.Adapter.PortfolioAdapter;
import com.appincubator.digitaldarji.Activity.AddCartPActivity;
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
public class Portfolio extends Fragment {


    public Portfolio() {
        // Required empty public constructor
    }


    RecyclerView rv_tailor;
    ArrayList<Product> products;
    PortfolioAdapter adapter;

    String email;

    private static String action = "GET";
    private static String type = "PORTFOLIO";

    Api api;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);

        products = new ArrayList<>();

        rv_tailor = view.findViewById(R.id.rv_tailor);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),3);
        rv_tailor.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10), true));
        rv_tailor.setItemAnimator(new DefaultItemAnimator());
        rv_tailor.setLayoutManager(layoutManager);

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
                adapter = new PortfolioAdapter(getActivity(), products, new PortfolioAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Product item) {
                        Intent intent = new Intent(getActivity(),AddCartPActivity.class);
                        intent.putExtra("img",item.getImg());
                        intent.putExtra("name",item.getName());
                        intent.putExtra("id",item.get_id());
                        intent.putExtra("to",item.get_id());
                        intent.putExtra("act","ADD");
                        intent.putExtra("selleremail",item.getEmail());
                        intent.putExtra("status","null");
                        startActivity(intent);
                    }
                });
                rv_tailor.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {

            }
        });
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
