package com.appincubator.digitaldarji.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import com.appincubator.digitaldarji.Activity.AddCartActivity;
import com.appincubator.digitaldarji.Activity.AddCartPActivity;
import com.appincubator.digitaldarji.Interface.Api;
import com.appincubator.digitaldarji.Model.Carts;
import com.appincubator.digitaldarji.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderAdapter extends ArrayAdapter<Carts> {

    Activity context;
    ArrayList<Carts> carts;
    String act;

    public OrderAdapter(Activity context, ArrayList<Carts> objects,String act) {
        super(context, R.layout.item_order, objects);

        this.context = context;
        this.carts = objects;
        this.act = act;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = context.getLayoutInflater().inflate(R.layout.item_order,null,true);

        ImageView iv_order = view.findViewById(R.id.iv_order);
        TextView tv_order_name = view.findViewById(R.id.tv_order_name);
        TextView tv_order_details = view.findViewById(R.id.tv_order_details);
        TextView tv_order_status = view.findViewById(R.id.tv_oreder_status);
        LinearLayout ll_order = view.findViewById(R.id.ll_order);

        Carts cart = carts.get(position);

        String img = ""+Api.BASE_URL+cart.getImg();
        Glide.with(context)
                .load(img)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(iv_order);

        tv_order_name.setText(cart.getName());

        if (cart.getType().equals("PORTFOLIO")){
            tv_order_details.setText(" ");
        }else {
            tv_order_details.setText(""+cart.getCount()+" Units  TK "+cart.getPrice()*cart.getCount());
        }

        tv_order_status.setText(cart.getStatus());

        if (cart.getStatus().equals("Pending")){
            tv_order_status.setBackgroundColor(Color.parseColor("#808080"));
        }else if (cart.getStatus().equals("Rejected")){
            tv_order_status.setBackgroundColor(Color.parseColor("#E84040"));
        }else if (cart.getStatus().equals("Accepted")){
            tv_order_status.setBackgroundColor(Color.parseColor("#3871F6"));
        }else if (cart.getStatus().equals("Processing")){
            tv_order_status.setBackgroundColor(Color.parseColor("#C6DA1B"));
        }else if (cart.getStatus().equals("Complete")){
            tv_order_status.setBackgroundColor(Color.parseColor("#3ED552"));
        }

        if (act.equals("ORDER")){
            tv_order_status.setTag(position);
            tv_order_status.setOnClickListener(listener);
        }

        ll_order.setTag(position);
        ll_order.setOnClickListener(listener);

        return view;
    }

    public View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            final int position = (int)view.getTag();
            final Carts cart = carts.get(position);
            switch (view.getId()){
                case R.id.tv_oreder_status:

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.content_order_status);
                    final RadioGroup rg_status = dialog.findViewById(R.id.rg_status);

                    if (cart.getStatus().equals("Pending")){
                        rg_status.check(R.id.rb_pending);
                    }else if (cart.getStatus().equals("Rejected")){
                        rg_status.check(R.id.rb_rejected);
                    }else if (cart.getStatus().equals("Accepted")){
                        rg_status.check(R.id.rb_accepted);
                    }else if (cart.getStatus().equals("Processing")){
                        rg_status.check(R.id.rb_processing);
                    }else if (cart.getStatus().equals("Complete")){
                        rg_status.check(R.id.rb_complete);
                    }

                    dialog.findViewById(R.id.cv_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    dialog.findViewById(R.id.cv_submit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            RadioButton rb = dialog.findViewById(rg_status.getCheckedRadioButtonId());
                            final String status = rb.getText().toString();

                            if (status.equals("Pending")){
                                view.setBackgroundColor(Color.parseColor("#808080"));
                                v.setBackgroundColor(Color.parseColor("#808080"));
                            }else if (status.equals("Rejected")){
                                view.setBackgroundColor(Color.parseColor("#E84040"));
                                v.setBackgroundColor(Color.parseColor("#E84040"));
                            }else if (status.equals("Accepted")){
                                view.setBackgroundColor(Color.parseColor("#3871F6"));
                                v.setBackgroundColor(Color.parseColor("#3871F6"));
                            }else if (status.equals("Processing")){
                                view.setBackgroundColor(Color.parseColor("#C6DA1B"));
                                v.setBackgroundColor(Color.parseColor("#C6DA1B"));
                            }else if (status.equals("Complete")){
                                view.setBackgroundColor(Color.parseColor("#3ED552"));
                                v.setBackgroundColor(Color.parseColor("#3ED552"));
                            }

                            TextView tv = (TextView) view;

                            tv.setText(status);

                            carts.get(position).setStatus(status);

                            Gson gson = new GsonBuilder()
                                    .setLenient()
                                    .create();

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(Api.BASE_URL)
                                    .addConverterFactory(GsonConverterFactory.create(gson))
                                    .build();

                            final Api api = retrofit.create(Api.class);

                            int id = cart.get_id();

                            Call<com.appincubator.digitaldarji.Model.Response> call = api.addCartResponse("ADD_ORDER", id, "null", "null", 0, 0, "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null","null",status);
                            call.enqueue(new Callback<com.appincubator.digitaldarji.Model.Response>() {
                                @Override
                                public void onResponse(Call<com.appincubator.digitaldarji.Model.Response> call, retrofit2.Response<com.appincubator.digitaldarji.Model.Response> response) {
                                    dialog.cancel();
                                    String msg = "Your order "+cart.getName()+" is "+status+".";
                                    Call<com.appincubator.digitaldarji.Model.Response> call1 = api.addNotificationResponse("ADD",cart.getEmail(),msg,"REQUEST");
                                    call1.enqueue(new Callback<com.appincubator.digitaldarji.Model.Response>() {
                                        @Override
                                        public void onResponse(Call<com.appincubator.digitaldarji.Model.Response> call, Response<com.appincubator.digitaldarji.Model.Response> response) {

                                        }

                                        @Override
                                        public void onFailure(Call<com.appincubator.digitaldarji.Model.Response> call, Throwable t) {

                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Call<com.appincubator.digitaldarji.Model.Response> call, Throwable t) {
                                    dialog.cancel();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setCancelable(true);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                                    AlertDialog alert = builder.create();

                                    alert.setMessage("Something went wrong.\n"+t.getMessage());
                                    alert.show();
                                    Log.e("RTRO",t.getMessage()+"\n"+t.getLocalizedMessage());

                                }
                            });

                        }
                    });

                    dialog.show();

                    break;
                case R.id.ll_order:

                    if (cart.getType().equals("SELL")){
                        Intent intent = new Intent(context,AddCartActivity.class);
                        intent.putExtra("name",cart.getName());
                        intent.putExtra("img",cart.getImg());
                        intent.putExtra("id",cart.get_id());
                        intent.putExtra("to",cart.getId());
                        intent.putExtra("count",cart.getCount());
                        intent.putExtra("price",cart.getPrice());
                        intent.putExtra("act",act);
                        intent.putExtra("size",cart.getSize());
                        intent.putExtra("color",cart.getColor());
                        intent.putExtra("selleremail",cart.getSelleremail());
                        intent.putExtra("status",cart.getStatus());
                        context.startActivity(intent);
                    }else {
                        Intent intent = new Intent(context,AddCartPActivity.class);
                        intent.putExtra("img",cart.getImg());
                        intent.putExtra("name",cart.getName());
                        intent.putExtra("id",cart.get_id());
                        intent.putExtra("to",cart.getId());
                        intent.putExtra("act",act);
                        intent.putExtra("selleremail",cart.getSelleremail());
                        intent.putExtra("status",cart.getStatus());

                        intent.putExtra("et_neck",cart.getNeckAround());
                        intent.putExtra("et_shoulder",cart.getShoulderWidth());
                        intent.putExtra("et_waist",cart.getWaistAround());
                        intent.putExtra("et_bicep",cart.getBicepAround());
                        intent.putExtra("et_sleeve",cart.getSleeveLength());
                        intent.putExtra("et_chest",cart.getChestAround());
                        intent.putExtra("et_shirt",cart.getShirtLength());
                        intent.putExtra("et_wrist",cart.getWristAround());

                        context.startActivity(intent);
                    }

                    break;
                default:
                break;
            }
        }
    };

}
