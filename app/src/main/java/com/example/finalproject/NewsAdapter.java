package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    Context ct;
    ArrayList<Pojo> list;
    public NewsAdapter(Context context, ArrayList<Pojo> list) {
        ct = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(ct).inflate(R.layout.row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(ct).load(list.get(position).getImg()).into(holder.iv);
        holder.tv.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv;
        ImageView iv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            tv = itemView.findViewById(R.id.tv);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Intent i = new Intent(ct,DisplayActivity.class);
            i.putExtra("position",pos);
            i.putExtra("name",list.get(pos).getName());
            i.putExtra("urlToImage",list.get(pos).getImg());
            i.putExtra("title",list.get(pos).getTitle());
            i.putExtra("author",list.get(pos).getAuthor());
            i.putExtra("description",list.get(pos).getDesc());
            i.putExtra("url",list.get(pos).getUr());
            i.putExtra("publishedAt",list.get(pos).getDate());
            i.putExtra("content",list.get(pos).content);
            ct.startActivity(i);
        }
    }
}
