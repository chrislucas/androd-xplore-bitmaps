package com.br.xplorer.helperbitmap.asynctask;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class AsyncTaskProcessBmp extends AsyncTask<Void, Void, Bitmap> {


    public interface CallbackAsyncTask {
        Bitmap doInBackground(Bitmap originalBitmap);
        void onPostExecute(Bitmap bitmap);
    }

    private Bitmap originalBitmap;
    private CallbackAsyncTask callbackAsyncTask;

    public AsyncTaskProcessBmp(Bitmap originalBitmap, CallbackAsyncTask callbackAsyncTask) {
        this.originalBitmap = originalBitmap;
        this.callbackAsyncTask = callbackAsyncTask;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        return callbackAsyncTask.doInBackground(originalBitmap);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        callbackAsyncTask.onPostExecute(result);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Bitmap result) {
        super.onCancelled(result);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
