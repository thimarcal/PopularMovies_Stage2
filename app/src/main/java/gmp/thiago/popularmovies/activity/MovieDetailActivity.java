package gmp.thiago.popularmovies.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import gmp.thiago.popularmovies.R;
import gmp.thiago.popularmovies.adapter.MovieAdapter;
import gmp.thiago.popularmovies.adapter.TrailerAdapter;
import gmp.thiago.popularmovies.data.MovieContract;
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
    private ImageView favoriteIv;
    private TextView movieNameTv;
    private TextView userRatingTv;
    private TextView releaseDateTv;
    private TextView overviewTv;
    private TextView reviewsTv;
    private RecyclerView mTrailersRv;
    private LinearLayoutManager layoutManager;
    private TrailerAdapter mTrailerAdapter;
    private ArrayList<ReviewJson.Review> mReviews;

    private MovieJson.Movie movie;
    private String trailerInfoJson;
    private String reviewInfoJson;

    private boolean isFavorite;
    private String sortType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        Intent retrievedIntent = getIntent();

        if (null != retrievedIntent && retrievedIntent.hasExtra(getString(R.string.movies_key))) {
            movie = retrievedIntent.getParcelableExtra(getString(R.string.movies_key));
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sortType = sharedPreferences.getString(getString(R.string.sort_by_key),
                getString(R.string.popular_key));

        if (null != movie) {
            Cursor favoritedMovie = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    MovieContract.MovieEntry.MOVIE_ID + "=" + movie.getId(),
                    null,
                    null);
            if (favoritedMovie.getCount() != 0) {
                isFavorite = true;
            }

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

            favoriteIv =(ImageView)findViewById(R.id.favorite_imageview);
            if (isFavorite) {
                favoriteIv.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),
                        android.R.drawable.btn_star_big_on));
            }
            favoriteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFavorite) {
                        removeMovieFromDb();
                        favoriteIv.setImageDrawable(ContextCompat.getDrawable(
                                                            getApplicationContext(),
                                                            android.R.drawable.btn_star_big_off));
                    } else {
                        insertMovieToDb();
                        favoriteIv.setImageDrawable(ContextCompat.getDrawable(
                                getApplicationContext(),
                                android.R.drawable.btn_star_big_on));
                    }
                }
            });

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
            if (null != sortType && !sortType.equals(getString(R.string.favorite_key))) {
                String thumbnailPath = MovieAdapter.IMAGE_BASE_URL +
                        MovieAdapter.IMAGE_SIZE +
                        movie.getPoster_path();
                Picasso.with(this).load(thumbnailPath).placeholder(R.drawable.ic_image).into(movieThumbnailIv);
            } else {
                byte[] data = Base64.decode(movie.getPoster_path(), Base64.DEFAULT);
                Bitmap bm;
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inMutable = true;
                bm = BitmapFactory.decodeByteArray(data, 0, data.length, opt);

                movieThumbnailIv.setImageBitmap(bm);
            }
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

    private void insertMovieToDb() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.MOVIE_NAME, movie.getOriginal_title());
        contentValues.put(MovieContract.MovieEntry.MOVIE_ID, movie.getId());
        contentValues.put(MovieContract.MovieEntry.RELEASE_DATE, movie.getRelease_date());
        contentValues.put(MovieContract.MovieEntry.OVERVIEW, movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.USER_RATING, movie.getVote_average());
        contentValues.put(MovieContract.MovieEntry.REVIEWS_INFO, reviewInfoJson);
        contentValues.put(MovieContract.MovieEntry.TRAILERS_INFO, trailerInfoJson);

        /*
         * Read Image into a ByteArray to save in DB
         */
        Bitmap posterBitmap = ((BitmapDrawable)movieThumbnailIv.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        posterBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte []img = baos.toByteArray();
        String base64Image = Base64.encodeToString(img, Base64.DEFAULT);

        contentValues.put(MovieContract.MovieEntry.MOVIE_POSTER, base64Image);

        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
    }

    private void removeMovieFromDb() {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(movie.getOriginal_title()).build();

        getContentResolver().delete(uri, null, null);
    }

    public void loadTrailers(int id) {
        if (null != sortType && !sortType.equals(getString(R.string.favorite_key))) {
            new FetchMovieTrailers(this, this).execute(id);
        } else {
            Cursor movieInfo = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    MovieContract.MovieEntry.MOVIE_ID +"="+movie.getId(),
                    null,
                    null);

            if (movieInfo.moveToNext()) {
                Gson gson = new Gson();
                TrailerJson trailerJson = gson.fromJson(movieInfo.getString(
                        movieInfo.getColumnIndex(MovieContract.MovieEntry.TRAILERS_INFO)),
                        TrailerJson.class);

                mTrailerAdapter.setTrailers(trailerJson.getResults());
            }
        }
    }

    public void loadReviews(int id) {
        if (null != sortType && !sortType.equals(getString(R.string.favorite_key))) {
            new FetchMovieReviews(this, this).execute(id);
        } else {
            Cursor movieInfo = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    MovieContract.MovieEntry.MOVIE_ID +"="+movie.getId(),
                    null,
                    null);

            if (movieInfo.moveToNext()) {
                Gson gson = new Gson();
                reviewInfoJson = movieInfo.getString(
                        movieInfo.getColumnIndex(MovieContract.MovieEntry.REVIEWS_INFO));
                ReviewJson reviewJson = gson.fromJson(reviewInfoJson,
                        ReviewJson.class);
                mReviews = new ArrayList<>(reviewJson.getResults());
                reviewsTv.setText(""+reviewJson.getTotal_results());
            }
        }
    }

    @Override
    public void onTaskCompleted(String result, int type) {
        Gson gson = new Gson();
        if (MOVIE_TRAILER == type) {
            trailerInfoJson = result;
            TrailerJson trailerJson = gson.fromJson(result, TrailerJson.class);

            if (null != mTrailerAdapter && null != trailerJson) {
                mTrailerAdapter.setTrailers(trailerJson.getResults());
            }
        } else if (MOVIE_REVIEW == type) {
            reviewInfoJson = result;
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
