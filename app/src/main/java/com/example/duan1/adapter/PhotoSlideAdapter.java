package com.example.duan1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.R;
import com.example.duan1.model.PhotoSlide;

import java.util.List;

public class PhotoSlideAdapter extends RecyclerView.Adapter<PhotoSlideAdapter.PhotoViewHolder> {
    private final List<PhotoSlide> mList;

    public PhotoSlideAdapter(List<PhotoSlide> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_slide, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        PhotoSlide photo = mList.get(position);
        if (photo == null) {
            return;
        }
        holder.imgPhoto.setImageResource(photo.getImage());
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgPhoto;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_slide);
        }
    }
}
