package com.example.instagram.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.CommentActivity;
import com.example.instagram.Model.Comment;
import com.example.instagram.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> commentlist;
    Context context;

    public CommentAdapter(List<Comment> commentList, Context context) {
        this.commentlist=commentList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imageview;
        private TextView textview;
        private TextView commenttextview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageview= itemView.findViewById(R.id.commentrecyclerimageview);
            textview= itemView.findViewById(R.id.commentrecyclertextview);
            commenttextview=itemView.findViewById(R.id.commentusernametextview);
        }
    }


    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemcommentlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {

        Comment comment= commentlist.get(position);
        if(comment.getImageurl()!= null){
            Log.i("CommentAdapterImageUrl",comment.getImageurl());
            Picasso.get().load(comment.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.imageview);
        }

        holder.textview.setText(comment.getCommentDescription());
        holder.commenttextview.setText(comment.getUsername());


    }

    @Override
    public int getItemCount() {
        return commentlist.size();
    }


}
