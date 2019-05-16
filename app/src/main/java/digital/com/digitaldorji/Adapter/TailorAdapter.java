package digital.com.digitaldorji.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import digital.com.digitaldorji.Interface.Api;
import digital.com.digitaldorji.Model.Tailor;
import digital.com.digitaldorji.R;

public class TailorAdapter extends RecyclerView.Adapter<TailorAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<Tailor> tailors = new ArrayList<>();
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Tailor item);
    }

    public TailorAdapter(Context mContext,ArrayList<Tailor> tailors , OnItemClickListener onItemClickListener){
        this.mContext = mContext;
        this.tailors = tailors;
        this.listener = onItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.iv_tailor);
        }
    }

    @NonNull
    @Override
    public TailorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_popular_tailors,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TailorAdapter.MyViewHolder myViewHolder, int i) {
        final Tailor tailor = tailors.get(i);
        String img = ""+Api.BASE_URL+tailor.getImg();
        Glide.with(mContext)
                .load(img)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(myViewHolder.thumbnail);

        myViewHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(tailor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tailors.size();
    }
}
