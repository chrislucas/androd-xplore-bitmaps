package com.br.xplorer.helperbitmap;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.br.xplorer.helperbitmap.callback.CallbackAsyncTaskProcessBitmap;

public class AsyncTaskProcessBitmap extends AsyncTask<Void, Void, Bitmap> {


    private Bitmap originalBitmap;
    private CallbackAsyncTaskProcessBitmap callbackAsyncTaskProcessBitmap;

    public AsyncTaskProcessBitmap(Bitmap originalBitmap, CallbackAsyncTaskProcessBitmap callbackAsyncTaskProcessBitmap) {
        this.originalBitmap = originalBitmap;
        this.callbackAsyncTaskProcessBitmap = callbackAsyncTaskProcessBitmap;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        return callbackAsyncTaskProcessBitmap.doInBackground(originalBitmap);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        callbackAsyncTaskProcessBitmap.onPostExecute(result);
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
