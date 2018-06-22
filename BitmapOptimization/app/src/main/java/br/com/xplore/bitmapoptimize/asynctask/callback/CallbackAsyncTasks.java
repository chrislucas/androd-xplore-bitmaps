package br.com.xplore.bitmapoptimize.asynctask.callback;

import java.util.List;

public interface CallbackAsyncTasks <T> {
    List<T> doInBackground();
    void onPostExecute(List<T> list);
    void onPreExecute();
}
