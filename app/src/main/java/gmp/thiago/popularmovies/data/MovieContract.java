package gmp.thiago.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

/**
 * Created by thiagom on 11/14/17.
 */

public class MovieContract {

    public static final String AUTHORITY = "gmp.thiago.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String MOVIES_PATH = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(MOVIES_PATH).build();

        public static final String TABLE_NAME = "movies";
        public static final String MOVIE_NAME = "movieName";
        public static final String MOVIE_ID = "movieId";
        public static final String USER_RATING = "userRating";
        public static final String MOVIE_POSTER = "moviePoster";
        public static final String RELEASE_DATE = "releaseDate";
        public static final String OVERVIEW = "overview";
        public static final String REVIEWS_INFO = "reviewsJson";
        public static final String TRAILERS_INFO = "trailersJson";
    }
}
