package com.appincubator.digitaldarji.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;

import com.appincubator.digitaldarji.Interface.Api;
import com.appincubator.digitaldarji.Model.Response;
import com.appincubator.digitaldarji.Model.Tailor;
import com.appincubator.digitaldarji.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddCartActivity extends BaseActivity {

    ImageView iv_add_cart;
    Button btn_add_cart_s, btn_add_cart_m, btn_add_cart_l, btn_add_cart_xl,
            btn_add_cart_xxl, iv_minus, iv_plus, btn_add_cart;
    RadioGroup rg_status;

    LinearLayout ll_request;
    Button btn_cart_tailor,btn_cart_review;
    String status;

    TextView tv_count, tv_add_cart_name;
    int id, count, price,to;
    String name, img, size, color, email,selleremail;
    String action = "ADD";
    final String type = "SELL";

    Api api;

    String act,userName;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);
        initToolbar();
        setToolbarTitle("Add Carts");

        iv_add_cart = findViewById(R.id.iv_add_cart);
        btn_add_cart_s = findViewById(R.id.btn_add_cart_s);
        btn_add_cart_m = findViewById(R.id.btn_add_cart_m);
        btn_add_cart_l = findViewById(R.id.btn_add_cart_l);
        btn_add_cart_xl = findViewById(R.id.btn_add_cart_xl);
        btn_add_cart_xxl = findViewById(R.id.btn_add_cart_xxl);
        rg_status = findViewById(R.id.rg_status);
        iv_minus = findViewById(R.id.iv_minus);
        iv_plus = findViewById(R.id.iv_plus);
        tv_count = findViewById(R.id.tv_count);
        tv_add_cart_name = findViewById(R.id.tv_add_cart_name);
        btn_add_cart = findViewById(R.id.btn_add_cart);
        ll_request = findViewById(R.id.ll_request);
        btn_cart_tailor = findViewById(R.id.btn_cart_tailor);
        btn_cart_review = findViewById(R.id.btn_cart_review);

        name = getIntent().getStringExtra("name");
        img = getIntent().getStringExtra("img");
        id = getIntent().getIntExtra("id", 0);
        to = getIntent().getIntExtra("to", 0);
        count = getIntent().getIntExtra("count", 1);
        price = getIntent().getIntExtra("price", 0);
        selleremail = getIntent().getStringExtra("selleremail");

        act = getIntent().getStringExtra("act");

        size = getIntent().getStringExtra("size");
        color = getIntent().getStringExtra("color");

        status = getIntent().getStringExtra("status");

        ll_request.setVisibility(View.GONE);

        if (act.equals("EDIT")){
            if(color.equals("Red")){
                rg_status.check(R.id.rb_r);
            }else if (color.equals("Yellow")){
                rg_status.check(R.id.rb_y);
            }else if (color.equals("Orange")){
                rg_status.check(R.id.rb_o);
            }else if (color.equals("Blue")){
                rg_status.check(R.id.rb_b);
            }else if (color.equals("Magenta")){
                rg_status.check(R.id.rb_m);
            }

            btn_add_cart.setText("EDIT YOUR CART");

            action = "EDIT";
        }else if (act.equals("ORDER")){
            if(color.equals("Red")){
                rg_status.check(R.id.rb_r);
            }else if (color.equals("Yellow")){
                rg_status.check(R.id.rb_y);
            }else if (color.equals("Orange")){
                rg_status.check(R.id.rb_o);
            }else if (color.equals("Blue")){
                rg_status.check(R.id.rb_b);
            }else if (color.equals("Magenta")){
                rg_status.check(R.id.rb_m);
            }

            for (int i = 0; i < rg_status.getChildCount(); i++) {
                rg_status.getChildAt(i).setEnabled(false);
            }

            btn_add_cart.setVisibility(View.GONE);
            ll_request.setVisibility(View.GONE);

        }else if (act.equals("REQUEST")){
            if(color.equals("Red")){
                rg_status.check(R.id.rb_r);
            }else if (color.equals("Yellow")){
                rg_status.check(R.id.rb_y);
            }else if (color.equals("Orange")){
                rg_status.check(R.id.rb_o);
            }else if (color.equals("Blue")){
                rg_status.check(R.id.rb_b);
            }else if (color.equals("Magenta")){
                rg_status.check(R.id.rb_m);
            }

            for (int i = 0; i < rg_status.getChildCount(); i++) {
                rg_status.getChildAt(i).setEnabled(false);
            }

            btn_add_cart.setVisibility(View.GONE);
            ll_request.setVisibility(View.VISIBLE);

            if (!status.equals("Complete")){
                btn_cart_review.setVisibility(View.GONE);
            }

        }

        tv_count.setText("" + count);

        if (count == 1) {
            iv_minus.setEnabled(false);
        }

        tv_add_cart_name.setText(name);

        String imgUrl = "" + Api.BASE_URL + img;

        Glide.with(this)
                .load(imgUrl)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(iv_add_cart);

        setSize();
        setCount();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(Api.class);

        btn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart();
            }
        });

        btn_cart_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review();
            }
        });

        btn_cart_tailor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tailor();
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        email = sharedPreferences.getString("email", "null");
        userName = sharedPreferences.getString("name", "null");

    }

    public void review(){
        Call<Response> call = api.getRatingsResponse("GET",email,to,"PRODUCT",0,0);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Response r = response.body();
                final int previous = r.getCode();

                final Dialog dialog = new Dialog(AddCartActivity.this);
                dialog.setContentView(R.layout.content_ratings);

                final SimpleRatingBar rating_bar = dialog.findViewById(R.id.rating_bar);

                rating_bar.setRating(previous);

                dialog.findViewById(R.id.cv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.findViewById(R.id.cv_submit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String a;

                        if (previous==0){
                            a = "ADD_PRODUCT";
                        }else {
                            a = "EDIT_PRODUCT";
                        }

                        final int star = (int)rating_bar.getRating();

                        Call<Response> call = api.getRatingsResponse(a,email,to,"PRODUCT",star,previous);
                        call.enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                dialog.cancel();
                                String msg = ""+userName+" gave "+star+" star on "+name+".";
                                Call<com.appincubator.digitaldarji.Model.Response> call1 = api.addNotificationResponse("ADD",selleremail,msg,"ITEM");
                                call1.enqueue(new Callback<com.appincubator.digitaldarji.Model.Response>() {
                                    @Override
                                    public void onResponse(Call<com.appincubator.digitaldarji.Model.Response> call, retrofit2.Response<Response> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<com.appincubator.digitaldarji.Model.Response> call, Throwable t) {

                                    }
                                });
                                Toast.makeText(AddCartActivity.this,"Done!",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Response> call, Throwable t) {
                                dialog.cancel();
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddCartActivity.this);
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
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddCartActivity.this);
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

    public void tailor(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Please wait...");
        dialog.show();
        Call<ArrayList<Tailor>> call = api.getTailorResponse("null",selleremail);
        call.enqueue(new Callback<ArrayList<Tailor>>() {
            @Override
            public void onResponse(Call<ArrayList<Tailor>> call, retrofit2.Response<ArrayList<Tailor>> response) {
                ArrayList<Tailor> popularTailors = response.body();
                Tailor item = popularTailors.get(0);
                Intent intent = new Intent(AddCartActivity.this,TailorPageOfUserActivity.class);
                intent.putExtra("_id",item.get_id());
                intent.putExtra("type",item.getType());
                intent.putExtra("email",item.getEmail());
                intent.putExtra("occupation",item.getOccupation());
                intent.putExtra("address",item.getAddress());
                intent.putExtra("category",item.getCategory());
                intent.putExtra("pass",item.getPass());
                intent.putExtra("name",item.getName());
                intent.putExtra("img",item.getImg());
                intent.putExtra("star",item.getStar());
                intent.putExtra("total",item.getTotal());
                intent.putExtra("star_5",item.getStar_5());
                intent.putExtra("star_4",item.getStar_4());
                intent.putExtra("star_3",item.getStar_3());
                intent.putExtra("star_2",item.getStar_2());
                intent.putExtra("star_1",item.getStar_1());
                dialog.cancel();
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ArrayList<Tailor>> call, Throwable t) {
                AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(AddCartActivity.this);
                builder.setMessage(t.getMessage());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setCancelable(true);
                alertDialog = builder.create();
                alertDialog.show();
                Log.e("RTRO",t.getMessage()+"\n"+t.getLocalizedMessage());
                dialog.cancel();
            }
        });
    }

    public void addCart() {
        getColor();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Progressing...");
        dialog.show();
        //price = price * count;
        Call<Response> call = api.addCartResponse(action, id, type, email, count, price, name, img, size, color, "null", "null", "null", "null", "null", "null", "null", "null",selleremail,"Cart");
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                //Toast.makeText(AddCartActivity.this, "Item added to cart.", Toast.LENGTH_SHORT).show();
                dialog.cancel();
                finish();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddCartActivity.this);
                builder.setCancelable(true);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert = builder.create();

                alert.setMessage("Something went wrong.\n" + t.getMessage());
                alert.show();
                dialog.cancel();
                Log.e("RTRO", t.getMessage() + "\n" + t.getLocalizedMessage());
            }
        });

    }

    public void getColor() {
        int i = rg_status.getCheckedRadioButtonId();
        switch (i) {
            case R.id.rb_r:
                color = "Red";
                break;
            case R.id.rb_y:
                color = "Yellow";
                break;
            case R.id.rb_o:
                color = "Orange";
                break;
            case R.id.rb_b:
                color = "Blue";
                break;
            case R.id.rb_m:
                color = "Magenta";
                break;
            default:
                break;
        }
    }

    public void setCount() {
        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                tv_count.setText("" + count);
                iv_minus.setEnabled(true);
            }
        });
        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                tv_count.setText("" + count);
                if (count == 1) {
                    iv_minus.setEnabled(false);
                }
            }
        });

        if (act.equals("ORDER")){
            iv_minus.setOnClickListener(null);
            iv_plus.setOnClickListener(null);
        }else if (act.equals("REQUEST")){
            iv_minus.setOnClickListener(null);
            iv_plus.setOnClickListener(null);
        }

    }

    public void setSize() {
        btn_add_cart_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                size = "Size S";
                btn_add_cart_s.setBackgroundColor(Color.parseColor("#518FFC"));
                btn_add_cart_m.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_add_cart_l.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_add_cart_xl.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_add_cart_xxl.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
        if(size.equals("Size S")){
            btn_add_cart_s.setBackgroundColor(Color.parseColor("#518FFC"));
            btn_add_cart_m.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_add_cart_l.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_add_cart_xl.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_add_cart_xxl.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        btn_add_cart_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                size = "Size M";
                btn_add_cart_s.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_add_cart_m.setBackgroundColor(Color.parseColor("#518FFC"));
                btn_add_cart_l.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_add_cart_xl.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_add_cart_xxl.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
        if(size.equals("Size M")){
            btn_add_cart_s.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_add_cart_m.setBackgroundColor(Color.parseColor("#518FFC"));
            btn_add_cart_l.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_add_cart_xl.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_add_cart_xxl.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        btn_add_cart_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                size = "Size L";
                btn_add_cart_s.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_add_cart_m.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_add_cart_l.setBackgroundColor(Color.parseColor("#518FFC"));
                btn_add_cart_xl.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_add_cart_xxl.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
        if(size.equals("Size L")){
            btn_add_cart_s.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_add_cart_m.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_add_cart_l.setBackgroundColor(Color.parseColor("#518FFC"));
            btn_add_cart_xl.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_add_cart_xxl.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        btn_add_cart_xl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                size = "Size XL";
                btn_add_cart_s.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_add_cart_m.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_add_cart_l.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_add_cart_xl.setBackgroundColor(Color.parseColor("#518FFC"));
                btn_add_cart_xxl.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
        if(size.equals("Size XL")){
            btn_add_cart_s.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_add_cart_m.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_add_cart_l.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_add_cart_xl.setBackgroundColor(Color.parseColor("#518FFC"));
            btn_add_cart_xxl.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        btn_add_cart_xxl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                size = "Size XXL";
                btn_add_cart_s.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_add_cart_m.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_add_cart_l.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_add_cart_xl.setBackgroundColor(Color.parseColor("#FFFFFF"));
                btn_add_cart_xxl.setBackgroundColor(Color.parseColor("#518FFC"));
            }
        });
        if(size.equals("Size XXL")){
            btn_add_cart_s.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_add_cart_m.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_add_cart_l.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_add_cart_xl.setBackgroundColor(Color.parseColor("#FFFFFF"));
            btn_add_cart_xxl.setBackgroundColor(Color.parseColor("#518FFC"));
        }


        if (act.equals("ORDER")){
            btn_add_cart_s.setOnClickListener(null);
            btn_add_cart_m.setOnClickListener(null);
            btn_add_cart_l.setOnClickListener(null);
            btn_add_cart_xl.setOnClickListener(null);
            btn_add_cart_xxl.setOnClickListener(null);
        }else if (act.equals("REQUEST")){
            btn_add_cart_s.setOnClickListener(null);
            btn_add_cart_m.setOnClickListener(null);
            btn_add_cart_l.setOnClickListener(null);
            btn_add_cart_xl.setOnClickListener(null);
            btn_add_cart_xxl.setOnClickListener(null);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

}