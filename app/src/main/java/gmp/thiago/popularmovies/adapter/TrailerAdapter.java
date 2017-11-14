package gmp.thiago.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gmp.thiago.popularmovies.R;
import gmp.thiago.popularmovies.data.TrailerJson;

/**
 * Created by thiagom on 11/12/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private ArrayList<TrailerJson.Trailer> mTrailers = new ArrayList<>();
    private Context mContext;
    private TrailerClickListener mListener;

    public TrailerAdapter(Context context, TrailerClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setTrailers (List trailers) {
        mTrailers = new ArrayList(trailers);
        notifyDataSetChanged();
    }

    public List getTrailers() {
        return mTrailers;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        TextView trailerTitle = (TextView)holder.itemView.findViewById(R.id.trailer_title_textview);
        trailerTitle.setText(mContext.getString(R.string.trailer) + " " + (position+1));
        ImageView playButton = (ImageView)holder.itemView.findViewById(R.id.play_imageview);
        playButton.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TrailerViewHolder(View itemView) {
            super(itemView);
            ImageView playButton = (ImageView)itemView.findViewById(R.id.play_imageview);
            playButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            TrailerJson.Trailer trailer = mTrailers.get(
                    Integer.parseInt(view.getTag().toString()));

            mListener.onTrailerClicked(trailer);
        }
    }

    public interface TrailerClickListener {
        public void onTrailerClicked(TrailerJson.Trailer trailer);
    }
}
