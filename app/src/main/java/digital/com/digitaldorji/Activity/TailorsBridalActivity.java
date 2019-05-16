package digital.com.digitaldorji.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import digital.com.digitaldorji.Adapter.TailorCAdapter;
import digital.com.digitaldorji.Interface.Api;
import digital.com.digitaldorji.Model.Tailor;
import digital.com.digitaldorji.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TailorsBridalActivity extends BaseActivity {
    Api api;
    ArrayList<Tailor> tailors;
    ListView lv_tailors;
    TailorCAdapter adapter;
    String category = "";
    String ct = "";
    String lo = "";



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailors_bridal);
        initToolbar();

        lv_tailors = findViewById(R.id.lv_tailors);
        tailors = new ArrayList<>();

        adapter = new TailorCAdapter(this, tailors);
        lv_tailors.setAdapter(adapter);

        category = getIntent().getStringExtra("category");
        ct = getIntent().getStringExtra("ct");
        lo = getIntent().getStringExtra("lo");

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(Api.class);

        if (category.equals("SEARCH")){
            setToolbarTitle("Search Results");
            search();
        }else {
            setToolbarTitle("" + category + " Tailors");
            getAllData();
        }

    }

    public void search(){
        Call<ArrayList<Tailor>> call = api.getSearchResponse(lo,ct);
        call.enqueue(new Callback<ArrayList<Tailor>>() {
            @Override
            public void onResponse(Call<ArrayList<Tailor>> call, Response<ArrayList<Tailor>> response) {
                tailors = response.body();
                adapter = new TailorCAdapter(TailorsBridalActivity.this, tailors);
                lv_tailors.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Tailor>> call, Throwable t) {
                AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(TailorsBridalActivity.this);
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
                Log.e("RTRO", t.getMessage() + "\n" + t.getLocalizedMessage());
            }
        });
    }

    public void getAllData() {
        Call<ArrayList<Tailor>> call = api.getTailorResponse(category,"null");
        call.enqueue(new Callback<ArrayList<Tailor>>() {
            @Override
            public void onResponse(Call<ArrayList<Tailor>> call, Response<ArrayList<Tailor>> response) {
                tailors = response.body();
                adapter = new TailorCAdapter(TailorsBridalActivity.this, tailors);
                lv_tailors.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Tailor>> call, Throwable t) {
                AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(TailorsBridalActivity.this);
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
                Log.e("RTRO", t.getMessage() + "\n" + t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}


