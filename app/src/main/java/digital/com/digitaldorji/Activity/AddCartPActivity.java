package digital.com.digitaldorji.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;

import digital.com.digitaldorji.Interface.Api;
import digital.com.digitaldorji.Model.Response;
import digital.com.digitaldorji.Model.Tailor;
import digital.com.digitaldorji.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddCartPActivity extends BaseActivity {

    ImageView iv_add_cart;
    EditText et_neck, et_shoulder, et_waist, et_bicep, et_sleeve,
            et_chest, et_shirt, et_wrist;
    Button btn_add_cart;
    String neckAround, shoulderWidth, waistAround, bicepAround,
            sleeveLength, chestAround, shirtLength, wristAround, img, name, email,selleremail;
    int id,to;

    LinearLayout ll_request;
    Button btn_cart_tailor,btn_cart_review;
    String status;

    String action = "ADD";
    final String type = "PORTFOLIO";

    String act,userName;

    Api api;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart_p);
        initToolbar();
        setToolbarTitle("Add Carts");

        iv_add_cart = findViewById(R.id.iv_add_cart);
        et_neck = findViewById(R.id.et_neck);
        et_shoulder = findViewById(R.id.et_shoulder);
        et_waist = findViewById(R.id.et_waist);
        et_bicep = findViewById(R.id.et_bicep);
        et_sleeve = findViewById(R.id.et_sleeve);
        et_chest = findViewById(R.id.et_chest);
        et_shirt = findViewById(R.id.et_shirt);
        et_wrist = findViewById(R.id.et_wrist);
        btn_add_cart = findViewById(R.id.btn_add_cart);

        ll_request = findViewById(R.id.ll_request);
        btn_cart_tailor = findViewById(R.id.btn_cart_tailor);
        btn_cart_review = findViewById(R.id.btn_cart_review);

        img = getIntent().getStringExtra("img");
        name = getIntent().getStringExtra("name");
        id = getIntent().getIntExtra("id", 0);
        to = getIntent().getIntExtra("to", 0);
        selleremail = getIntent().getStringExtra("selleremail");

        act = getIntent().getStringExtra("act");

        String imgUrl = "" + Api.BASE_URL + img;

        Glide.with(this)
                .load(imgUrl)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(iv_add_cart);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(Api.class);

        status = getIntent().getStringExtra("status");

        ll_request.setVisibility(View.GONE);

        if (act.equals("EDIT")){

            et_neck.setText(getIntent().getStringExtra("et_neck"));
            et_shoulder.setText(getIntent().getStringExtra("et_shoulder"));
            et_waist.setText(getIntent().getStringExtra("et_waist"));
            et_bicep.setText(getIntent().getStringExtra("et_bicep"));
            et_sleeve.setText(getIntent().getStringExtra("et_sleeve"));
            et_chest.setText(getIntent().getStringExtra("et_chest"));
            et_shirt.setText(getIntent().getStringExtra("et_shirt"));
            et_wrist.setText(getIntent().getStringExtra("et_wrist"));

            btn_add_cart.setText("EDIT YOUR CART");

            action = "EDIT";
        }else if (act.equals("ORDER")){
            et_neck.setText(getIntent().getStringExtra("et_neck"));
            et_shoulder.setText(getIntent().getStringExtra("et_shoulder"));
            et_waist.setText(getIntent().getStringExtra("et_waist"));
            et_bicep.setText(getIntent().getStringExtra("et_bicep"));
            et_sleeve.setText(getIntent().getStringExtra("et_sleeve"));
            et_chest.setText(getIntent().getStringExtra("et_chest"));
            et_shirt.setText(getIntent().getStringExtra("et_shirt"));
            et_wrist.setText(getIntent().getStringExtra("et_wrist"));

            et_neck.setEnabled(false);
            et_shoulder.setEnabled(false);
            et_waist.setEnabled(false);
            et_bicep.setEnabled(false);
            et_sleeve.setEnabled(false);
            et_chest.setEnabled(false);
            et_shirt.setEnabled(false);
            et_wrist.setEnabled(false);

            btn_add_cart.setVisibility(View.GONE);
            ll_request.setVisibility(View.GONE);

        }else if (act.equals("REQUEST")){
            et_neck.setText(getIntent().getStringExtra("et_neck"));
            et_shoulder.setText(getIntent().getStringExtra("et_shoulder"));
            et_waist.setText(getIntent().getStringExtra("et_waist"));
            et_bicep.setText(getIntent().getStringExtra("et_bicep"));
            et_sleeve.setText(getIntent().getStringExtra("et_sleeve"));
            et_chest.setText(getIntent().getStringExtra("et_chest"));
            et_shirt.setText(getIntent().getStringExtra("et_shirt"));
            et_wrist.setText(getIntent().getStringExtra("et_wrist"));

            et_neck.setEnabled(false);
            et_shoulder.setEnabled(false);
            et_waist.setEnabled(false);
            et_bicep.setEnabled(false);
            et_sleeve.setEnabled(false);
            et_chest.setEnabled(false);
            et_shirt.setEnabled(false);
            et_wrist.setEnabled(false);

            btn_add_cart.setVisibility(View.GONE);
            ll_request.setVisibility(View.VISIBLE);

            if (!status.equals("Complete")){
                btn_cart_review.setVisibility(View.GONE);
            }

        }

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

                final Dialog dialog = new Dialog(AddCartPActivity.this);
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
                                Call<digital.com.digitaldorji.Model.Response> call1 = api.addNotificationResponse("ADD",selleremail,msg,"ITEM");
                                call1.enqueue(new Callback<digital.com.digitaldorji.Model.Response>() {
                                    @Override
                                    public void onResponse(Call<digital.com.digitaldorji.Model.Response> call, retrofit2.Response<Response> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<digital.com.digitaldorji.Model.Response> call, Throwable t) {

                                    }
                                });
                                Toast.makeText(AddCartPActivity.this,"Done!",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Response> call, Throwable t) {
                                dialog.cancel();
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddCartPActivity.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddCartPActivity.this);
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
                Intent intent = new Intent(AddCartPActivity.this,TailorPageOfUserActivity.class);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddCartPActivity.this);
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
        if (getData()) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setCancelable(false);
            dialog.setTitle("Progressing...");
            dialog.show();
            Call<Response> call = api.addCartResponse(action, id, type, email, 0, 0, name, img, "null", "null", neckAround, shoulderWidth, waistAround, bicepAround, sleeveLength, chestAround, shirtLength, wristAround,selleremail,"Cart");
            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    //Toast.makeText(AddCartPActivity.this, "Item added to cart.", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    finish();
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddCartPActivity.this);
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
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddCartPActivity.this);
            builder.setCancelable(true);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alert = builder.create();

            alert.setMessage("Fields can't be empty.");
            alert.show();
        }

    }

    public boolean getData() {

        neckAround = et_neck.getText().toString();
        shoulderWidth = et_shoulder.getText().toString();
        waistAround = et_waist.getText().toString();
        bicepAround = et_bicep.getText().toString();
        sleeveLength = et_sleeve.getText().toString();
        chestAround = et_chest.getText().toString();
        shirtLength = et_shirt.getText().toString();
        wristAround = et_wrist.getText().toString();

        if (!neckAround.isEmpty() && !shoulderWidth.isEmpty() && !waistAround.isEmpty() && !bicepAround.isEmpty() && !sleeveLength.isEmpty() && !chestAround.isEmpty() && !shirtLength.isEmpty() && !wristAround.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
