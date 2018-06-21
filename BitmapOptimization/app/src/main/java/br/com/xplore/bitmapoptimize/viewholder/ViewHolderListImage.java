package br.com.xplore.bitmapoptimize.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import br.com.xplore.bitmapoptimize.R;


public class ViewHolderListImage extends RecyclerView.ViewHolder {

    private ImageView imageView;

    public ViewHolderListImage(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image);
    }

    public ImageView getImageView() {
        return imageView;
    }
}
