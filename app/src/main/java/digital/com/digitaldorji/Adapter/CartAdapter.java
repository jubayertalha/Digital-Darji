package digital.com.digitaldorji.Adapter;

import android.app.Activity;
import android.content.Context;
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
import java.util.List;

import digital.com.digitaldorji.Interface.Api;
import digital.com.digitaldorji.Model.Carts;
import digital.com.digitaldorji.R;

public class CartAdapter extends ArrayAdapter<Carts> {

    Activity context;
    ArrayList<Carts> carts;

    public CartAdapter(Activity context, ArrayList<Carts> objects) {
        super(context, R.layout.item_cart, objects);
        this.context = context;
        this.carts = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = context.getLayoutInflater().inflate(R.layout.item_cart,null,true);

        ImageView iv_cart = view.findViewById(R.id.iv_cart);
        TextView tv_cart_name = view.findViewById(R.id.tv_cart_name);
        TextView tv_cart_status = view.findViewById(R.id.tv_cart_status);
        TextView tv_cart_price = view.findViewById(R.id.tv_cart_price);
        TextView tv_cart_count = view.findViewById(R.id.tv_cart_count);

        Carts cart = carts.get(position);

        String img = ""+Api.BASE_URL+cart.getImg();
        Glide.with(context)
                .load(img)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(iv_cart);

        tv_cart_name.setText(cart.getName());

        if (cart.getType().equals("PORTFOLIO")){
            tv_cart_status.setText(" ");
            tv_cart_price.setText(" ");
            tv_cart_count.setText(" ");
        }else {
            tv_cart_status.setText(""+cart.getSize()+" . "+cart.getColor());
            tv_cart_price.setText("TK "+cart.getPrice()*cart.getCount());
            tv_cart_count.setText(""+cart.getCount()+" Units");
        }

        return view;
    }

    public Carts remove(int position){
        Carts cart = carts.get(position);
        carts.remove(position);
        notifyDataSetChanged();
        return cart;
    }

    public Carts getCart(int position){
        Carts cart = carts.get(position);
        return cart;
    }

    public ArrayList<Carts> getCarts(){
        return carts;
    }

}
