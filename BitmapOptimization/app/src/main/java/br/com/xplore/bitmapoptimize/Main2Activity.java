package br.com.xplore.bitmapoptimize;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import br.com.xplore.bitmapoptimize.asynctask.AsyncTaskReadBitmaps;
import com.br.xplorer.helperbitmap.HelperBitmap;
import br.com.xplore.bitmapoptimize.asynctask.callback.CallbackAsyncTasks;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.xplore.bitmapoptimize.adapter.AdapterListImageView;


/**
 *
 *
 * "Some application displays photos taken using your Android
 * devices's camera which are typically much higher resolution
 * than the screen density of your device"
 *
 * "Given that you are working with limited memory
 * ,ideally you only want to load a lower resolution version in memory.
 * The lower resolution version should match the size of the UI component that displays it."
 *
 * https://developer.android.com/topic/performance/graphics/load-bitmap
 *
 * */

public class Main2Activity extends AppCompatActivity implements CallbackAsyncTasks<Bitmap> {

    private AdapterListImageView adapterListImageView;
    private List<Bitmap> bitmaps;

    private static final String BUNDLE_LIST_BITMAPS = "BUNDLE_LIST_BITMAPS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        RecyclerView recyclerViewBitmap = findViewById(R.id.list_bitmaps);

        if (savedInstanceState == null)
            bitmaps = new ArrayList<>();
        else {
            bitmaps = savedInstanceState.getParcelableArrayList(BUNDLE_LIST_BITMAPS);
            if (bitmaps == null)
                bitmaps = new ArrayList<>();
        }
        adapterListImageView = new AdapterListImageView(bitmaps);
        recyclerViewBitmap.setAdapter(adapterListImageView);
        recyclerViewBitmap.setLayoutManager(new LinearLayoutManager(this));

        if (bitmaps.size() == 0) {
            AsyncTaskReadBitmaps async = new AsyncTaskReadBitmaps(this);
            async.execute();
        }
    }

    private Bitmap readBitmap(String filename, AssetManager assetManager) {
        Bitmap b = null;
        try {
            InputStream stream = assetManager.open(filename);
            BitmapFactory.Options options = new BitmapFactory.Options();
            /**
             * Marcar esse atributo como true faz com que o metodo decodificacao para bitmap
             * retorne um bitmap nulo, porem as dimensoes armazenadas nos atributos
             * outWidth e outHeight serao definidas, assim nao alocamos memoria para o bitmap
             * mas temos informacoes da sua dimensao e  tipo. Isso eh interessante para podemos saber
             * informacoes da imagem sem precisarmos alocar memoria para ela
             * */
            options.inJustDecodeBounds = true;
            Rect paddingRect = new Rect(0,0,0,0);
            b = HelperBitmap.decodeStream(stream, paddingRect, options);
            if (b != null)
                bitmaps.add(b);
            Log.i("DIMENSION", String.format("Width: %d, Height: %d, MimeType: %s"
                    , options.outWidth, options.outHeight, options.outMimeType));
        }
        catch (IOException e) {
            Log.e("READ_IMAGE_FROM_ASSETS", e.getMessage());
        }

        return b;
    }

    private boolean readAllDirectoriesFromAssets(AssetManager assetManager, List<Bitmap> bitmaps, String path) {
        try {
            String [] paths = assetManager.list(path);
            for (String inner : paths) {
                String c = path.concat(File.separator).concat(inner);
                if ( ! readAllDirectoriesFromAssets(assetManager, bitmaps, inner) ) {
                    return false;
                }
                // https://www.regular-expressions.info/modifiers.html
                if (c.matches(".+\\.(?i)(jpeg|png|jpg)"))
                    bitmaps.add(readBitmap(c, assetManager));
                else
                    return readAllDirectoriesFromAssets(assetManager, bitmaps, c);
            }
        }
        catch (IOException e) {
            Log.e("READ_PATH_FROM_ASSETS", e.getMessage());
        }
        // se retornar true eh porque o path aponta para um arquivo nao uma pasta
        return true;
    }

    @Override
    public List<Bitmap> doInBackground() {
        List<Bitmap> bitmaps = new ArrayList<>();
        readAllDirectoriesFromAssets(getAssets(), bitmaps, "");
        return bitmaps;
    }

    @Override
    public void onPostExecute(List<Bitmap> list) {
        bitmaps.addAll(list);
        adapterListImageView.notifyDataSetChanged();
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            bitmaps = savedInstanceState.getParcelableArrayList(BUNDLE_LIST_BITMAPS);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BUNDLE_LIST_BITMAPS, (ArrayList<? extends Parcelable>) bitmaps);
    }
}
