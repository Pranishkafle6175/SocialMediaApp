package com.example.instagram.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.Model.Notification;
import com.example.instagram.Model.Post;
import com.example.instagram.Model.User;
import com.example.instagram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private final List<Notification> notificationlist;
    private final Context context;

    public NotificationAdapter(List<Notification> notificationlist, Context context) {
        this.notificationlist = notificationlist;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        
        private CircleImageView notificationimage;
        private TextView notifcationtext;
        private ImageView postimage;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            notifcationtext= itemView.findViewById(R.id.notificationtext);
            notificationimage = itemView.findViewById(R.id.notificationuserimage);
            postimage = itemView.findViewById(R.id.notificationpostimage);
            
        }
    }
    
    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemnotification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        
        Notification notifications = notificationlist.get(position);
        
        getimage(holder.notificationimage,holder.postimage,notifications.getUserid() ,notifications.getPostid());

        getText(notifications,holder.notifcationtext,notifications.getUserid(),notifications.getText());


    }

    private void getText(Notification notifications, TextView notifcationtext, String userid, String text) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    User user = snapshot.getValue(User.class);
                    notifcationtext.setText(user.getUsername()+notifications.getText());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return notificationlist.size();
    }

    private void getimage(CircleImageView notificationimage, ImageView postimage, String userid, String postid) {

        //For Postimage

        FirebaseDatabase.getInstance().getReference().child("Posts").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                String imageurl = post.getImageurl();

                if(!imageurl.isEmpty()){
                    Picasso.get().load(imageurl).into(postimage);
                }else {
                    Picasso.get().load(R.mipmap.ic_launcher).into(postimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //For UserImage
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                String image = user.getImage();
                String username = user.getUsername();
                Log.i("Username",username);
                if(!image.isEmpty()){
                    Picasso.get().load(image).into(notificationimage);
                }else{
                    Picasso.get().load(R.drawable.ic_person).into(notificationimage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
