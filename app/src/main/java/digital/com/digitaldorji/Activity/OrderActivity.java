package digital.com.digitaldorji.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;

import digital.com.digitaldorji.Adapter.OrderAdapter;
import digital.com.digitaldorji.Interface.Api;
import digital.com.digitaldorji.Model.Carts;
import digital.com.digitaldorji.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderActivity extends BaseActivity {

    String act;

    ListView lv_orders;

    ArrayList<Carts> carts;

    Api api;

    String email,action;

    OrderAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initToolbar();

        act = getIntent().getStringExtra("act");

        if (act.equals("ORDER")){
            setToolbarTitle("Your Orders");
            action = "GET_ORDER";
        }else {
            setToolbarTitle("Your Requests");
            action = "GET_REQUEST";
        }

        lv_orders = findViewById(R.id.lv_orders);

        carts = new ArrayList<>();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(OrderActivity.this);

        email = sharedPreferences.getString("email", "null");

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(Api.class);

        getData();
    }

    public void getData(){

        Call<ArrayList<Carts>> call = api.getCartResponse(action,email,0,0,"null","null","null","null","null","null","null","null","null","null","null","null","null",0,"null","null");
        call.enqueue(new Callback<ArrayList<Carts>>() {
            @Override
            public void onResponse(Call<ArrayList<Carts>> call, Response<ArrayList<Carts>> response) {
                carts = response.body();
                Collections.reverse(carts);
                adapter = new OrderAdapter(OrderActivity.this,carts,act);
                lv_orders.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Carts>> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
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

    @Override
    public void onBackPressed() {
        finish();
    }

}
