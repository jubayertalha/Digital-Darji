package digital.com.digitaldorji.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;

import digital.com.digitaldorji.Interface.Api;
import digital.com.digitaldorji.Model.Tailor;
import digital.com.digitaldorji.R;
import digital.com.digitaldorji.Activity.TailorPageOfUserActivity;

public class TailorCAdapter extends ArrayAdapter<Tailor> {

    ArrayList<Tailor> tailors;
    Activity context;

    public TailorCAdapter(Activity context, ArrayList<Tailor> objects) {
        super(context, R.layout.item_bridal_tailor, objects);
        this.context = context;
        this.tailors = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_bridal_tailor,null,true);

        Tailor tailor = tailors.get(position);

        LinearLayout ll_tailor = view.findViewById(R.id.ll_tailor);
        ImageView iv_tailor_dp = view.findViewById(R.id.iv_tailor_dp);
        TextView tv_tailor_name = view.findViewById(R.id.tv_tailor_name);
        TextView tv_tailor_email = view.findViewById(R.id.tv_tailor_email);
        TextView tv_tailor_address = view.findViewById(R.id.tv_tailor_address);
        SimpleRatingBar rating_bar = view.findViewById(R.id.rating_bar);
        TextView tv_tailor_rating = view.findViewById(R.id.tv_tailor_rating);

        String img = ""+Api.BASE_URL+tailor.getImg();
        Glide.with(context)
                .load(img)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(iv_tailor_dp);

        tv_tailor_name.setText(tailor.getName());
        tv_tailor_email.setText(tailor.getEmail());
        tv_tailor_address.setText(tailor.getAddress());
        String rating = ""+tailor.getTotal()+" Reviews";
        tv_tailor_rating.setText(rating);

        rating_bar.setRating(tailor.getStar());
        rating_bar.setIndicator(true);

        ll_tailor.setTag(position);
        ll_tailor.setOnClickListener(onClickListener);

        return view;
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int)v.getTag();
            Tailor item = tailors.get(position);
            Intent intent = new Intent(context,TailorPageOfUserActivity.class);
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
            context.startActivity(intent);        }
    };
}
