package gmp.thiago.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by thiagom on 9/27/17.
 */

public class NetworkUtils {

    private static final String BASE_MOVIESDB_URL = "http://api.themoviedb.org/3/movie/";

    private static final String POPULAR_SEARCH = "popular";
    private static final String TOP_RATED_SEARCH = "top_rated";

    private static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY = ""; // Add Valid TheMoviesDB API KEY here
    public static final int SEARCH_BY_POPULAR = 0;
    public static final int SEARCH_BY_TOP_RATED = 1;

    public static URL buildUrl(int searchType) {

        String searchLocation = null;
        switch (searchType) {
            case SEARCH_BY_POPULAR:
                searchLocation = POPULAR_SEARCH;
                break;
            case SEARCH_BY_TOP_RATED:
                searchLocation = TOP_RATED_SEARCH;
                break;
            default:
                return null;
        }

        Uri builtUri = Uri.parse(BASE_MOVIESDB_URL).buildUpon()
                                .appendPath(searchLocation)
                                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                                .build();

        URL queryURL = null;
        try {
            queryURL = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return queryURL;
    }

    /**
     * This method is going to return the String for a Http request
     */
    public static String getHttpResponse (URL httpURL) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) httpURL.openConnection();
        try {
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\A"); // Retrieve all the data

            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }

        } finally {
            connection.disconnect();
        }
    }
}
