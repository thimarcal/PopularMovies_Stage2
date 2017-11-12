package gmp.thiago.popularmovies.task;

/**
 * Created by thiagom on 11/11/17.
 */

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

import gmp.thiago.popularmovies.data.MovieJson;
import gmp.thiago.popularmovies.utilities.NetworkUtils;

/**
 * AsyncTask for Fetching Movies data from TheMoviesDB
 */
public class FetchMoviesData extends AsyncTask<Integer, Void, String> {

    private Context mContext;
    private TaskCompleteListener<String> mListener;

    public FetchMoviesData (Context context, TaskCompleteListener<String> listener) {
        mContext = context;
        mListener = listener;
    }

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
        mListener.onTaskCompleted(response);
    }
}