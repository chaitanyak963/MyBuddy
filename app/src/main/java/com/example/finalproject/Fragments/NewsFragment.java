package com.example.finalproject.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.finalproject.Interfaces.MyInterface;
import com.example.finalproject.NewsAdapter;
import com.example.finalproject.Pojo;
import com.example.finalproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NewsFragment extends Fragment {


    RecyclerView rv;
    ArrayList<Pojo> list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        // final TextView textView = root.findViewById(R.id.text_dashboard);
        rv = root.findViewById(R.id.rvNews);
        list = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory
                        (ScalarsConverterFactory.create())
                .build();
        Call<String> response = retrofit.
                create(MyInterface.class).value();
        response.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String res = response.body();
                try {
                    JSONObject root = new JSONObject(res);
                    JSONArray articles = root.getJSONArray("articles");
                    for (int i = 0; i < articles.length(); i++) {
                        JSONObject jsonObject = articles.getJSONObject(i);
                        JSONObject source = jsonObject.getJSONObject("source");
                        String name = source.getString("name");
                        String img = jsonObject.getString("urlToImage");
                        String title = jsonObject.getString("title");
                        String author = jsonObject.getString("author");
                        String desc = jsonObject.getString("description");
                        String ur = jsonObject.getString("url");
                        String date = jsonObject.getString("publishedAt");
                        String content = jsonObject.getString("content");
                        Pojo pojo = new Pojo(name, author, title, desc, ur, img, date, content);
                        list.add(pojo);
                    }
                    NewsAdapter adapter = new NewsAdapter(getContext(), list);
                    rv.setAdapter(adapter);
                    rv.setLayoutManager(new LinearLayoutManager(getContext()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Failed to fetch",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

}