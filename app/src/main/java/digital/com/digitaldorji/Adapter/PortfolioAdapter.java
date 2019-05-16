package digital.com.digitaldorji.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;

import digital.com.digitaldorji.Interface.Api;
import digital.com.digitaldorji.Model.Product;
import digital.com.digitaldorji.Model.Tailor;
import digital.com.digitaldorji.R;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<Product> products = new ArrayList<>();
    PortfolioAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Product item);
    }

    public PortfolioAdapter(Context mContext,ArrayList<Product> products , PortfolioAdapter.OnItemClickListener onItemClickListener){
        this.mContext = mContext;
        this.products = products;
        this.listener = onItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_portfolio;
        ImageView iv_portfolio_dp;
        SimpleRatingBar rating_bar;
        TextView tv_portfolio_name;
        TextView tv_portfolio_category;

        public MyViewHolder(View view) {
            super(view);
            ll_portfolio = view.findViewById(R.id.ll_portfolio);
            iv_portfolio_dp = view.findViewById(R.id.iv_portfolio_dp);
            rating_bar = view.findViewById(R.id.rating_bar);
            tv_portfolio_name = view.findViewById(R.id.tv_portfolio_name);
            tv_portfolio_category = view.findViewById(R.id.tv_portfolio_category);
        }
    }

    @NonNull
    @Override
    public PortfolioAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_portfolio,viewGroup,false);
        return new PortfolioAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PortfolioAdapter.MyViewHolder myViewHolder, int i) {
        final Product product = products.get(i);
        String img = ""+Api.BASE_URL+product.getImg();
        Glide.with(mContext)
                .load(img)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(myViewHolder.iv_portfolio_dp);

        myViewHolder.tv_portfolio_name.setText(product.getName());
        myViewHolder.tv_portfolio_category.setText(product.getCategory());

        myViewHolder.rating_bar.setRating(product.getStar());
        myViewHolder.rating_bar.setIndicator(true);

        myViewHolder.ll_portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
