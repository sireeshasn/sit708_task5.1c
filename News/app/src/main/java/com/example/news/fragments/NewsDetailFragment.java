package com.example.news.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.news.R;
import com.example.news.adapters.RelatedNewsAdapter;
import com.example.news.models.Article;
import com.example.news.models.NewsApiResponse;
import com.example.news.models.NewsItem;
import com.example.news.network.NewsApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsDetailFragment extends Fragment {

    private ImageView newsImage;
    private TextView newsTitle, newsDescription;
    private RecyclerView relatedRecyclerView;
    private String title;

    public NewsDetailFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_detail, container, false);

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        newsImage = view.findViewById(R.id.newsImage);
        newsTitle = view.findViewById(R.id.newsTitle);
        newsDescription = view.findViewById(R.id.newsDescription);
        relatedRecyclerView = view.findViewById(R.id.recyclerRelatedNews);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        Bundle args = getArguments();
        if (args != null) {
            title = args.getString("title");
            newsTitle.setText(title);
            newsDescription.setText(args.getString("description"));

            String imageUrl = args.getString("imageUrl");

            if (imageUrl != null) {
                if (imageUrl.startsWith("data:image")) {
                    try {
                        String base64Image = imageUrl.split(",")[1];
                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        newsImage.setImageBitmap(decodedByte);
                    } catch (Exception e) {
                        newsImage.setImageResource(R.drawable.error_image);
                    }
                } else {
                    // ✅ Load URL image with Glide
                    Glide.with(requireContext())
                            .load(imageUrl)
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.error_image)
                            .into(newsImage);
                }
            } else {
                newsImage.setImageResource(R.drawable.placeholder_image);
            }
        }

        relatedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchRelatedNews(title);

        return view;
    }

    private void fetchRelatedNews(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsApiService apiService = retrofit.create(NewsApiService.class);

        apiService.getRelatedNews(query, "543f5c977b1e47bf81b91d27bb725113", 1).enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsApiResponse> call, @NonNull Response<NewsApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().articles.isEmpty()) {
                    Article article = response.body().articles.get(0);

                    List<NewsItem> relatedList = new ArrayList<>();
                    relatedList.add(new NewsItem(
                            article.title,
                            article.urlToImage,
                            article.description,
                            article.url
                    ));

                    RelatedNewsAdapter adapter = new RelatedNewsAdapter(relatedList, getContext());
                    relatedRecyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "No related news found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsApiResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Failed to load related news", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
