package digital.com.digitaldorji.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import digital.com.digitaldorji.Adapter.TailorPagerAdapter;
import digital.com.digitaldorji.Interface.Api;
import digital.com.digitaldorji.Model.Response;
import digital.com.digitaldorji.Model.Tailor;
import digital.com.digitaldorji.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TailorPageOfUserActivity extends AppCompatActivity {
    AppBarLayout appBarLayout;
    public static String email;
    int _id, star, star_1, star_2, star_3, star_4, star_5, total;
    ImageView iv_tailor_dp;
    LinearLayout ll_u_tailor;
    ViewPager vp_tailor;
    SimpleRatingBar rating_bar;
    String type, occupation, address, category, pass, name, img;
    TextView title, tv_tailor_name, tv_tailor_email, tv_tailor_address, tv_tailor_rating;
    Tailor tailor;
    TabLayout tl_tailor;

    String user_email,userName;

    Api api;

    RelativeLayout rl_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailor_page_of_user);
        appBarLayout = findViewById(R.id.appbar);
        initCollapsingToolbar();
        title = findViewById(R.id.toolbar_title);
        tv_tailor_name = findViewById(R.id.tv_tailor_name);
        tv_tailor_email = findViewById(R.id.tv_tailor_email);
        tv_tailor_address = findViewById(R.id.tv_tailor_address);
        tv_tailor_rating = findViewById(R.id.tv_tailor_rating);
        iv_tailor_dp = findViewById(R.id.iv_tailor_dp);
        rating_bar = findViewById(R.id.rating_bar);
        vp_tailor = findViewById(R.id.vp_tailor);
        tl_tailor = findViewById(R.id.tl_tailor);
        ll_u_tailor = findViewById(R.id.ll_u_tailor);
        rl_rating = findViewById(R.id.rl_rating);

        _id = getIntent().getIntExtra("_id", 0);
        type = getIntent().getStringExtra("type");
        email = getIntent().getStringExtra("email");
        occupation = getIntent().getStringExtra("occupation");
        address = getIntent().getStringExtra("address");
        category = getIntent().getStringExtra("category");
        pass = getIntent().getStringExtra("pass");
        name = getIntent().getStringExtra("name");
        img = getIntent().getStringExtra("img");
        star = getIntent().getIntExtra("star", 0);
        total = getIntent().getIntExtra("total", 0);
        star_1 = getIntent().getIntExtra("star_1", 0);
        star_2 = getIntent().getIntExtra("star_2", 0);
        star_3 = getIntent().getIntExtra("star_3", 0);
        star_4 = getIntent().getIntExtra("star_4", 0);
        star_5 = getIntent().getIntExtra("star_5", 0);
        tailor = new Tailor(_id, type, email, occupation, address, category, pass, name, img, star, total, star_5, star_4, star_3, star_2, star_1,"null");

        title.setText(name);

        String img = "" + Api.BASE_URL + tailor.getImg();
        Glide.with(this)
                .load(img)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(iv_tailor_dp);

        tv_tailor_name.setText(tailor.getName());
        tv_tailor_email.setText(tailor.getEmail());
        tv_tailor_address.setText(tailor.getAddress());
        String rating = "" + tailor.getTotal() + " Reviews";
        tv_tailor_rating.setText(rating);

        rating_bar.setRating(tailor.getStar());
        rating_bar.setIndicator(true);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(60, 0, 60, 0);
        ll_u_tailor.setLayoutParams(params);

        TailorPagerAdapter adapter = new TailorPagerAdapter(getSupportFragmentManager());

        vp_tailor.setAdapter(adapter);

        tl_tailor.setupWithViewPager(vp_tailor);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        user_email = sharedPreferences.getString("email", "null");
        userName = sharedPreferences.getString("name", "null");

        rl_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review();
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

    }

    public void review(){
        Call<Response> call = api.getRatingsResponse("GET",user_email,_id,"TAILOR",0,0);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Response r = response.body();
                final int previous = r.getCode();

                final Dialog dialog = new Dialog(TailorPageOfUserActivity.this);
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
                            a = "ADD_TAILOR";
                        }else {
                            a = "EDIT_TAILOR";
                        }

                        final int star = (int)rating_bar.getRating();

                        Call<Response> call = api.getRatingsResponse(a,user_email,_id,"TAILOR",star,previous);
                        call.enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                dialog.cancel();
                                String msg = ""+userName+" gave you "+star+" star rating.";
                                Call<digital.com.digitaldorji.Model.Response> call1 = api.addNotificationResponse("ADD",email,msg,"ITEM");
                                call1.enqueue(new Callback<digital.com.digitaldorji.Model.Response>() {
                                    @Override
                                    public void onResponse(Call<digital.com.digitaldorji.Model.Response> call, retrofit2.Response<Response> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<digital.com.digitaldorji.Model.Response> call, Throwable t) {

                                    }
                                });
                                Toast.makeText(TailorPageOfUserActivity.this,"Done!",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Response> call, Throwable t) {
                                dialog.cancel();
                                AlertDialog.Builder builder = new AlertDialog.Builder(TailorPageOfUserActivity.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(TailorPageOfUserActivity.this);
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

    private void initCollapsingToolbar() {

        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    //collapsingToolbar.setTitle(getString(R.string.app_name));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 0, 0);
                    ll_u_tailor.setLayoutParams(params);
                    isShow = true;
                } else if (isShow) {
                    //collapsingToolbar.setTitle(" ");
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(60, 0, 60, 0);
                    ll_u_tailor.setLayoutParams(params);
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
