package br.com.xplore.bitmapoptimize;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.br.xplorer.helperbitmap.HelperBitmap;

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

public class Main2Activity extends AppCompatActivity {

    private RecyclerView recyclerViewBitmap;
    private AdapterListImageView adapterListImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerViewBitmap = findViewById(R.id.list_bitmaps);
        readImagesFromAssets();
    }


    private List<Bitmap> readImagesFromAssets() {
        List<Bitmap> bitmaps = new ArrayList<>();
        AssetManager assetManager = getAssets();
        String [] paths = {"test1.jpeg", "test2.jpeg"};
        for(String path : paths) {
            try {
                InputStream stream = assetManager.open(path);
                BitmapFactory.Options options = new BitmapFactory.Options();
                Rect paddingRect = new Rect(0,0,0,0);
                bitmaps.add(HelperBitmap.decodeStream(stream, paddingRect, options));
            }
            catch (IOException e) {
                Log.e("READ_IMAGE_FROM_ASSETS", e.getMessage());
            }
        }
        adapterListImageView = new AdapterListImageView(bitmaps);
        recyclerViewBitmap.setAdapter(adapterListImageView);
        return bitmaps;
    }
}
