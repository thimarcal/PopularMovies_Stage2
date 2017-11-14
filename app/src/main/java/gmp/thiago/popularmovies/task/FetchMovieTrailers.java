package gmp.thiago.popularmovies.task;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

import gmp.thiago.popularmovies.utilities.NetworkUtils;

/**
 * Created by thiagom on 11/13/17.
 */

public class FetchMovieTrailers extends AsyncTask<Integer, Void, String> {

    private Context mContext;
    private TaskCompleteListener mListener;

    public FetchMovieTrailers(Context context, TaskCompleteListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    protected String doInBackground(Integer... params) {
        String jsonTrailersResponse = null;
        // If there's no search type, there's nothing we can do here
        if(params.length == 0) {
            return null;
        }

        int movieId = params[0];
        URL moviesUrl = NetworkUtils.buildTrailerUrl(movieId);

        try {
            jsonTrailersResponse = NetworkUtils.getHttpResponse(moviesUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonTrailersResponse;
    }

    @Override
    protected void onPostExecute(String result) {
        mListener.onTaskCompleted(result, TaskCompleteListener.MOVIE_TRAILER);
    }
}
