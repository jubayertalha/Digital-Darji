package digital.com.digitaldorji.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import digital.com.digitaldorji.Interface.Api;
import digital.com.digitaldorji.Model.Profile;
import digital.com.digitaldorji.Model.Response;
import digital.com.digitaldorji.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileSettingActivity extends BaseActivity {

    String name,email,occupation,address,category,img,id,type;

    ImageView camera_upload;
    EditText et_full_name,et_email,et_occupation,et_address;
    Button sign_up;
    Spinner spinner;

    String selectedImg="";

    Api api;

    ProgressDialog dialog;
    AlertDialog alert;

    ArrayAdapter<CharSequence> adapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
        initToolbar();
        setToolbarTitle("Settings");

        spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.category_arrays_profile, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

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

        et_full_name = findViewById(R.id.et_full_name);
        et_email = findViewById(R.id.et_email);
        sign_up = findViewById(R.id.sign_up);
        camera_upload = findViewById(R.id.camera_upload);
        et_occupation = findViewById(R.id.et_occupation);
        et_address = findViewById(R.id.et_address);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(Api.class);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        camera_upload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(ProfileSettingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            10);
                }else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select Image"), 10);
                }
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ProfileSettingActivity.this);

        email = sharedPreferences.getString("email","null");
        type = sharedPreferences.getString("type","null");

        getData();

    }

    public void getData(){
        Call<Profile> call = api.getProfileResponse(email,type);

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, retrofit2.Response<Profile> response) {

                Profile profile = response.body();

                name = profile.getName();
                occupation = profile.getOccupation();
                address = profile.getAddress();
                category = profile.getCategory();
                img = profile.getImg();
                id = ""+profile.getId();

                et_full_name.setText(name);
                et_email.setText(email);
                et_occupation.setText(occupation);
                et_address.setText(address);

                int position = adapter.getPosition(category);
                spinner.setSelection(position);

                String imgUrl = ""+Api.BASE_URL+img;

                Glide.with(ProfileSettingActivity.this)
                        .load(imgUrl)
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(camera_upload);

            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSettingActivity.this);
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
            }
        });
    }

    public void update(){
        Bitmap bitmap = ((BitmapDrawable) camera_upload.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        selectedImg = Base64.encodeToString(imgByte, Base64.DEFAULT);
        Log.e("IMG", selectedImg);
        name = et_full_name.getText().toString();
        email = et_email.getText().toString();
        address = et_address.getText().toString();
        occupation = et_occupation.getText().toString();
        category = spinner.getSelectedItem().toString();
        if (!address.isEmpty()&&!occupation.isEmpty()&&!name.isEmpty()&&!email.isEmpty()&&!selectedImg.isEmpty()){
            dialog.setTitle("Saving...");
            dialog.show();
            Call<Response> call = api.getRegisterResponse(id,"EDIT",name,email,occupation,address,category,"null","null",img,selectedImg,"null");
            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(ProfileSettingActivity.this,"Saved successfully.",Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        finish();
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
                    alert.setMessage("Something went wrong.\n"+t.getMessage());
                    alert.show();
                    dialog.cancel();
                    Log.e("RTRO",t.getMessage()+"\n"+t.getLocalizedMessage());
                }
            });
        }else {
            alert.setMessage("Fields can't be empty.");
            alert.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode ==10) {
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
        if (requestCode == 10 && resultCode == RESULT_OK && data != null){
            Uri path = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                camera_upload.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}