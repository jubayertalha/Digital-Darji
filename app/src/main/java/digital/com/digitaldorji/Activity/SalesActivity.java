package digital.com.digitaldorji.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;

import digital.com.digitaldorji.Adapter.ProductSAdapter;
import digital.com.digitaldorji.Interface.Api;
import digital.com.digitaldorji.Model.Product;
import digital.com.digitaldorji.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SalesActivity extends BaseActivity {

    Api api;
    ArrayList<Product> products;
    Button btn_add_sale;
    ListView lv_sales;
    Product product;
    ProductSAdapter adapter;
    String email;

    private static String action = "GET";
    private static String type = "SELL";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        initToolbar();
        setToolbarTitle("Sales");

        lv_sales = findViewById(R.id.lv_sales);
        btn_add_sale = findViewById(R.id.btn_add_sale);

        products = new ArrayList<>();

        adapter = new ProductSAdapter(this, products);

        lv_sales.setAdapter(adapter);

        btn_add_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesActivity.this, UploadSalesActivity.class);

                intent.putExtra("id", "null");
                intent.putExtra("img", "null");
                intent.putExtra("name", "null");
                intent.putExtra("category", "null");
                intent.putExtra("price", "null");
                intent.putExtra("todo", "add");

                startActivity(intent);
                finish();
            }
        });

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(Api.class);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        email = sharedPreferences.getString("email", "null");

        getData();

    }

    public void getData() {
        Call<ArrayList<Product>> call = api.getProductResponse(action, email, type, "null", "null", "null", "null", "null", "null");
        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                if (response.isSuccessful()){
                    products = response.body();
                    Collections.reverse(products);
                    adapter = new ProductSAdapter(SalesActivity.this, products);
                    lv_sales.setAdapter(adapter);
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SalesActivity.this);
                    builder.setCancelable(true);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alert = builder.create();

                    alert.setMessage("Error: "+response.errorBody().toString());
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
