package com.appincubator.digitaldarji.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.util.ArrayList;
import java.util.Collections;

import com.appincubator.digitaldarji.Adapter.CartAdapter;
import com.appincubator.digitaldarji.Activity.AddCartActivity;
import com.appincubator.digitaldarji.Activity.AddCartPActivity;
import com.appincubator.digitaldarji.Interface.Api;
import com.appincubator.digitaldarji.Activity.OrderActivity;
import com.appincubator.digitaldarji.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.appincubator.digitaldarji.Model.Carts;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Cart.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Cart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cart extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Cart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Carts.
     */
    // TODO: Rename and change types and number of parameters
    public static Cart newInstance(String param1, String param2) {
        Cart fragment = new Cart();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //..........................................................................






    ArrayList<Carts> carts;

    final String action = "GET";
    String email,userName;

    Api api;

    ListView lv_cart;
    TextView tv_total_count;
    TextView tv_total_price;
    Button btn_cart;

    int total_count;
    int total_price;

    CartAdapter adapter;

    int complete = 0;
    boolean isOrderDone = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        lv_cart = view.findViewById(R.id.lv_cart);
        tv_total_count = view.findViewById(R.id.tv_total_count);
        tv_total_price = view.findViewById(R.id.tv_total_price);
        btn_cart = view.findViewById(R.id.btn_cart);

        carts = new ArrayList<>();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        email = sharedPreferences.getString("email", "null");

        userName = sharedPreferences.getString("name", "null");

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(Api.class);

        getData();

        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order();
            }
        });

        final SwipeToDismissTouchListener<ListViewAdapter> listener = new SwipeToDismissTouchListener<>(new ListViewAdapter(lv_cart), new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
            @Override
            public boolean canDismiss(int position) {
                return true;
            }

            @Override
            public void onDismiss(ListViewAdapter recyclerView, int position) {
                //carts.remove(position);
                Carts cart = adapter.remove(position);
                delete(cart);
            }
        });

        adapter = new CartAdapter(getActivity(),carts);
        lv_cart.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lv_cart.setOnTouchListener(listener);
        lv_cart.setOnScrollListener((AbsListView.OnScrollListener) listener.makeScrollListener());
        lv_cart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener.existPendingDismisses()){
                    listener.undoPendingDismiss();
                }else {
                    Carts cart = adapter.getCart(position);
                    if (cart.getType().equals("SELL")){
                        Intent intent = new Intent(getActivity(),AddCartActivity.class);
                        intent.putExtra("name",cart.getName());
                        intent.putExtra("img",cart.getImg());
                        intent.putExtra("id",cart.get_id());
                        intent.putExtra("to",cart.getId());
                        intent.putExtra("count",cart.getCount());
                        intent.putExtra("price",cart.getPrice());
                        intent.putExtra("act","EDIT");
                        intent.putExtra("size",cart.getSize());
                        intent.putExtra("color",cart.getColor());
                        intent.putExtra("selleremail",cart.getSelleremail());
                        intent.putExtra("status",cart.getStatus());
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getActivity(),AddCartPActivity.class);
                        intent.putExtra("img",cart.getImg());
                        intent.putExtra("name",cart.getName());
                        intent.putExtra("id",cart.get_id());
                        intent.putExtra("to",cart.getId());
                        intent.putExtra("act","EDIT");
                        intent.putExtra("selleremail",cart.getSelleremail());
                        intent.putExtra("status",cart.getStatus());

                        intent.putExtra("et_neck",cart.getNeckAround());
                        intent.putExtra("et_shoulder",cart.getShoulderWidth());
                        intent.putExtra("et_waist",cart.getWaistAround());
                        intent.putExtra("et_bicep",cart.getBicepAround());
                        intent.putExtra("et_sleeve",cart.getSleeveLength());
                        intent.putExtra("et_chest",cart.getChestAround());
                        intent.putExtra("et_shirt",cart.getShirtLength());
                        intent.putExtra("et_wrist",cart.getWristAround());

                        startActivity(intent);
                    }
                }
            }
        });

        return view;
    }

    public void order(){

        final ArrayList<Carts> orders = adapter.getCarts();
        final ArrayList<String> allEmails = new ArrayList<>();

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle("Progressing...");
        dialog.show();

        for(int i=0;orders.size()>i;i++){

            final Carts cart = orders.get(i);

            Call<com.appincubator.digitaldarji.Model.Response> call = api.addCartResponse("ADD_ORDER", cart.get_id(), "null", "null", 0, 0, "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null","null","Pending");
            call.enqueue(new Callback<com.appincubator.digitaldarji.Model.Response>() {
                @Override
                public void onResponse(Call<com.appincubator.digitaldarji.Model.Response> call, retrofit2.Response<com.appincubator.digitaldarji.Model.Response> response) {
                    complete++;
                    if (complete==1){
                        allEmails.add(cart.getSelleremail());
                        String msg = "Your have new order from "+userName+".";
                        Call<com.appincubator.digitaldarji.Model.Response> call1 = api.addNotificationResponse("ADD",cart.getSelleremail(),msg,"ORDER");
                        call1.enqueue(new Callback<com.appincubator.digitaldarji.Model.Response>() {
                            @Override
                            public void onResponse(Call<com.appincubator.digitaldarji.Model.Response> call, retrofit2.Response<com.appincubator.digitaldarji.Model.Response> response) {

                            }

                            @Override
                            public void onFailure(Call<com.appincubator.digitaldarji.Model.Response> call, Throwable t) {

                            }
                        });
                    }
                    for(int j=0;j<allEmails.size();j++){
                        if (!allEmails.get(j).equals(cart.getSelleremail())){
                            allEmails.add(cart.getSelleremail());
                            String msg = "Your have new order from "+userName+".";
                            Call<com.appincubator.digitaldarji.Model.Response> call1 = api.addNotificationResponse("ADD",cart.getSelleremail(),msg,"ORDER");
                            call1.enqueue(new Callback<com.appincubator.digitaldarji.Model.Response>() {
                                @Override
                                public void onResponse(Call<com.appincubator.digitaldarji.Model.Response> call, retrofit2.Response<com.appincubator.digitaldarji.Model.Response> response) {

                                }

                                @Override
                                public void onFailure(Call<com.appincubator.digitaldarji.Model.Response> call, Throwable t) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onFailure(Call<com.appincubator.digitaldarji.Model.Response> call, Throwable t) {
                    isOrderDone = false;
                }
            });

        }

        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (complete==orders.size()&&isOrderDone){
                    dialog.cancel();
                    this.cancel();
                    getData();

                    complete=0;

                    Intent intent = new Intent(getActivity(),OrderActivity.class);
                    intent.putExtra("act","REQUEST");
                    startActivity(intent);

                }else if(!isOrderDone){
                    dialog.cancel();
                    this.cancel();
                    complete=0;

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alert = builder.create();

                    alert.setMessage("Something went wrong.");
                    alert.show();
                    dialog.cancel();
                    isOrderDone=true;
                    getData();
                }
            }

            @Override
            public void onFinish() {
                if (complete<orders.size()&&isOrderDone){
                    this.start();
                }
            }
        }.start();

    }

    public void delete(Carts cart){

        setData(adapter.getCarts());

        Call<com.appincubator.digitaldarji.Model.Response> call = api.deleteCartResponse("REMOVE","null",cart.get_id(),0,"null","null","null","null","null","null","null","null","null","null","null","null","null",0,"null","null");

        call.enqueue(new Callback<com.appincubator.digitaldarji.Model.Response>() {
            @Override
            public void onResponse(Call<com.appincubator.digitaldarji.Model.Response> call, Response<com.appincubator.digitaldarji.Model.Response> response) {
                //Toast.makeText(getContext(),"Deleted",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<com.appincubator.digitaldarji.Model.Response> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    public void getData(){

        Call<ArrayList<Carts>> call = api.getCartResponse(action,email,0,0,"null","null","null","null","null","null","null","null","null","null","null","null","null",0,"null","null");
        call.enqueue(new Callback<ArrayList<Carts>>() {
            @Override
            public void onResponse(Call<ArrayList<Carts>> call, Response<ArrayList<Carts>> response) {
                carts = response.body();
                Collections.reverse(carts);
                setData(carts);
                adapter = new CartAdapter(getActivity(),carts);
                lv_cart.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Carts>> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    public void setData(ArrayList<Carts> cart){
        total_count = 0;
        total_price = 0;

        //total_count = carts.size();

        for (int i = 0; i < cart.size(); i++){
            total_price += cart.get(i).getPrice()*cart.get(i).getCount();
            total_count += cart.get(i).getCount();
            if (cart.get(i).getType().equals("PORTFOLIO")){
                total_count++;
            }
        }

        tv_total_count.setText("Total: "+total_count);
        tv_total_price.setText("TK "+total_price);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }





    //..........................................................................

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
