package com.br.xplorer.helperbitmap.callback;

import android.graphics.Bitmap;

public interface CallbackAsyncTaskProcessBitmap {
    Bitmap doInBackground(Bitmap originalBitmap);
    void onPostExecute(Bitmap originalBitmap);
}
