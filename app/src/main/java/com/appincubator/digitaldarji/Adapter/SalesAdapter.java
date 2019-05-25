package com.appincubator.digitaldarji.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;

import com.appincubator.digitaldarji.Interface.Api;
import com.appincubator.digitaldarji.Model.Product;
import com.appincubator.digitaldarji.R;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.MyViewHolder> {

    ArrayList<Product> products = new ArrayList<>();
    Activity context;
    SalesAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Product item,int count);
    }

    public SalesAdapter(Activity context, ArrayList<Product> objects, SalesAdapter.OnItemClickListener onItemClickListener) {
        this.products = objects;
        this.context = context;
        this.listener = onItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_sales_dp;
        TextView tv_sales_name;
        SimpleRatingBar rating_bar;
        TextView tv_sales_price;
        LinearLayout ll_sales_count;
        TextView tv_sales_count;
        TextView tv_sales_cart;

        public MyViewHolder(View view) {
            super(view);
            iv_sales_dp = view.findViewById(R.id.iv_sales_dp);
            tv_sales_name = view.findViewById(R.id.tv_sales_name);
            rating_bar = view.findViewById(R.id.rating_bar);
            tv_sales_price = view.findViewById(R.id.tv_sales_price);
            ll_sales_count = view.findViewById(R.id.ll_sales_count);
            tv_sales_count = view.findViewById(R.id.tv_sales_count);
            tv_sales_cart = view.findViewById(R.id.tv_sales_cart);
        }
    }

    @NonNull
    @Override
    public SalesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_sales,viewGroup,false);
        return new SalesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final MyViewHolder holder = myViewHolder;
        final Product product = products.get(i);
        String imgUrl = ""+Api.BASE_URL+product.getImg();

        Glide.with(context)
                .load(imgUrl)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(myViewHolder.iv_sales_dp);

        myViewHolder.rating_bar.setRating(product.getStar());
        myViewHolder.rating_bar.setIndicator(true);

        myViewHolder.tv_sales_name.setText(product.getName());
        myViewHolder.tv_sales_price.setText("TK "+product.getPrice());

        myViewHolder.tv_sales_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(holder.tv_sales_count.getText().toString());
                listener.onItemClick(product,count);
            }
        });

        myViewHolder.ll_sales_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(holder.tv_sales_count.getText().toString());
                count++;
                holder.tv_sales_count.setText(""+count);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

}
