package br.com.xplore.bitmapoptimize.asynctask;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import br.com.xplore.bitmapoptimize.asynctask.callback.CallbackAsyncTasks;

import java.util.List;

public class AsyncTaskReadBitmaps extends AsyncTask<Void, Void, List<Bitmap>> {

    private CallbackAsyncTasks<Bitmap> callbackAsyncTasks;

    public AsyncTaskReadBitmaps(CallbackAsyncTasks<Bitmap> callbackAsyncTasks) {
        this.callbackAsyncTasks = callbackAsyncTasks;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callbackAsyncTasks.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<Bitmap> bitmaps) {
       callbackAsyncTasks.onPostExecute(bitmaps);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(List<Bitmap> bitmaps) {
        super.onCancelled(bitmaps);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected List<Bitmap> doInBackground(Void... voids) {
        return callbackAsyncTasks.doInBackground();
    }
}
