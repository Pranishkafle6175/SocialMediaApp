package com.example.instagram.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.Model.Post;
import com.example.instagram.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileGridAdapter extends RecyclerView.Adapter<ProfileGridAdapter.ViewHolder> {
    Context context;
    List<Post> postitem;

    public ProfileGridAdapter(Context context, List<Post> postitem) {

        this.context = context;
        this.postitem= postitem;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.gridimage);
        }
    }


    @NonNull
    @Override
    public ProfileGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemgridphoto, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileGridAdapter.ViewHolder holder, int position) {
        Post post = postitem.get(position);
        Picasso.get().load(post.getImageurl()).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return postitem.size();
    }


}
