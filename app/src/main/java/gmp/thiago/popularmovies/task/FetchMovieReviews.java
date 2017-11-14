package gmp.thiago.popularmovies.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

import gmp.thiago.popularmovies.utilities.NetworkUtils;

/**
 * Created by thiagom on 11/13/17.
 */

public class FetchMovieReviews extends AsyncTask<Integer, Void, String> {

    private Context mContext;
    private TaskCompleteListener mListener;

    public FetchMovieReviews(Context context, TaskCompleteListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    protected String doInBackground(Integer... params) {
        String jsonReviewsResponse = null;
        // If there's no search type, there's nothing we can do here
        if(params.length == 0) {
            return null;
        }

        int movieId = params[0];
        URL moviesUrl = NetworkUtils.buildReviewsUrl(movieId);

        Log.d("Thiago", moviesUrl.toExternalForm());

        try {
            jsonReviewsResponse = NetworkUtils.getHttpResponse(moviesUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonReviewsResponse;
    }

    @Override
    protected void onPostExecute(String result) {
        mListener.onTaskCompleted(result, TaskCompleteListener.MOVIE_REVIEW);
    }
}
