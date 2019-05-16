package digital.com.digitaldorji.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import digital.com.digitaldorji.Interface.Api;
import digital.com.digitaldorji.Model.Profile;
import digital.com.digitaldorji.Activity.OrderActivity;
import digital.com.digitaldorji.Activity.PortfolioActivity;
import digital.com.digitaldorji.Activity.ProfileSettingActivity;
import digital.com.digitaldorji.R;
import digital.com.digitaldorji.Activity.SalesActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SellerProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SellerProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SellerProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerProfile newInstance(String param1, String param2) {
        SellerProfile fragment = new SellerProfile();
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

    //............................................................................................





    ImageView ivImage;
    TextView tv_name,tv_occupation,tv_email,tv_location,tv_sell,tv_portfolio;
    CardView cv_portfolio,cv_sale,cv_setting,cv_request,cv_order;

    Api api;

    Profile profile;

    String email,type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_profile, container, false);

        ivImage = view.findViewById(R.id.ivImage);
        tv_name = view.findViewById(R.id.tv_name);
        tv_occupation = view.findViewById(R.id.tv_occupation);
        tv_email = view.findViewById(R.id.tv_email);
        tv_location = view.findViewById(R.id.tv_location);
        tv_sell = view.findViewById(R.id.tv_sell);
        tv_portfolio = view.findViewById(R.id.tv_portfolio);
        cv_portfolio = view.findViewById(R.id.cv_portfolio);
        cv_sale = view.findViewById(R.id.cv_sale);
        cv_setting = view.findViewById(R.id.cv_setting);
        cv_request = view.findViewById(R.id.cv_request);
        cv_order = view.findViewById(R.id.cv_order);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        email = sharedPreferences.getString("email","null");
        type = sharedPreferences.getString("type","null");

        getData();

        cv_portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),PortfolioActivity.class));
            }
        });

        cv_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SalesActivity.class));
            }
        });

        cv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ProfileSettingActivity.class));
            }
        });

        cv_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),OrderActivity.class);
                intent.putExtra("act","REQUEST");
                startActivity(intent);
            }
        });

        cv_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),OrderActivity.class);
                intent.putExtra("act","ORDER");
                startActivity(intent);
            }
        });

        return view;
    }

    public void getData(){

        Call<Profile> call = api.getProfileResponse(email,type);

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {

                profile = response.body();

                String imgUrl = ""+Api.BASE_URL+profile.getImg();

                Glide.with(getActivity())
                        .load(imgUrl)
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(ivImage);

                tv_name.setText(profile.getName());
                tv_occupation.setText(profile.getOccupation());
                tv_email.setText(email);
                tv_location.setText(profile.getAddress());
                String sell = ""+profile.getSell();
                String portfolio = ""+profile.getPortfolio();
                tv_sell.setText(sell);
                tv_portfolio.setText(portfolio);

            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    //.......................................................................................

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
