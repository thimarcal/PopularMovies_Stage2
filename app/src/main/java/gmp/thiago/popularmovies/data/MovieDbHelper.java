package gmp.thiago.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import gmp.thiago.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by thiagom on 11/15/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "popularMovies.db";

    private static final int DATABASE_VERSION = 2;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE "+ MovieEntry.TABLE_NAME + " ("
                + MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieEntry.MOVIE_NAME + " TEXT NOT NULL, "
                + MovieEntry.MOVIE_ID + " INTEGER NOT NULL, "
                + MovieEntry.MOVIE_POSTER + " BLOB NOT NULL, "
                + MovieEntry.OVERVIEW + " TEXT NOT NULL, "
                + MovieEntry.RELEASE_DATE + " TEXT NOT NULL, "
                + MovieEntry.USER_RATING + " TEXT NOT NULL, "
                + MovieEntry.REVIEWS_INFO + " TEXT NOT NULL, "
                + MovieEntry.TRAILERS_INFO + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
