package com.appincubator.digitaldarji.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.appincubator.digitaldarji.Interface.Api;
import com.appincubator.digitaldarji.Model.Response;
import com.appincubator.digitaldarji.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends BaseActivity {
    Api api;
    AlertDialog alert;
    Button sign_up;
    CheckBox checkbox1;
    EditText et_full_name, et_email, et_password, et_confirm_password, et_occupation, et_address;
    ImageView camera_upload;
    ProgressDialog dialog;
    String name, email, pass, type, imgUrl, confirmPass, occupation, address, category;
    String selectedImg = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initToolbar();
        setToolbarTitle("Sign Up");
        et_full_name = findViewById(R.id.et_full_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        sign_up = findViewById(R.id.sign_up);
        camera_upload = findViewById(R.id.camera_upload);
        checkbox1 = findViewById(R.id.checkbox1);
        et_occupation = findViewById(R.id.et_occupation);
        et_address = findViewById(R.id.et_address);

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
        category = "All";


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        camera_upload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent,1);
                if (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            10);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select Image"), 10);
                }
            }
        });

    }

    public void register() {
        if (checkbox1.isChecked()) {
            name = et_full_name.getText().toString();
            email = et_email.getText().toString();
            pass = et_password.getText().toString();
            confirmPass = et_confirm_password.getText().toString();
            address = et_address.getText().toString();
            occupation = et_occupation.getText().toString();
            imgUrl = "" + type + "/" + email + ".jpg";
            if (!address.isEmpty() && !occupation.isEmpty() && !name.isEmpty() && !email.isEmpty() && !pass.isEmpty() && !confirmPass.isEmpty() && !imgUrl.isEmpty() && !selectedImg.isEmpty()) {
                if (pass.length() >= 8) {
                    if (pass.equals(confirmPass)) {
                        dialog.setTitle("Sign up progressing...");
                        dialog.show();
                        String token = FirebaseInstanceId.getInstance().getToken();
                        Call<Response> call = api.getRegisterResponse("0", "REGISTER", name, email, occupation, address, category, pass, type, imgUrl, selectedImg,token);
                        call.enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                if (response.isSuccessful()){
                                    Response r = response.body();
                                    if (r.getCode() == 1) {
                                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("email", email);
                                        editor.putString("type", type);
                                        editor.putString("name", name);
                                        editor.commit();
                                        Toast.makeText(SignUpActivity.this, "Sign up success.", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                        finish();
                                    } else {
                                        alert.setMessage("User already exist. Please try to sign in.");
                                        alert.show();
                                        dialog.cancel();
                                    }
                                }else {
                                    if (response.code()==413){
                                        alert.setMessage("Image have to be less than 8MB.");
                                        alert.show();
                                    }else {
                                        alert.setMessage("Error: \n"+response.errorBody().toString()+"\n"+response.code()+"\n"+response.message());
                                        alert.show();
                                    }
                                    dialog.cancel();
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
                        alert.setMessage("Password didn't match.");
                        alert.show();
                    }
                } else {
                    alert.setMessage("Password must contain 8 or more characters.");
                    alert.show();
                }
            } else {
                alert.setMessage("Fields can't be empty.");
                alert.show();
            }
        } else {
            alert.setMessage("You didn't agree to term and conditions.");
            alert.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent, "Select Image"), 10);
            } else {
                Toast.makeText(this, "Enable Storage Permission.", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                Bitmap befogeBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                befogeBitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                byte[] imageInByte = stream.toByteArray();
                selectedImg = Base64.encodeToString(imageInByte, Base64.DEFAULT);
                camera_upload.setImageBitmap(befogeBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }
}
