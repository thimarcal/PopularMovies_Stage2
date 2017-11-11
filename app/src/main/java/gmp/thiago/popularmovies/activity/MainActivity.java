package gmp.thiago.popularmovies.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import gmp.thiago.popularmovies.R;
import gmp.thiago.popularmovies.adapter.MovieAdapter;
import gmp.thiago.popularmovies.data.MovieJson;
import gmp.thiago.popularmovies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity
                                         implements MovieAdapter.MovieClickListener,
                                            SharedPreferences.OnSharedPreferenceChangeListener {
    private RecyclerView mMoviesRV;
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
        String sortType = sharedPreferences.getString(getString(R.string.sort_by_key),
                                                      getString(R.string.popular));
        if (sortType.equals(getString(R.string.popular))) {
            new FetchMoviesData().execute(NetworkUtils.SEARCH_BY_POPULAR);
        } else if (sortType.equals(getString(R.string.top_rated))) {
            new FetchMoviesData().execute(NetworkUtils.SEARCH_BY_TOP_RATED);
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

        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.sort_by_key))) {

            loadMovies();
        }
    }


    /**
     * AsyncTask for Fetching Movies data from TheMoviesDB
     */
    public class FetchMoviesData extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {

            String jsonMoviesResponse = null;
            // If there's no search type, there's nothing we can do here
            if(params.length == 0) {
                return null;
            }

            int searchType = params[0];
            URL moviesUrl = NetworkUtils.buildUrl(searchType);

            try {
                jsonMoviesResponse = NetworkUtils.getHttpResponse(moviesUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jsonMoviesResponse;
        }

        @Override
        protected void onPostExecute(String response) {
            // Here, we'll transform the JSON into a MovieJson Object using Gson
            Gson gson = new Gson();
            MovieJson jsonObject = gson.fromJson(response, MovieJson.class);

            if (null != mMovieAdapter && null != jsonObject) {
                mMovieAdapter.setMovies(jsonObject.getResults());
            }
        }
    }
}
