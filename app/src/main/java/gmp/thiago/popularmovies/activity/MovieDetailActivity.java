package gmp.thiago.popularmovies.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gmp.thiago.popularmovies.R;
import gmp.thiago.popularmovies.adapter.MovieAdapter;
import gmp.thiago.popularmovies.adapter.TrailerAdapter;
import gmp.thiago.popularmovies.data.MovieJson;
import gmp.thiago.popularmovies.data.ReviewJson;
import gmp.thiago.popularmovies.data.TrailerJson;
import gmp.thiago.popularmovies.task.FetchMovieReviews;
import gmp.thiago.popularmovies.task.FetchMovieTrailers;
import gmp.thiago.popularmovies.task.TaskCompleteListener;
import gmp.thiago.popularmovies.utilities.NetworkUtils;

public class MovieDetailActivity extends AppCompatActivity
                                 implements TaskCompleteListener <String>,
                                            TrailerAdapter.TrailerClickListener {

    private ImageView movieThumbnailIv;
    private TextView movieNameTv;
    private TextView userRatingTv;
    private TextView releaseDateTv;
    private TextView overviewTv;
    private TextView reviewsTv;
    private RecyclerView mTrailersRv;
    private LinearLayoutManager layoutManager;
    private TrailerAdapter mTrailerAdapter;
    private ArrayList<ReviewJson.Review> mReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        Intent retrievedIntent = getIntent();
        MovieJson.Movie movie = null;
        if (null != retrievedIntent && retrievedIntent.hasExtra(getString(R.string.movies_key))) {
            movie = retrievedIntent.getParcelableExtra(getString(R.string.movies_key));
        }
        if (null != movie) {

            movieNameTv = (TextView)findViewById(R.id.movie_name);
            movieNameTv.setText(movie.getOriginal_title());

            userRatingTv = (TextView)findViewById(R.id.user_rating_textview);
            userRatingTv.setText(""+movie.getVote_average());

            releaseDateTv = (TextView)findViewById(R.id.release_date_textview);
            releaseDateTv.setText(movie.getRelease_date());

            overviewTv = (TextView)findViewById(R.id.movie_overview_textview);
            overviewTv.setText(movie.getOverview());

            reviewsTv = (TextView)findViewById(R.id.reviews_text_view);
            reviewsTv.setText("0");

            /**
             * Here, we'll set an onClickListener for the number. And make it look like a link
             */
            reviewsTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ReviewsActivity.class);
                    intent.putParcelableArrayListExtra(getString(R.string.reviews),
                                                        mReviews);
                    startActivity(intent);
                }
            });

            movieThumbnailIv = (ImageView) findViewById(R.id.movie_thumbnail_imageview);
            String thumbnailPath = MovieAdapter.IMAGE_BASE_URL +
                                                MovieAdapter.IMAGE_SIZE +
                                                movie.getPoster_path();
            Picasso.with(this).load(thumbnailPath).placeholder(R.drawable.ic_image).into(movieThumbnailIv);

            mTrailersRv = (RecyclerView)findViewById(R.id.trailer_list_reciclerview);
            layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            mTrailersRv.setLayoutManager(layoutManager);

            mTrailerAdapter = new TrailerAdapter(this, this);
            mTrailersRv.setAdapter(mTrailerAdapter);

            loadTrailers(movie.getId());
            loadReviews(movie.getId());
        }
    }

    public void loadTrailers(int id) {
        new FetchMovieTrailers(this, this).execute(id);
    }

    public void loadReviews(int id) {
        new FetchMovieReviews(this, this).execute(id);
    }

    @Override
    public void onTaskCompleted(String result, int type) {
        Gson gson = new Gson();
        if (MOVIE_TRAILER == type) {
            TrailerJson trailerJson = gson.fromJson(result, TrailerJson.class);

            if (null != mTrailerAdapter && null != trailerJson) {
                mTrailerAdapter.setTrailers(trailerJson.getResults());
            }
        } else if (MOVIE_REVIEW == type) {
            ReviewJson reviewJson = gson.fromJson(result, ReviewJson.class);
            mReviews = new ArrayList<>(reviewJson.getResults());
            reviewsTv.setText(""+reviewJson.getTotal_results());
        }
    }

    @Override
    public void onTrailerClicked(TrailerJson.Trailer trailer) {
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(NetworkUtils.YOUTUBE_BASE+trailer.getKey()));

        startActivity(youtubeIntent);
    }
}
