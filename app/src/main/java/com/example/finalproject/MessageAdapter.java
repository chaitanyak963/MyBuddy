package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.finalproject.Model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_LEFT = 0;
    public static final int MSG_RIGHT = 1;

    Context ct;
    List<Chat> mchat;
    String imageurl;

    FirebaseUser fuser;

    public MessageAdapter(Context ct, List<Chat> mchat, String imageurl) {
        this.ct = ct;
        this.mchat = mchat;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_RIGHT){
            View v = LayoutInflater.from(ct).inflate(R.layout.chat_item_right,parent,false);
            return new ViewHolder(v);
        }else{
            View v = LayoutInflater.from(ct).inflate(R.layout.chat_item_left,parent,false);
            return new ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Chat chat = mchat.get(position);
            holder.tv.setText(chat.getMessage());
            if (imageurl.equals("default")){
                holder.iv.setImageResource(R.mipmap.ic_launcher);
            }
            else{
                Glide.with(ct).load(imageurl).into(holder.iv);
            }
        if (position == mchat.size()-1){
            if (chat.isIsseen()){
                holder.ts.setText("Seen");
            } else {
                holder.ts.setText("Delivered");
            }
        } else {
            holder.ts.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mchat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv,ts;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.pi);
            tv = itemView.findViewById(R.id.showmsz);
            ts = itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mchat.get(position).getSender().equals(fuser.getUid())){
            return MSG_RIGHT;
        }else{
            return MSG_LEFT;
        }
    }
}
