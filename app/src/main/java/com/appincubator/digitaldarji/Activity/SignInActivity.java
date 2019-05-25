package com.appincubator.digitaldarji.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.ButterKnife;
import butterknife.OnClick;
import com.appincubator.digitaldarji.Interface.Api;
import com.appincubator.digitaldarji.Model.Response;
import com.appincubator.digitaldarji.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends BaseActivity {

    private Context mContext;
    private Activity mActivity;
    private Toolbar mToolbar;

    AlertDialog alert;
    Api api;
    EditText e_mail, et_password;
    ProgressDialog dialog;
    String email, pass, type;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initVariable();
    }

    private void initVariable() {
        mContext = getApplicationContext();
        mActivity = SignInActivity.this;

        e_mail = findViewById(R.id.e_mail);
        et_password = findViewById(R.id.et_password);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert = builder.create();

        type = getIntent().getStringExtra("type");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);

    }

    private void initView() {
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        initToolbar();
        setToolbarTitle("Sign In");
    }

    @OnClick(R.id.sign_in)
    public void signIn() {

        email = e_mail.getText().toString();
        pass = et_password.getText().toString();

        if (!email.isEmpty() && !pass.isEmpty()) {
            dialog.setTitle("Sign in progressing...");
            dialog.show();
            String token = FirebaseInstanceId.getInstance().getToken();
            Call<Response> call = api.getLoginResponse(email, pass, type,token);
            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    if (response.isSuccessful()){
                        Response r = response.body();
                        if (r.getCode() == 1) {
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignInActivity.this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", email);
                            editor.putString("type", type);
                            editor.putString("name", response.body().getMessage());
                            editor.commit();
                            Toast.makeText(SignInActivity.this, "Sign in success.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            alert.setMessage("Email or Password is Incorrect. Please try again.");
                            alert.show();
                            dialog.cancel();
                        }
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
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
                public void onFailure(Call<Response> call, Throwable t) {
                    alert.setMessage("Something went wrong.\n" + t.getMessage());
                    alert.show();
                    dialog.cancel();
                    Log.e("RTRO", t.getMessage() + "\n" + t.getLocalizedMessage());
                }
            });
        } else {
            alert.setMessage("Fields can't be empty.");
            alert.show();
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }

}
