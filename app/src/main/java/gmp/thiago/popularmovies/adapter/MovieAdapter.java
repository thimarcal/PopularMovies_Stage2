package gmp.thiago.popularmovies.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import gmp.thiago.popularmovies.R;
import gmp.thiago.popularmovies.data.MovieJson;

/**
 * Created by thiagom on 9/28/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE = "w185/";
    private ArrayList<MovieJson.Movie> mMovies = new ArrayList<>();
    private Context mContext;

    private MovieClickListener mMovieClickListener;

    public MovieAdapter(Context context, MovieClickListener listener) {
        mContext = context;
        mMovieClickListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View movieView = inflater.inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        String posterPath = mMovies.get(position).getPoster_path();
        posterPath = IMAGE_BASE_URL+IMAGE_SIZE+posterPath;
        Uri uri = Uri.parse(posterPath);

        Picasso.with(mContext)
                            .load(uri)
                            .placeholder(R.drawable.ic_image)
                            .into(holder.moviePoster);
        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void setMovies(List movies) {
        mMovies = new ArrayList<>(movies);
        notifyDataSetChanged();
    }

    public ArrayList getMovies() {
        return mMovies;
    }

    public interface MovieClickListener {
        public void onMovieClicked(MovieJson.Movie movie);
    }

    /**
     * View Holder Class.
     * By implementing OnClickListener, we allow images to be clicked
     */
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView moviePoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            moviePoster = (ImageView)itemView.findViewById(R.id.imageview_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            MovieJson.Movie clickedMovie = mMovies.get(
                                    Integer.parseInt(view.getTag().toString()));

            mMovieClickListener.onMovieClicked(clickedMovie);
        }
    }
}
