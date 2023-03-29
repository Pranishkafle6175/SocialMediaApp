package com.example.instagram.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.CommentActivity;
import com.example.instagram.Model.Post;
import com.example.instagram.Model.User;
import com.example.instagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private final List<Post> list;
    public FirebaseUser firebaseUser;
    private final Context context;
    private  String userid;
    private String username;

    public PostAdapter(List<Post> list, Context context) {
        this.list=list;
        this.context = context;
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView userprofileimg;
        private TextView username;
        private ImageView postmore;
        private ImageView postimage;
        private ImageView like;
        private ImageView comment;
        private ImageView save;
        private TextView nooflikes;
        private TextView postnametext;
        private TextView description;
        private TextView noofcomment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userprofileimg = itemView.findViewById(R.id.itempostcircularimage);
            username = itemView.findViewById(R.id.itempostusernametextview);
            postmore=itemView.findViewById(R.id.itempostmore);
            postimage= itemView.findViewById(R.id.itempostimage);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.itempostcomment);
            save = itemView.findViewById(R.id.itempostsave);
            nooflikes = itemView.findViewById(R.id.itempostnooflike);
            postnametext=itemView.findViewById(R.id.itempostnametextview);
            description= itemView.findViewById(R.id.itempostdescription);
            noofcomment = itemView.findViewById(R.id.itempostnoofcomment);

        }
    }


    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itempost, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Post post = list.get(position);
        holder.description.setText(post.getDescription());
        Picasso.get().load(post.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.postimage);
        getinfo(post,holder.username,holder.postnametext,holder.userprofileimg);



        checknoofcomment(post.getPostid(),holder.noofcomment);

        checkLike(post.getPostid(),holder.like);
        nooflike(post.getPostid(),holder.nooflikes);


        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( holder.like.getTag().equals("like")){

                    Log.i("Like pressed","pressed");
                    FirebaseDatabase.getInstance().getReference().child("Like").child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);
                    addNotification(post.getPostid(),post.getUid());

                }else{
                    Log.i("Like Notpressed","Notpressed");

                    FirebaseDatabase.getInstance().getReference().child("Like").child(post.getPostid()).child(firebaseUser.getUid()).removeValue();

                }

            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postid",post.getPostid());
                context.startActivity(intent);
            }
        });

        holder.noofcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postid",post.getPostid());
                context.startActivity(intent);

            }
        });
    }

    private void getinfo(Post post, TextView username, TextView postnametext, CircleImageView userprofileimg) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        Log.i("Username","Username");
                        if(user.getImage()!=null){
                            Picasso.get().load(user.getImage()).into(userprofileimg);
                        }
                        username.setText(user.getUsername());
                        postnametext.setText(user.getName());


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void addNotification(String postid, String uid) {
        HashMap< String, Object> hashMap= new HashMap<>();
        hashMap.put("userid",uid);
        hashMap.put("postid",postid);
        //hashMap.put("ispost",true);
        hashMap.put("text","  liked your post");

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(uid).push().setValue(hashMap);

    }



    private void checknoofcomment(String postid, TextView noofcomment) {
        FirebaseDatabase.getInstance().getReference().child("Comments")
                .child(postid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        noofcomment.setText(snapshot.getChildrenCount() + " Comments");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void nooflike(String postid, TextView nooflikes) {
        FirebaseDatabase.getInstance().getReference().child("Like")
                .child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nooflikes.setText(snapshot.getChildrenCount() + "likes");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkLike(String postid, ImageView like) {

        FirebaseDatabase.getInstance().getReference().child("Like").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean liked= false;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals(firebaseUser.getUid())) {
                        liked = true;
                        break;
                    }
                }
                    if (liked) {
                        like.setImageResource(R.drawable.ic_customfavourite);

                        like.setTag("liked");
                    } else {
                        like.setImageResource(R.drawable.ic_favourite);
                        like.setTag("like");
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}
