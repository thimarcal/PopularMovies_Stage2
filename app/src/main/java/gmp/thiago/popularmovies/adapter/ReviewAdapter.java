package gmp.thiago.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gmp.thiago.popularmovies.R;
import gmp.thiago.popularmovies.data.ReviewJson;

/**
 * Created by thiagom on 11/14/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private ArrayList<ReviewJson.Review> mReviews;

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        ReviewJson.Review review = mReviews.get(position);
        TextView author = (TextView)holder.itemView.findViewById(R.id.author_textview);
        author.setText(review.getAuthor());

        TextView content = (TextView)holder.itemView.findViewById(R.id.review_content_textview);
        content.setText(review.getContent());

    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public void setReviews(List reviews) {
        mReviews = new ArrayList<>(reviews);
        notifyDataSetChanged();
    }

    public ArrayList getReviews() {
        return mReviews;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        public ReviewViewHolder(View itemView) {
            super(itemView);
        }
    }
}
