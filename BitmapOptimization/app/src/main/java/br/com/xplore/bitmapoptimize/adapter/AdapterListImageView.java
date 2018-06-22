package br.com.xplore.bitmapoptimize.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.xplore.bitmapoptimize.R;
import br.com.xplore.bitmapoptimize.viewholder.ViewHolderListImage;

public class AdapterListImageView extends RecyclerView.Adapter<ViewHolderListImage> {

    private List<Bitmap> bitmaps;

    public AdapterListImageView(List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    @NonNull
    @Override
    public ViewHolderListImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_adapter_list_imageview, parent, false);
        return new ViewHolderListImage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderListImage holder, int position) {
        holder.getImageView().setImageBitmap(bitmaps.get(position));
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }
}
