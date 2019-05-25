package com.appincubator.digitaldarji.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.appincubator.digitaldarji.Interface.Api;
import com.appincubator.digitaldarji.Model.Response;
import com.appincubator.digitaldarji.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadSalesActivity extends AppCompatActivity {
    private static String action = "ADD";
    private static String type = "SELL";
    Api api;
    AlertDialog alert;
    CardView cv_cancel_uploadSales, cv_upload_uploadSales;
    EditText et_item, et_price;
    ImageView iv_selectedImage;
    ProgressDialog dialog;
    RelativeLayout relative_layout;
    Spinner spinner;
    String email, name, category, price, img, selectedImg = "", id, todo;
    TextView toolbar_title, tv_button_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_sales);
        iv_selectedImage = findViewById(R.id.iv_selectedImage);
        et_item = findViewById(R.id.et_item);
        et_price = findViewById(R.id.et_price);
        cv_cancel_uploadSales = findViewById(R.id.cv_cancel_uploadSales);
        cv_upload_uploadSales = findViewById(R.id.cv_upload_uploadSales);
        toolbar_title = findViewById(R.id.toolbar_title);
        tv_button_title = findViewById(R.id.tv_button_title);
        spinner = findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_arrays, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        relative_layout = findViewById(R.id.relative_layout);

        relative_layout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!todo.equals("delete")) {
                    if (ContextCompat.checkSelfPermission(UploadSalesActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                10);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent, "Select Image"), 10);
                    }
                }
            }
        });

        iv_selectedImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!todo.equals("delete")) {
                    if (ContextCompat.checkSelfPermission(UploadSalesActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                10);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent, "Select Image"), 10);
                    }
                }
            }
        });

        cv_cancel_uploadSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadSalesActivity.this, SalesActivity.class));
                finish();
            }
        });

        cv_upload_uploadSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (todo.equals("edit")) {
                    update();
                } else if (todo.equals("delete")) {
                    delete();
                } else {
                    upload();
                }
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

        todo = getIntent().getStringExtra("todo");

        if (todo.equals("edit")) {
            toolbar_title.setText("Edit Sales");
            tv_button_title.setText("Edit");

            id = getIntent().getStringExtra("id");
            img = getIntent().getStringExtra("img");
            name = getIntent().getStringExtra("name");
            category = getIntent().getStringExtra("category");
            price = getIntent().getStringExtra("price");

            et_item.setText(name);
            et_price.setText(price);
            String imgUrl = "" + Api.BASE_URL + img;
            Glide.with(this)
                    .load(imgUrl)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(iv_selectedImage);
            relative_layout.setVisibility(View.INVISIBLE);

            int position = adapter.getPosition(category);
            spinner.setSelection(position);
        } else if (todo.equals("delete")) {
            toolbar_title.setText("Delete Sales");
            tv_button_title.setText("Delete");

            id = getIntent().getStringExtra("id");
            img = getIntent().getStringExtra("img");
            name = getIntent().getStringExtra("name");
            category = getIntent().getStringExtra("category");
            price = getIntent().getStringExtra("price");

            et_item.setText(name);
            et_price.setText(price);
            String imgUrl = "http://10.0.2.2/dorji/" + img;
            Glide.with(this)
                    .load(imgUrl)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(iv_selectedImage);
            relative_layout.setVisibility(View.INVISIBLE);

            int position = adapter.getPosition(category);
            spinner.setSelection(position);

            et_item.setEnabled(false);
            et_price.setEnabled(false);
            spinner.setEnabled(false);
        }

    }

    private void delete() {
        dialog.setTitle("Deleting...");
        dialog.show();
        Call<Response> call = api.deleteProductResponse(id, "REMOVE", "null", "null", "null", "null", "null", "null", "null");
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Toast.makeText(UploadSalesActivity.this, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                dialog.cancel();
                startActivity(new Intent(UploadSalesActivity.this, SalesActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                alert.setMessage("Something went wrong.\n" + t.getMessage());
                alert.show();
                dialog.cancel();
                Log.e("RTRO", t.getMessage() + "\n" + t.getLocalizedMessage());
            }
        });
    }

    private void update() {
        Bitmap bitmap = ((BitmapDrawable) iv_selectedImage.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        selectedImg = Base64.encodeToString(imgByte, Base64.DEFAULT);
        Log.e("IMG", selectedImg);
        name = et_item.getText().toString();
        price = et_price.getText().toString();
        category = spinner.getSelectedItem().toString();
        if (!selectedImg.isEmpty() && !name.isEmpty() && !price.isEmpty()) {
            dialog.setTitle("Updating...");
            dialog.show();
            Call<Response> call = api.editProductResponse(id, "EDIT", name, category, price, selectedImg, "null", "null", "null");
            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(UploadSalesActivity.this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        startActivity(new Intent(UploadSalesActivity.this, SalesActivity.class));
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

    private void upload() {
        name = et_item.getText().toString();
        price = et_price.getText().toString();
        category = spinner.getSelectedItem().toString();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        img = "" + type + "/" + timeStamp + email + ".jpg";
        if (!selectedImg.isEmpty() && !name.isEmpty() && !price.isEmpty()) {
            dialog.setTitle("Uploading...");
            dialog.show();
            Call<Response> call = api.addProductResponse(action, email, type, name, category, price, img, selectedImg, "null");
            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(UploadSalesActivity.this, "Uploaded successfully!", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        startActivity(new Intent(UploadSalesActivity.this, SalesActivity.class));
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                iv_selectedImage.setImageBitmap(bitmap);
                relative_layout.setVisibility(View.INVISIBLE);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
                byte[] imgByte = byteArrayOutputStream.toByteArray();
                selectedImg = Base64.encodeToString(imgByte, Base64.DEFAULT);
                Log.e("IMG", selectedImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UploadSalesActivity.this, SalesActivity.class));
        finish();
    }

}
