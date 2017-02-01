/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 *
 * Inspired by: http://docs.oracle.com/javaee/6/api/javax/ejb/AsyncResult.html
 */

package mprog.nl.studentenschoonmaakapp.models;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Wraps the result of an asynchronous method call as a Future object.
 * This object is not passed to the user.
 * AsyncResult is just used to return a "empty" value when the asynchronous method
 * is done to force multiple asynchronous methods to wait for each other.
 */

public class AsyncResult<T> implements Future<Object> {
    public AsyncResult() {
    }

    @Override
    public boolean cancel(boolean b) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public Object get(long l, @NonNull TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
