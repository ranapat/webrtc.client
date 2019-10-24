package android.os;

/**
 * This is a shadow class for AsyncTask which forces it to run synchronously.
 */
public abstract class AsyncTask<Params, Progress, Result> {

    @SuppressWarnings("unchecked")
    protected abstract Result doInBackground(Params... params);

    @SuppressWarnings("unchecked")
    protected void onPostExecute(Result result) {
    }

    @SuppressWarnings("unchecked")
    protected void onProgressUpdate(Progress... values) {
    }

    @SuppressWarnings("unchecked")
    public AsyncTask<Params, Progress, Result> execute(Params... params) {
        Result result = doInBackground(params);
        onPostExecute(result);
        return this;
    }

    @SuppressWarnings("unchecked")
    public static void execute(Runnable runnable) {
        runnable.run();
    }
}