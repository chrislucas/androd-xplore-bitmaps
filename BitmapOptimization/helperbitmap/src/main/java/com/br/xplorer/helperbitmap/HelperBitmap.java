package com.br.xplorer.helperbitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.BitmapCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

/**
 * Created by r028367 on 05/07/2017.
 */

public class HelperBitmap {

    /**
     *  Converte um bitmap em um array de bytes com um nome mais sugestivo
     * */
    public static byte [] fromBitmapToByteArray(Bitmap bitmap, Bitmap.CompressFormat compressFormat, int quality) {
        return compressAndChangeQuality(bitmap, compressFormat, quality);
    }

    /**
     * Converte um bitmap em um array de bytes
     * */
    public static byte [] compressAndChangeQuality(Bitmap bitmap, Bitmap.CompressFormat compressFormat, int quality) {
        if (quality < 0 || quality > 100) {
            throw new IllegalArgumentException("O intervalo de qualidade deve estar entre [0, 100]");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(compressFormat, quality, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte [] compressAndChangeQuality(byte [] buffer, Bitmap.CompressFormat compressFormat, int quality) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
        return compressAndChangeQuality(bitmap, compressFormat, quality);
    }

    public static Bitmap fromDrawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        if(drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            bitmap = bitmapDrawable.getBitmap();
            return bitmap;
        }
        else {
            int iWidth = drawable.getIntrinsicWidth();
            int iHeight = drawable.getIntrinsicHeight();
            if(iWidth <=0 || iHeight <=0) {
                // 8 bits para cada canal de cor ARGB (4bytes usandos no total)
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            }
            else {
                bitmap = Bitmap.createBitmap(iWidth, iHeight, Bitmap.Config.ARGB_8888);
            }
        }
        if(!bitmap.isRecycled()) {
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return bitmap;
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width
                , v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

    public static Bitmap getBitmapFromView(View v) {
        v.measure(View.MeasureSpec.makeMeasureSpec(0
                , View.MeasureSpec.UNSPECIFIED)
                , View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false); // clear drawing cache
        return b;
    }

    /**
     * Pegar a imagem dentro do ImageView e tranformar em bitmap
     * */
    public static Bitmap getBitmapFromImageView(ImageView imageView) {
        //Tentar recuperar um copiaBmp a partir de um ImageView
        Bitmap bitmap = imageView.getDrawingCache();
        // Se nao for possivel
        if (bitmap == null) {
            // tentar recuperar a partir de um Drawable
            bitmap = HelperBitmap.fromDrawableToBitmap(imageView.getDrawable());
        }
        return bitmap;
    }

    public static final String DEFAULT_COMPRESSED_FILE_NAME = "bitmap.png";

    public static WeakReference<Bitmap> resize(WeakReference<Bitmap> weakReference, int width, int height) {
        Bitmap bitmap = weakReference.get();
        // largura original
        int originalWidth   = bitmap.getWidth();
        // altura original
        int originalHeight  = bitmap.getHeight();
        // calcular dimensao proporcional baseado na dimensao que queremos
        float scaleW = (width * 1.0f / (originalWidth * 1.0f));
        float scaleH = (height * 1.0f / (originalHeight * 1.0f));
        Matrix matrix = new Matrix();
        // Aplicando operacao de Escala na imagem
        matrix.postScale(scaleW, scaleH);
        return new WeakReference<>(Bitmap.createBitmap(bitmap, 0, 0, originalWidth, originalHeight, matrix, true));
    }

    public static boolean compressAsStream(WeakReference<Bitmap> bitmapWeakReference
            , Bitmap.CompressFormat compressFormat, Context context, int quanlity) {
        return compressAsStream(bitmapWeakReference, compressFormat, context, DEFAULT_COMPRESSED_FILE_NAME, quanlity);
    }

    public static boolean compressAsStream(WeakReference<Bitmap> bitmapWeakReference
            , Bitmap.CompressFormat compressFormat, Context context, String name, int quanlity) {
        try {
            Bitmap bitmap = bitmapWeakReference.get();
            FileOutputStream stream = context.openFileOutput(name, Context.MODE_PRIVATE);
            boolean rs = bitmap.compress(compressFormat, quanlity, stream);
            stream.close();
            return rs;
        }
        catch (IOException fexp) {
            Log.e("EXCP_COMPRESS_BITMAP", fexp.getMessage());
        }
        return false;
    }

    public static Bitmap decompressAsStream(Context context) {
        return decompressAsStream(context, DEFAULT_COMPRESSED_FILE_NAME);
    }

    public static Bitmap decompressAsStream(Context context, String name) {
        Bitmap bitmap = null;
        try {
            FileInputStream fis = context.openFileInput(name);
            bitmap = BitmapFactory.decodeStream(fis);
        }
        catch (IOException ioex) {
            Log.e("EXCP_DECOMPRESS_BITMAP", ioex.getMessage());
        }
        return bitmap;
    }

    public static WeakReference<Bitmap> fromByArrayToBitmap(byte [] buffer) {
        return new WeakReference<>(BitmapFactory.decodeByteArray(buffer, 0, buffer.length));
    }

    /**
     * metodo para alterar a qualidade
     * */
    public static WeakReference<Bitmap> mudarQualidade(Bitmap bitmap, int qualidade) {
        WeakReference<Bitmap> weakReference = null;
        try {
            byte [] array =  HelperBitmap.compressAndChangeQuality(bitmap, Bitmap.CompressFormat.JPEG, qualidade);
            weakReference = HelperBitmap.fromByArrayToBitmap(array);
        }
        catch (IllegalArgumentException e) {
            String message = e.getMessage() == null ? "Não foi possivel recuperar o erro" : e.getMessage();
            Log.e("COMPRESS_IMAGE_LIST", message);
        }

        return weakReference;
    }

    /**
     * Metodo para redimentionsar um bitmap
     * */
    public static WeakReference<Bitmap> resize(byte [] array, int width, int height) {
        /**
         * Redimensionar as imagens
         * */
        return resize(HelperBitmap.fromByArrayToBitmap(array), width, height);
    }



    public static byte [] fromInputStreamToByteArray(@Nullable InputStream inputStream) throws IOException {
        if (inputStream == null)
            return null;
        int read;
        byte [] buffer = new byte[1<<16];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while ( (read = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, read);
        }
        return byteArrayOutputStream.toByteArray();
    }


    public static byte [] fromUriToByteArray(Uri uri, Context context) throws Exception {
        byte [] foto = null;
        if (context == null)
            throw new Exception("Contexto não pode ser nulo");
        /**
         * Recuperar arquivo da imagem que foi salva numa pasta escondida no celular
         * */
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            /**
             * Converte uma imagem no formato de Stream Para uma array de bytes
             **/
            foto = fromInputStreamToByteArray(inputStream);
        }
        catch (IOException e) {
            String message = e.getMessage() == null ? "" : e.getMessage();
            Log.e("EXCP_FROM_URI_IN_STREAM", message);
        }
        return foto;
    }


    /**
     * Para recuperar a medida proporcional dado um lado da imagem, informe qual lado eh a base para
     * o calculo de proporcao e sua medida em pixels
     *
     * Para redimensionar a imagem para 640 pixels de largura, precisamos
     * achar o valor para altura proporcional, uma vez que a Imagem pode
     * ter por exemplo 3000x2500 de dimensao. Basta realizar o seguinte
     * calculo: 640 / 3000 * 2500 e teremos a altura proporcional
     * */
    public static double getProportionalMeasureSide(Bitmap bitmap, int measure, @Dimension2D int side) {
        /**
         * Se eu quero recuperar a altura proporcional dado a largura
         * Dividir a nova largura pela antiga e multiplicar pela antiga altura
         * */
        if (side == Dimension2D.WIDTH) {
            return (measure * 1.0f) / (bitmap.getWidth() * 1.0f) * (bitmap.getHeight() * 1.0f);
        }
        else {
            /**
             * Se eu quero recuperar a largura proporcional dado uma altura
             * Dividir a nova altura pela antiga e multiplicar pela antiga largura
             * */
            return  (measure * 1.0f) / (bitmap.getHeight() * 1.0f) * (bitmap.getWidth() * 1.0f);
        }
    }

    /**
     * IntDef utilizado para que o usuario da lib possa indicar se ele quer recuperar
     * a altura proporcional a uma largura ou vice-versa
     * */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Dimension2D.WIDTH, Dimension2D.HEIGHT})
    public @interface Dimension2D {
         int WIDTH = 1, HEIGHT = 2;
    }

    public static Bitmap resizeImageFromByteArray(@NonNull byte [] foto, int measure, @Dimension2D int side) {
        Log.i("PHOTO", String.format("%d bytes Antes de redimensionar\n.%f MB"
                , foto.length, foto.length / (1024.0f*1024.0f)));
        /**
         * Mudar a imagem que vai para a lista
         * */
        Bitmap original = HelperBitmap.fromByArrayToBitmap(foto).get();

        Log.i("PHOTO", String.format("Imagem Original: Dimensao (%d, %d)"
                , original.getWidth(), original.getHeight()));

        int measure2 = (int) HelperBitmap.getProportionalMeasureSide(original, measure, side);
        Bitmap bitmap = null;
        if (side == Dimension2D.WIDTH) {
            bitmap = HelperBitmap.resize(foto, measure, measure2).get();
        }
        else {
            bitmap = HelperBitmap.resize(foto, measure2, measure).get();
        }

        Log.i("PHOTO", String.format("Imagem da Lista: Dimensao (%d, %d)."
                    +
                        "\nQuantidade de bytes Minimos para alocar essa imagem %d."
                    +
                        "\nQuantidade de MB Minimos para alocar essa imagem %f."
                , bitmap.getWidth()
                , bitmap.getHeight()
                , bitmap.getByteCount()
                , bitmap.getByteCount() * 1.0f / (1024.0f * 1024.0f)
        ));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            int b = bitmap.getAllocationByteCount();
            Log.i("PHOTO", String.format("Bytes alocados para armazenar essa imagem %d.\n" +
                            "MB alocados para armazenar essa imagem %f."
                    , b
                    , b * 1.0f / (1024.0f * 1024.0f)
                )
            );
        }
        return bitmap;
    }

    public static Bitmap resizeImageWithFullQuality(@NonNull byte [] foto, int measure, @Dimension2D int side) {
        return HelperBitmap.mudarQualidade(resizeImageFromByteArray(foto, measure, side), 100).get();
    }

    public static Bitmap resizeImageAndChangeQuality(@NonNull byte [] foto, int measure, int quality, @Dimension2D int side) {
        return HelperBitmap.mudarQualidade(resizeImageFromByteArray(foto, measure, side), quality).get();
    }

    public static long getAllocationByteCount(Bitmap bitmap) {
        return BitmapCompat.getAllocationByteCount(bitmap);
    }

    public static Bitmap decodeStream(InputStream inputStream) {
        return BitmapFactory.decodeStream(inputStream);
    }

    public static Bitmap decodeStream(InputStream inputStream, Rect outPadding, BitmapFactory.Options options) {
        return BitmapFactory.decodeStream(inputStream, outPadding, options);
    }
}
