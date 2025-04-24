package com.example.news.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.adapters.NewsAdapter;
import com.example.news.adapters.TopStoriesAdapter;
import com.example.news.models.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

        private RecyclerView recyclerTopStories;
        private RecyclerView recyclerNews;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_home, container, false);

            recyclerTopStories = view.findViewById(R.id.recyclerTopStories);
            recyclerNews = view.findViewById(R.id.recyclerNews);

            List<NewsItem> topStoriesList = getSampleNews("Top Story");
            List<NewsItem> newsList = getSampleNews("News");

            TopStoriesAdapter topAdapter = new TopStoriesAdapter(topStoriesList, getContext(), this::navigateToDetail);
            NewsAdapter newsAdapter = new NewsAdapter(newsList, getContext(), this::navigateToDetail);

            recyclerTopStories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

            recyclerTopStories.setAdapter(topAdapter);
            recyclerNews.setAdapter(newsAdapter);

            return view;
        }


    private List<NewsItem> getSampleNews(String category) {
        List<NewsItem> list = new ArrayList<>();

        if (category.equals("Top Story")) {

            list.add(new NewsItem(
                            "Biden signs foreign aid package",
                            "https://dims.apnews.com/dims4/default/bdf51d0/2147483647/strip/true/crop/3931x2621+0+0/resize/1440x960!/format/webp/quality/90/?url=https%3A%2F%2Fassets.apnews.com%2F4f%2F9d%2Fe9edd6decd8ea45ac02c6de40ee6%2F261a4181ade446e08aa62b3629277544",
                            "President Biden signed a $95 billion foreign aid package including support for Ukraine, Israel, and Taiwan.",
                            "AP News"
                    ));

            list.add(new NewsItem(
                    "Pope Francis Dies at 88; Global Tributes Pour In",
                    "https://ichef.bbci.co.uk/ace/standard/1024/cpsprodpb/f21f/live/31887130-1efd-11f0-80b3-83959215671c.jpg",
                    "The pontiff passed away due to stroke-related complications. Leaders worldwide honor his legacy on peace and climate advocacy.",
                    "The Guardian"
            ));

            list.add(new NewsItem(
                    "Early Voting Begins in Australian Federal Election Amid Economic Concerns",
                    "https://res.cloudinary.com/cognitives-s3/image/upload/c_limit,dpr_auto,f_auto,fl_lossy,h_1900,q_auto,w_1900/v1/cog-live/n/1271/2025/Apr/22/nvHLShXEc0fnGsauMSFn.jpg",
                    "Australians begin casting votes early ahead of the May 3 election, with the economy, housing, and cybersecurity in focus.",
                    "The Guardian"
            ));

            list.add(new NewsItem(
                    "Jax Taylor Seeks Mental Health Treatment After Domestic Incident",
                    "https://www.geo.tv/assets/uploads/updates/2025-04-22/601176_4391420_updates.jpg",
                    "The reality TV star enters a mental health program following a domestic altercation and announces ongoing sobriety progress.",
                    "People Magazine"
            ));

        }else if (category.equals("News")) {
                list.add(new NewsItem(
                        "NASA News",
                        "https://i.abcnewsfe.com/a/61c6b55b-87a5-4764-bd51-df8bbee32401/artemis-crew-1-abc-gmh-230403_1680547449661_hpMain_16x9.jpg?w=992",
                        "NASA's Artemis II crew, including the first woman and person of color on a lunar mission, begins training for their 2025 Moon orbit mission.",
                        "Learn More"
                ));

                list.add(new NewsItem(
                        "ISRO Report",
                        "https://dims.apnews.com/dims4/default/141f7ec/2147483647/strip/true/crop/2142x1600+0+0/resize/1440x1076!/format/webp/quality/90/?url=https%3A%2F%2Fassets.apnews.com%2F93%2Fad%2Fda2a5afe2266d6d0c83b966d738e%2Fdabdc6f7d3604ca4ab41e0617082f121",
                        "India’s Chandrayaan-3 rover confirms the presence of sulfur near the Moon’s south pole, aiding future lunar missions and science goals.",
                        "Learn More"
                ));

                list.add(new NewsItem(
                        "Electrek",
                        "https://s1.cdn.autoevolution.com/images-webp/news/tesla-hints-at-imminent-fsd-launch-in-australia-changes-the-name-to-fsd-supervised-247893-7.jpeg.webp",
                        "Tesla plans to launch Full Self-Driving (FSD) subscription services in Australia by late 2025, starting with a limited rollout.",
                        "Learn More"
                ));

                list.add(new NewsItem(
                        "TechCrunch",
                        "https://www.thetechoutlook.com/wp-content/uploads/2025/03/Gemini-trip-Planner.jpg",
                        "Google Maps introduces AI-powered travel planning using Gemini AI, offering itinerary suggestions and hidden spot alerts.",
                        "Learn More"
                ));
            }

            return list;
    }


    private void navigateToDetail(NewsItem item) {
        NewsDetailFragment detailFragment = new NewsDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putString("title", item.getTitle());
        bundle.putString("description", item.getDescription());
        bundle.putString("imageUrl", item.getImageUrl());  // Add this line to pass the image
        detailFragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, detailFragment)  // make sure this ID matches your layout
                .addToBackStack(null)
                .commit();
    }

}
