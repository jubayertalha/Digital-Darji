package com.appincubator.digitaldarji.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import com.appincubator.digitaldarji.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String email = sharedPreferences.getString("email","null");
        if (!email.equals("null")){
            startActivity(new Intent(this,HomeActivity.class));
            finish();
        }

    }


    @OnClick(R.id.sign_up_user)
    public void signUpUser() {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        intent.putExtra("type","USER");
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.sign_up_tailor)
    public void signUpTailor() {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        intent.putExtra("type","TAILOR");
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.sign_in_tailor)
    public void signInTailor() {
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        intent.putExtra("type","TAILOR");
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.sign_in_user)
    public void signInUser() {
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        intent.putExtra("type","USER");
        startActivity(intent);
        finish();
    }

}
