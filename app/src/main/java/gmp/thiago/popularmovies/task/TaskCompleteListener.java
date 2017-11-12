package gmp.thiago.popularmovies.task;

/**
 * Created by thiagom on 11/12/17.
 */

public interface TaskCompleteListener<T> {
    public void onTaskCompleted(T result);
}
