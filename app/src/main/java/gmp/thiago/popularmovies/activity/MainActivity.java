package gmp.thiago.popularmovies.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import gmp.thiago.popularmovies.R;
import gmp.thiago.popularmovies.adapter.MovieAdapter;
import gmp.thiago.popularmovies.data.MovieContract;
import gmp.thiago.popularmovies.data.MovieJson;
import gmp.thiago.popularmovies.task.FetchMoviesData;
import gmp.thiago.popularmovies.task.TaskCompleteListener;
import gmp.thiago.popularmovies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity
                                         implements MovieAdapter.MovieClickListener,
                                            SharedPreferences.OnSharedPreferenceChangeListener,
                                            TaskCompleteListener <String> {
    private RecyclerView mMoviesRV;
    private TextView disconnectedTextView;
    private GridLayoutManager layoutManager;
    private MovieAdapter mMovieAdapter;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Retrieve the recyclerview in which we'l hold our movies. With that, we can set the
         * Adapter and Layout Manager.
         */
        mMoviesRV = (RecyclerView)findViewById(R.id.recyclerview_movies);
        disconnectedTextView = (TextView)findViewById(R.id.disconnected_textview);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        }
        mMoviesRV.setLayoutManager(layoutManager);

        mMoviesRV.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(getApplicationContext(), this);
        mMoviesRV.setAdapter(mMovieAdapter);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        // If restoring state, we shall consider saved values, otherwise, we load the movies
        if (null != savedInstanceState &&
                savedInstanceState.containsKey(getString(R.string.movies_key))) {
            ArrayList movies = savedInstanceState.getParcelableArrayList(getString(R.string.movies_key));
            mMovieAdapter.setMovies(movies);
        } else {
            loadMovies();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    private void loadMovies() {
        ConnectivityManager connManager = (ConnectivityManager)
                                                getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        boolean isConnected = (networkInfo != null) && (networkInfo.isConnected());
        String sortType = sharedPreferences.getString(getString(R.string.sort_by_key),
                getString(R.string.popular_key));

        if (!isConnected && !sortType.equals(getString(R.string.favorite_key))) {
            disconnectedTextView.setVisibility(View.VISIBLE);
            mMoviesRV.setVisibility(View.INVISIBLE);
            return;
        }

        disconnectedTextView.setVisibility(View.INVISIBLE);
        mMoviesRV.setVisibility(View.VISIBLE);

        if (sortType.equals(getString(R.string.popular_key))) {
            new FetchMoviesData(this, this)
                    .execute(NetworkUtils.SEARCH_BY_POPULAR);
        } else if (sortType.equals(getString(R.string.top_rated_key))) {
            new FetchMoviesData(this, this)
                    .execute(NetworkUtils.SEARCH_BY_TOP_RATED);
        } else if (sortType.equals(getString(R.string.favorite_key))) {
            Cursor moviesCursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
            ArrayList<MovieJson.Movie> moviesList= new ArrayList<>();

            while (moviesCursor.moveToNext()) {
                MovieJson.Movie movie = new MovieJson.Movie();
                movie.setId(moviesCursor.getInt(
                        moviesCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_ID)));
                movie.setOriginal_title(moviesCursor.getString(
                        moviesCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_NAME)));
                movie.setOverview(moviesCursor.getString(
                        moviesCursor.getColumnIndex(MovieContract.MovieEntry.OVERVIEW)));
                movie.setRelease_date(moviesCursor.getString(
                        moviesCursor.getColumnIndex(MovieContract.MovieEntry.RELEASE_DATE)));
                movie.setVote_average(Float.parseFloat(moviesCursor.getString(
                        moviesCursor.getColumnIndex(MovieContract.MovieEntry.USER_RATING))));

                movie.setPoster_path(moviesCursor.getString(
                        moviesCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_POSTER)));

                moviesList.add(movie);
            }

            mMovieAdapter.setMovies(moviesList);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save state by putting the movies inside the bundle
        outState.putParcelableArrayList(getString(R.string.movies_key), mMovieAdapter.getMovies());

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        if (null != menuInflater) {
            menuInflater.inflate(R.menu.main_menu, menu);
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.settings == item.getItemId()) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieClicked(MovieJson.Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(getString(R.string.movies_key), movie);

        URL url = NetworkUtils.buildTrailerUrl(movie.getId());
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.sort_by_key))) {

            loadMovies();
        }
    }

    @Override
    public void onTaskCompleted(String result, int type) {
        // Here, we'll transform the JSON into a MovieJson Object using Gson
        if (MOVIE_DATA == type) {
            Gson gson = new Gson();
            MovieJson jsonObject = gson.fromJson(result, MovieJson.class);

            if (null != mMovieAdapter && null != jsonObject) {
                mMovieAdapter.setMovies(jsonObject.getResults());
            }
        }
    }
}
