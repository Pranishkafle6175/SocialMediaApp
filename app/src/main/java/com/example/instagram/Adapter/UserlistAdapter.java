package com.example.instagram.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.ChatActivity;
import com.example.instagram.MessageHomeActivity;
import com.example.instagram.Model.User;
import com.example.instagram.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserlistAdapter extends RecyclerView.Adapter<UserlistAdapter.ViewHolder> {
    Context context;
    List<User> friendlist;

    public UserlistAdapter(Context context, List<User> friendlist) {
        this.context = context;
        this.friendlist= friendlist;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userimage;
        TextView userlistusernametextview;
        TextView userlistemail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userimage = itemView.findViewById(R.id.userlistimage);
            userlistusernametextview= itemView.findViewById(R.id.userlistusernametextview);
            userlistemail = itemView.findViewById(R.id.userlistemail);
        }
    }


    @NonNull
    @Override
    public UserlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.userlist, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserlistAdapter.ViewHolder holder, int position) {

        User user = friendlist.get(position);
        Picasso.get().load(user.getImage()).into(holder.userimage);
        holder.userlistusernametextview.setText(user.getUsername());
        holder.userlistemail.setText(user.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userid",user.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return friendlist.size();
    }


}
