package com.appincubator.digitaldarji.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import com.appincubator.digitaldarji.Interface.Api;
import com.appincubator.digitaldarji.Model.Product;
import com.appincubator.digitaldarji.R;
import com.appincubator.digitaldarji.Activity.UploadSalesActivity;

public class ProductSAdapter extends ArrayAdapter<Product> {

    Activity context;
    ArrayList<Product> products;

    ImageView iv_delete,iv_edit;

    public ProductSAdapter(Activity context, ArrayList<Product> objects) {
        super(context, R.layout.item_sales, objects);

        this.context = context;
        this.products = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_sales,null,true);

        ImageView iv_item_pic = view.findViewById(R.id.iv_item_pic);
        TextView tv_item_name = view.findViewById(R.id.tv_item_name);
        TextView tv_item_category = view.findViewById(R.id.tv_item_category);
        TextView tv_item_price = view.findViewById(R.id.tv_item_price);
        iv_delete = view.findViewById(R.id.iv_delete);
        iv_edit = view.findViewById(R.id.iv_edit);

        Product product = products.get(position);

        String imgUrl = ""+Api.BASE_URL+product.getImg();

        Glide.with(context)
                .load(imgUrl)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(iv_item_pic);

        tv_item_name.setText(product.getName());
        tv_item_category.setText(product.getCategory());
        String price = ""+product.getPrice();
        tv_item_price.setText(price);

        iv_delete.setTag(position);
        iv_edit.setTag(position);

        iv_delete.setOnClickListener(onClickListener);
        iv_edit.setOnClickListener(onClickListener);

        return view;
    }

    public void delete(int position){

        Product product = products.get(position);

        Intent intent = new Intent(context,UploadSalesActivity.class);

        intent.putExtra("id",""+product.get_id());
        intent.putExtra("img",product.getImg());
        intent.putExtra("name",product.getName());
        intent.putExtra("category",product.getCategory());
        intent.putExtra("price",""+product.getPrice());
        intent.putExtra("todo","delete");

        context.startActivity(intent);
        context.finish();


    }

    public void edit(int position){

        Product product = products.get(position);

        Intent intent = new Intent(context,UploadSalesActivity.class);

        intent.putExtra("id",""+product.get_id());
        intent.putExtra("img",product.getImg());
        intent.putExtra("name",product.getName());
        intent.putExtra("category",product.getCategory());
        intent.putExtra("price",""+product.getPrice());
        intent.putExtra("todo","edit");

        context.startActivity(intent);
        context.finish();

    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_delete:
                    delete((int)v.getTag());
                    break;
                case R.id.iv_edit:
                    edit((int)v.getTag());
                    break;
                default:
                    break;
            }
        }
    };

}
