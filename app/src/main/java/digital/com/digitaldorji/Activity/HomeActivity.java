package digital.com.digitaldorji.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;

import digital.com.digitaldorji.Fragment.Cart;
import digital.com.digitaldorji.Fragment.Home;
import digital.com.digitaldorji.Fragment.Notification;
import digital.com.digitaldorji.Fragment.SellerProfile;
import digital.com.digitaldorji.Fragment.UserProfile;
import digital.com.digitaldorji.Interface.Api;
import digital.com.digitaldorji.Model.Tailor;
import digital.com.digitaldorji.R;
import digital.com.digitaldorji.Service.NotificationService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends BaseActivity implements
        Home.OnFragmentInteractionListener,
        Notification.OnFragmentInteractionListener,
        Cart.OnFragmentInteractionListener,
        SellerProfile.OnFragmentInteractionListener,
        UserProfile.OnFragmentInteractionListener {

    private TextView title;
    boolean isInHome = true;
    BottomNavigationView navigation;
    String type;

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    Home home = new Home();
                    switchFragment(home);
                    position = 1;
                    isInHome = true;
                    setDrawer(position);
                    title.setVisibility(View.GONE);
                    open_cart.setVisibility(View.VISIBLE);
                    et_search.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_notification:
                    Notification notification = new Notification();
                    switchFragment(notification);
                    position = 2;
                    isInHome = false;
                    setDrawer(position);
                    title.setVisibility(View.VISIBLE);
                    open_cart.setVisibility(View.GONE);
                    et_search.setVisibility(View.GONE);
                    title.setText("Notification");
                    return true;
                case R.id.navigation_cart:
                    Cart cart = new Cart();
                    switchFragment(cart);
                    position = 3;
                    isInHome = false;
                    setDrawer(position);
                    title.setVisibility(View.VISIBLE);
                    open_cart.setVisibility(View.GONE);
                    et_search.setVisibility(View.GONE);
                    title.setText("Carts");
                    return true;
                case R.id.navigation_profile:
                    SellerProfile sellerProfile = new SellerProfile();
                    UserProfile userProfile = new UserProfile();
                    if (type.equals("USER")) {
                        switchFragment(userProfile);
                    } else {
                        switchFragment(sellerProfile);
                    }
                    title.setVisibility(View.VISIBLE);
                    open_cart.setVisibility(View.GONE);
                    et_search.setVisibility(View.GONE);
                    title.setText("Profile");
                    position = 4;
                    setDrawer(position);
                    isInHome = false;
                    return true;
                default:
                    return false;
            }
        }
    };

    String location="",category="";
    int result = 0;

    Api api;

    RelativeLayout et_search;
    ImageView open_home,open_cart;

    DrawerLayout drawerLayout;

    TextView tv_home,tv_cart,tv_profile,tv_notification,tv_feed,tv_logout;
    ImageView iv_settings,line1,line2,line3,line4;

    int position = 1;

    final String packageName = "digital.com.digitaldorji";

    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        title = findViewById(R.id.toolbar_title);
        navigation = findViewById(R.id.navigation);
        et_search = findViewById(R.id.et_search);
        drawerLayout = findViewById(R.id.drawer_layout);
        open_home = findViewById(R.id.open_home);
        open_cart = findViewById(R.id.open_cart);

        tv_home = findViewById(R.id.tv_home);
        tv_cart = findViewById(R.id.tv_cart);
        tv_profile = findViewById(R.id.tv_profile);
        tv_notification = findViewById(R.id.tv_notification);
        tv_feed = findViewById(R.id.tv_feed);
        tv_logout = findViewById(R.id.tv_logout);
        iv_settings = findViewById(R.id.iv_settings);
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        line3 = findViewById(R.id.line3);
        line4 = findViewById(R.id.line4);

        setDrawer(1);

        tv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.setSelectedItemId(R.id.navigation_home);
                drawerLayout.closeDrawer(Gravity.END);
            }
        });

        tv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.setSelectedItemId(R.id.navigation_notification);
                drawerLayout.closeDrawer(Gravity.END);
            }
        });

        tv_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.setSelectedItemId(R.id.navigation_cart);
                drawerLayout.closeDrawer(Gravity.END);
            }
        });

        tv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.setSelectedItemId(R.id.navigation_profile);
                drawerLayout.closeDrawer(Gravity.END);
            }
        });

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_logout.setTextColor(getResources().getColor(R.color.cyanBlue));
                tv_logout.setTextColor(getResources().getColor(R.color.ashGrey));
                drawerLayout.closeDrawer(Gravity.END);
                AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Sure to logout?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email","null");
                        editor.commit();
                        dialog.cancel();
                        startActivity(new Intent(HomeActivity.this,MainActivity.class));
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setCancelable(true);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        iv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.END);
                startActivity(new Intent(HomeActivity.this,ProfileSettingActivity.class));
            }
        });

        tv_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_feed.setTextColor(getResources().getColor(R.color.cyanBlue));
                tv_feed.setTextColor(getResources().getColor(R.color.ashGrey));
                drawerLayout.closeDrawer(Gravity.END);
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                }
            }
        });

        open_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.END);
            }
        });

        open_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.setSelectedItemId(R.id.navigation_cart);
                //showNotification();
            }
        });

        //setSupportActionBar(searchBar);
        //toolbar.setVisibility(View.INVISIBLE);

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        type = sharedPreferences.getString("type", "USER");

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(Api.class);

    }

    public void showNotification(){

        int NOTIFICATION_ID = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = "digital_darji_1";
        CharSequence name = "digital_darji";
        String Description = "Digital Darji";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.setShowBadge(false);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{0,400,200,400});

            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(HomeActivity.this, CHANNEL_ID)
                .setSmallIcon(R.drawable.nnnn)
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setAutoCancel(true)
                .setContentTitle("Digital Darji")
                .setContentText("This is a test notification.");

        Intent resultIntent = new Intent(HomeActivity.this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(HomeActivity.this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

    public void setDrawer(int position){
        switch (position){
            case 1:

                tv_home.setTextColor(getResources().getColor(R.color.cyanBlue));
                tv_notification.setTextColor(getResources().getColor(R.color.ashGrey));
                tv_cart.setTextColor(getResources().getColor(R.color.ashGrey));
                tv_profile.setTextColor(getResources().getColor(R.color.ashGrey));

                line1.setVisibility(View.VISIBLE);
                line4.setVisibility(View.GONE);
                line2.setVisibility(View.GONE);
                line3.setVisibility(View.GONE);

                break;
            case 2:

                tv_home.setTextColor(getResources().getColor(R.color.ashGrey));
                tv_notification.setTextColor(getResources().getColor(R.color.cyanBlue));
                tv_cart.setTextColor(getResources().getColor(R.color.ashGrey));
                tv_profile.setTextColor(getResources().getColor(R.color.ashGrey));

                line1.setVisibility(View.GONE);
                line4.setVisibility(View.VISIBLE);
                line2.setVisibility(View.GONE);
                line3.setVisibility(View.GONE);

                break;
            case 3:

                tv_home.setTextColor(getResources().getColor(R.color.ashGrey));
                tv_notification.setTextColor(getResources().getColor(R.color.ashGrey));
                tv_cart.setTextColor(getResources().getColor(R.color.cyanBlue));
                tv_profile.setTextColor(getResources().getColor(R.color.ashGrey));

                line1.setVisibility(View.GONE);
                line4.setVisibility(View.GONE);
                line2.setVisibility(View.VISIBLE);
                line3.setVisibility(View.GONE);

                break;
            case 4:

                tv_home.setTextColor(getResources().getColor(R.color.ashGrey));
                tv_notification.setTextColor(getResources().getColor(R.color.ashGrey));
                tv_cart.setTextColor(getResources().getColor(R.color.ashGrey));
                tv_profile.setTextColor(getResources().getColor(R.color.cyanBlue));

                line1.setVisibility(View.GONE);
                line4.setVisibility(View.GONE);
                line2.setVisibility(View.GONE);
                line3.setVisibility(View.VISIBLE);

                break;
            default:
                break;
        }
    }

    public void search(){

        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.content_search);

        final EditText editText = dialog.findViewById(R.id.location);
        final Spinner spinner = dialog.findViewById(R.id.spinner);
        final TextView textView = dialog.findViewById(R.id.tv_result);
        ImageView cancel = dialog.findViewById(R.id.cancel);
        Button show = dialog.findViewById(R.id.show);
        final ProgressBar pb_search = dialog.findViewById(R.id.pb_search);

        pb_search.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(HomeActivity.this, R.array.category_arrays_profile, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location = editText.getText().toString().toLowerCase();
                category = spinner.getSelectedItem().toString();
                if (!location.isEmpty()){
                    pb_search.setVisibility(View.VISIBLE);
                    Call<ArrayList<Tailor>> call = api.getSearchResponse(location,category);
                    call.enqueue(new Callback<ArrayList<Tailor>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Tailor>> call, Response<ArrayList<Tailor>> response) {
                            ArrayList<Tailor> list = response.body();
                            result = list.size();
                            textView.setText(""+result);
                            pb_search.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Tailor>> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                location = editText.getText().toString().toLowerCase();
                category = spinner.getSelectedItem().toString();
                if (!location.isEmpty()){
                    pb_search.setVisibility(View.VISIBLE);
                    Call<ArrayList<Tailor>> call = api.getSearchResponse(location,category);
                    call.enqueue(new Callback<ArrayList<Tailor>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Tailor>> call, Response<ArrayList<Tailor>> response) {
                            ArrayList<Tailor> list = response.body();
                            result = list.size();
                            textView.setText(""+result);
                            pb_search.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Tailor>> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (result!=0){
                    location = editText.getText().toString().toLowerCase();
                    category = spinner.getSelectedItem().toString();
                    Intent intent = new Intent(HomeActivity.this,TailorsBridalActivity.class);
                    intent.putExtra("category","SEARCH");
                    intent.putExtra("ct",category);
                    intent.putExtra("lo",location);
                    startActivity(intent);
                    dialog.cancel();
                }
            }
        });

        dialog.show();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void switchFragment(Fragment baseFragment) {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            if (getSupportFragmentManager().findFragmentById(R.id.home_fragment) == null) {
                ft.add(R.id.home_fragment, baseFragment);
            } else {
                ft.replace(R.id.home_fragment, baseFragment);
            }
            ft.addToBackStack(null);
            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isInHome == false)
            navigation.setSelectedItemId(R.id.navigation_home);
        else
            finish();

    }
}