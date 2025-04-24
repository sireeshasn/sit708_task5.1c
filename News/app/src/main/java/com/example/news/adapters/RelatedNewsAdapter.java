package com.example.news.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.news.R;
import com.example.news.models.NewsItem;

import java.util.List;

public class RelatedNewsAdapter extends RecyclerView.Adapter<RelatedNewsAdapter.RelatedNewsViewHolder> {

    private final List<NewsItem> relatedList;
    private final Context context;

    public RelatedNewsAdapter(List<NewsItem> relatedList, Context context) {
        this.relatedList = relatedList;
        this.context = context;
    }

    @NonNull
    @Override
    public RelatedNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_related_news, parent, false);
        return new RelatedNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedNewsViewHolder holder, int position) {
        NewsItem item = relatedList.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());

        String imageUrl = item.getImageUrl();
        if (imageUrl != null && imageUrl.startsWith("data:image")) {
            // Base64 image
            try {
                String base64Image = imageUrl.split(",")[1];
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.image.setImageBitmap(decodedByte);
            } catch (Exception e) {
                holder.image.setImageResource(R.drawable.error_image);
            }
        } else {
            // URL image with Glide
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(holder.image);
        }

        // Open external news link in browser
        holder.itemView.setOnClickListener(v -> {
            String url = item.getSourceUrl();
            if (url != null && !url.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Show only one related story as per your wireframe
        return Math.min(relatedList.size(), 1);
    }

    public static class RelatedNewsViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, description;

        public RelatedNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.relatedImage);
            title = itemView.findViewById(R.id.relatedTitle);
            description = itemView.findViewById(R.id.relatedDesc);
        }
    }
}
