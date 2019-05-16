package digital.com.digitaldorji.Interface;

import java.util.ArrayList;

import digital.com.digitaldorji.Model.Carts;
import digital.com.digitaldorji.Model.Notifications;
import digital.com.digitaldorji.Model.Product;
import digital.com.digitaldorji.Model.Profile;
import digital.com.digitaldorji.Model.Response;
import digital.com.digitaldorji.Model.Tailor;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    String BASE_URL = "http://digitaldarji.net/darji/";

    @FormUrlEncoded
    @POST("register.php")
    Call<Response> getRegisterResponse(@Field("_id") String id,@Field("action") String action,@Field("name") String name, @Field("email") String email,@Field("occupation") String occupation,@Field("address") String address,@Field("category") String category, @Field("pass") String pass, @Field("type") String type, @Field("img") String img, @Field("selectedImg") String selectedImg, @Field("token") String token);

    @FormUrlEncoded
    @POST("login.php")
    Call<Response> getLoginResponse(@Field("email") String email, @Field("pass") String pass, @Field("type") String type, @Field("token") String token);

    @FormUrlEncoded
    @POST("profile.php")
    Call<Profile> getProfileResponse(@Field("email") String email, @Field("type") String type);

    @FormUrlEncoded
    @POST("product.php")
    Call<ArrayList<Product>> getProductResponse(@Field("action") String action, @Field("email") String email, @Field("type") String type,@Field("_id") String id,@Field("name") String name,@Field("category") String category,@Field("price") String price,@Field("img") String img,@Field("selectedImg") String selectedImg);

    @FormUrlEncoded
    @POST("product.php")
    Call<Response> addProductResponse(@Field("action") String action,@Field("email") String email, @Field("type") String type,@Field("name") String name,@Field("category") String category,@Field("price") String price,@Field("img") String img,@Field("selectedImg") String selectedImg,@Field("_id") String id);

    @FormUrlEncoded
    @POST("product.php")
    Call<Response> deleteProductResponse(@Field("_id") String id,@Field("action") String action,@Field("email") String email, @Field("type") String type,@Field("name") String name,@Field("category") String category,@Field("price") String price,@Field("img") String img,@Field("selectedImg") String selectedImg);

    @FormUrlEncoded
    @POST("product.php")
    Call<Response> editProductResponse(@Field("_id") String id,@Field("action") String action,@Field("name") String name,@Field("category") String category,@Field("price") String price,@Field("selectedImg") String selectedImg,@Field("email") String email, @Field("type") String type,@Field("img") String img);

    @FormUrlEncoded
    @POST("tailor.php")
    Call<ArrayList<Tailor>> getTailorResponse(@Field("category") String category,@Field("email") String email);

    @FormUrlEncoded
    @POST("cart.php")
    Call<Response> addCartResponse(@Field("action") String action,@Field("id") int id, @Field("type") String type,@Field("email") String email,@Field("count") int count,@Field("price") int price,@Field("name") String name,@Field("img") String img,@Field("size") String size,@Field("color") String color,@Field("neckAround") String neckAround,@Field("shoulderWidth") String shoulderWidth,@Field("waistAround") String waistAround,@Field("bicepAround") String bicepAround,@Field("sleeveLength") String sleeveLength,@Field("chestAround") String chestAround,@Field("shirtLength") String shirtLength,@Field("wristAround") String wristAround, @Field("selleremail") String selleremail,@Field("status") String status);

    @FormUrlEncoded
    @POST("cart.php")
    Call<ArrayList<Carts>> getCartResponse(@Field("action") String action, @Field("email") String email, @Field("count") int count, @Field("price") int price, @Field("name") String name, @Field("img") String img, @Field("size") String size, @Field("color") String color, @Field("neckAround") String neckAround, @Field("shoulderWidth") String shoulderWidth, @Field("waistAround") String waistAround, @Field("bicepAround") String bicepAround, @Field("sleeveLength") String sleeveLength, @Field("chestAround") String chestAround, @Field("shirtLength") String shirtLength, @Field("wristAround") String wristAround, @Field("type") String type, @Field("id") int id, @Field("selleremail") String selleremail,@Field("status") String status);

    @FormUrlEncoded
    @POST("cart.php")
    Call<Response> deleteCartResponse(@Field("action") String action, @Field("email") String email, @Field("count") int count, @Field("price") int price, @Field("name") String name, @Field("img") String img, @Field("size") String size, @Field("color") String color, @Field("neckAround") String neckAround, @Field("shoulderWidth") String shoulderWidth, @Field("waistAround") String waistAround, @Field("bicepAround") String bicepAround, @Field("sleeveLength") String sleeveLength, @Field("chestAround") String chestAround, @Field("shirtLength") String shirtLength, @Field("wristAround") String wristAround, @Field("type") String type, @Field("id") int id ,@Field("selleremail") String selleremail,@Field("status") String status);

    @FormUrlEncoded
    @POST("ratings.php")
    Call<Response> getRatingsResponse(@Field("action") String action, @Field("from") String from, @Field("to") int to, @Field("type") String type, @Field("star") int star, @Field("previous") int previous);

    @FormUrlEncoded
    @POST("notification.php")
    Call<ArrayList<Notifications>> getNotificationResponse(@Field("action") String action, @Field("email") String email, @Field("msg") String msg, @Field("type") String type);

    @FormUrlEncoded
    @POST("notification.php")
    Call<Response> addNotificationResponse(@Field("action") String action, @Field("email") String email, @Field("msg") String msg, @Field("type") String type);

    @FormUrlEncoded
    @POST("search.php")
    Call<ArrayList<Tailor>> getSearchResponse(@Field("location") String location, @Field("category") String category);

}
