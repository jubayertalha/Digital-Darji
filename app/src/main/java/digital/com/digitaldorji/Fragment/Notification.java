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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;

import digital.com.digitaldorji.Adapter.NotificationAdapter;
import digital.com.digitaldorji.Interface.Api;
import digital.com.digitaldorji.Model.Notifications;
import digital.com.digitaldorji.Activity.OrderActivity;
import digital.com.digitaldorji.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Notification.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Notification#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notification extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Notification() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Notification.
     */
    // TODO: Rename and change types and number of parameters
    public static Notification newInstance(String param1, String param2) {
        Notification fragment = new Notification();
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


    //.......................................................................................










    ListView lv_notification;

    NotificationAdapter adapter;

    ArrayList<Notifications> notifications;

    String email;

    Api api;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        lv_notification = view.findViewById(R.id.lv_notification);

        notifications = new ArrayList<>();

        adapter = new NotificationAdapter(getActivity(),notifications);

        lv_notification.setAdapter(adapter);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        email = sharedPreferences.getString("email", "null");

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(Api.class);

        getData();

        lv_notification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notifications notification = notifications.get(position);
                if (notification.getType().equals("ORDER")){
                    Intent intent = new Intent(getActivity(),OrderActivity.class);
                    intent.putExtra("act","ORDER");
                    startActivity(intent);
                }else if (notification.getType().equals("REQUEST")){
                    Intent intent = new Intent(getActivity(),OrderActivity.class);
                    intent.putExtra("act","REQUEST");
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    public void getData(){

        Call<ArrayList<Notifications>> call = api.getNotificationResponse("GET",email,"null","null");
        call.enqueue(new Callback<ArrayList<Notifications>>() {
            @Override
            public void onResponse(Call<ArrayList<Notifications>> call, Response<ArrayList<Notifications>> response) {
                notifications = response.body();
                Collections.reverse(notifications);
                adapter = new NotificationAdapter(getActivity(),notifications);
                lv_notification.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Notifications>> call, Throwable t) {
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







    //................................................................................................


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
