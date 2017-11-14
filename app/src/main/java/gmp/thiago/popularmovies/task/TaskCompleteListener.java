package gmp.thiago.popularmovies.task;

/**
 * Created by thiagom on 11/12/17.
 */

public interface TaskCompleteListener<T> {
    public static final int MOVIE_DATA = 1;
    public static final int MOVIE_TRAILER = 2;
    public static final int MOVIE_REVIEW = 3;
    public void onTaskCompleted(T result, int type);
}
