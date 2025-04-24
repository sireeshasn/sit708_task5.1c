package com.example.news.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.news.R;
import com.example.news.models.NewsItem;

import java.util.List;

public class TopStoriesAdapter extends RecyclerView.Adapter<TopStoriesAdapter.TopStoriesViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(NewsItem item);
    }

    private final List<NewsItem> items;
    private final Context context;
    private final OnItemClickListener listener;

    public TopStoriesAdapter(List<NewsItem> items, Context context, OnItemClickListener listener) {
        this.items = items;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TopStoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_top_story, parent, false);
        return new TopStoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopStoriesViewHolder holder, int position) {
        NewsItem item = items.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.descriptionTextView.setText(item.getDescription());

        String imageUrl = item.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (imageUrl.startsWith("data:image")) {
                try {
                    String base64Image = imageUrl.split(",")[1];
                    byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    holder.imageView.setImageBitmap(decodedByte);
                } catch (Exception e) {
                    Log.e("TopStoriesAdapter", "Base64 decoding error", e);
                    holder.imageView.setImageResource(R.drawable.error_image);
                }
            } else if (imageUrl.endsWith(".svg")) {
                Log.w("TopStoriesAdapter", "SVG not supported: " + imageUrl);
                holder.imageView.setImageResource(R.drawable.error_image);
            } else {
                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                        Target<Drawable> target, boolean isFirstResource) {
                                Log.e("GlideError", "Top Story Load failed: " + imageUrl, e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model,
                                                           Target<Drawable> target, DataSource dataSource,
                                                           boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(holder.imageView);
            }
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder_image);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class TopStoriesViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView;
        ImageView imageView;

        public TopStoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.newsTitle);
            descriptionTextView = itemView.findViewById(R.id.newsDesc);
            imageView = itemView.findViewById(R.id.newsImage);
        }
    }
}
