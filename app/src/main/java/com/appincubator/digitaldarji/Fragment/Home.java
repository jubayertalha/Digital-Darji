package com.appincubator.digitaldarji.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;

import com.appincubator.digitaldarji.Adapter.TailorAdapter;
import com.appincubator.digitaldarji.Interface.Api;
import com.appincubator.digitaldarji.Model.Tailor;
import com.appincubator.digitaldarji.R;
import com.appincubator.digitaldarji.Activity.TailorPageOfUserActivity;
import com.appincubator.digitaldarji.Activity.TailorsBridalActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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

    //...........................................................................................







    ArrayList<Tailor> allTailors;
    ArrayList<Tailor> popularTailors;
    TailorAdapter allAdapter;
    TailorAdapter popularAdapter;
    RecyclerView rv_all;
    RecyclerView rv_popular;

    Api api;


    AppBarLayout appBarLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        appBarLayout = view.findViewById(R.id.appbar);
        initCollapsingToolbar();


        rv_all = view.findViewById(R.id.my_recycler_view2);
        allTailors = new ArrayList<>();

        rv_popular = view.findViewById(R.id.my_recycler_view);
        popularTailors = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),3);
        rv_all.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10), true));
        rv_all.setItemAnimator(new DefaultItemAnimator());
        rv_all.setLayoutManager(layoutManager);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rv_popular.setLayoutManager(layoutManager1);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(Api.class);

        getAllData();

        getPopularData();


        ImageView party = view.findViewById(R.id.party);

        party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),TailorsBridalActivity.class);
                intent.putExtra("category","Party");
                intent.putExtra("ct","null");
                intent.putExtra("lo","null");
                getActivity().startActivity(intent);
            }
        });

        ImageView bridal = view.findViewById(R.id.bridal);

        bridal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),TailorsBridalActivity.class);
                intent.putExtra("category","Bridal");
                intent.putExtra("ct","null");
                intent.putExtra("lo","null");
                getActivity().startActivity(intent);
            }
        });

        ImageView kid = view.findViewById(R.id.kid);

        kid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),TailorsBridalActivity.class);
                intent.putExtra("category","Kid");
                intent.putExtra("ct","null");
                intent.putExtra("lo","null");
                getActivity().startActivity(intent);
            }
        });

        ImageView casual = view.findViewById(R.id.casual);

        casual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),TailorsBridalActivity.class);
                intent.putExtra("category","Casual");
                intent.putExtra("ct","null");
                intent.putExtra("lo","null");
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    public void getAllData(){
        Call<ArrayList<Tailor>> call = api.getTailorResponse("All","null");
        call.enqueue(new Callback<ArrayList<Tailor>>() {
            @Override
            public void onResponse(Call<ArrayList<Tailor>> call, Response<ArrayList<Tailor>> response) {
                allTailors = response.body();
                Collections.reverse(allTailors);
                allAdapter = new TailorAdapter(getActivity(), allTailors, new TailorAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Tailor item) {
                        Intent intent = new Intent(getActivity(),TailorPageOfUserActivity.class);
                        intent.putExtra("_id",item.get_id());
                        intent.putExtra("type",item.getType());
                        intent.putExtra("email",item.getEmail());
                        intent.putExtra("occupation",item.getOccupation());
                        intent.putExtra("address",item.getAddress());
                        intent.putExtra("category",item.getCategory());
                        intent.putExtra("pass",item.getPass());
                        intent.putExtra("name",item.getName());
                        intent.putExtra("img",item.getImg());
                        intent.putExtra("star",item.getStar());
                        intent.putExtra("total",item.getTotal());
                        intent.putExtra("star_5",item.getStar_5());
                        intent.putExtra("star_4",item.getStar_4());
                        intent.putExtra("star_3",item.getStar_3());
                        intent.putExtra("star_2",item.getStar_2());
                        intent.putExtra("star_1",item.getStar_1());
                        startActivity(intent);                    }
                });
                rv_all.setAdapter(allAdapter);
                allAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Tailor>> call, Throwable t) {
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
                Log.e("RTRO",t.getMessage()+"\n"+t.getLocalizedMessage());
            }
        });
    }

    public void getPopularData(){
        Call<ArrayList<Tailor>> call = api.getTailorResponse("Popular","null");
        call.enqueue(new Callback<ArrayList<Tailor>>() {
            @Override
            public void onResponse(Call<ArrayList<Tailor>> call, Response<ArrayList<Tailor>> response) {
                popularTailors = response.body();
                popularAdapter = new TailorAdapter(getActivity(), popularTailors, new TailorAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Tailor item) {
                        Intent intent = new Intent(getActivity(),TailorPageOfUserActivity.class);
                        intent.putExtra("_id",item.get_id());
                        intent.putExtra("type",item.getType());
                        intent.putExtra("email",item.getEmail());
                        intent.putExtra("occupation",item.getOccupation());
                        intent.putExtra("address",item.getAddress());
                        intent.putExtra("category",item.getCategory());
                        intent.putExtra("pass",item.getPass());
                        intent.putExtra("name",item.getName());
                        intent.putExtra("img",item.getImg());
                        intent.putExtra("star",item.getStar());
                        intent.putExtra("total",item.getTotal());
                        intent.putExtra("star_5",item.getStar_5());
                        intent.putExtra("star_4",item.getStar_4());
                        intent.putExtra("star_3",item.getStar_3());
                        intent.putExtra("star_2",item.getStar_2());
                        intent.putExtra("star_1",item.getStar_1());
                        startActivity(intent);
                    }
                });
                rv_popular.setAdapter(popularAdapter);
                popularAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Tailor>> call, Throwable t) {
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
                Log.e("RTRO",t.getMessage()+"\n"+t.getLocalizedMessage());
            }
        });
    }





    private void initCollapsingToolbar() {

        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    //collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    //collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }












    //.............................................................................................

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
