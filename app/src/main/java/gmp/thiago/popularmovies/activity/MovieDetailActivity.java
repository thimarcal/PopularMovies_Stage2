package gmp.thiago.popularmovies.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import gmp.thiago.popularmovies.R;
import gmp.thiago.popularmovies.adapter.MovieAdapter;
import gmp.thiago.popularmovies.data.MovieJson;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView movieThumbnailIv;
    private TextView movieNameTv;
    private TextView userRatingTv;
    private TextView releaseDateTv;
    private TextView overviewTv;

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

            movieThumbnailIv = (ImageView) findViewById(R.id.movie_thumbnail_imageview);
            String thumbnailPath = MovieAdapter.IMAGE_BASE_URL +
                                                MovieAdapter.IMAGE_SIZE +
                                                movie.getPoster_path();
            Picasso.with(this).load(thumbnailPath).placeholder(R.drawable.ic_image).into(movieThumbnailIv);
        }
    }
}
