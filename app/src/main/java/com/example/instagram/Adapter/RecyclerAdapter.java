package com.example.instagram.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.Model.User;
import com.example.instagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private final List<User> userlist ;
    private final Context context;
    private final boolean isFragment;
    public FirebaseUser firebaseUser;

    public RecyclerAdapter(List<User> userlist, Context context, boolean isFragment) {
        this.userlist = userlist;
        this.context = context;
        this.isFragment = isFragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView imageview;
        public TextView username;
        public TextView fullname;
        public Button follow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview = (CircleImageView) itemView.findViewById(R.id.circularimage);
            username = (TextView) itemView.findViewById(R.id.recyclerviewusername);
            fullname = (TextView) itemView.findViewById(R.id.recyclerviewfullname);
            follow = (Button) itemView.findViewById(R.id.recyclerbutton);
            follow.setVisibility(View.INVISIBLE);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_recycleview, parent, false);
        return new RecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = userlist.get(position);
        holder.username.setText(user.getUsername());
        holder.fullname.setText(user.getName());

        Picasso.get().load(user.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageview);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("Following");
        if(firebaseUser.getUid().equals(user.getId())){
            holder.follow.setVisibility(View.INVISIBLE);
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(user.getId()).exists()){
                    holder.follow.setVisibility(View.VISIBLE);
                    holder.follow.setText("Following");
                }
                else{
                    holder.follow.setText("Follow");
                    holder.follow.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.follow.getText().toString().equals("Follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("Following").child(user.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId()).child("Followers").child(firebaseUser.getUid()).setValue(true);
                    holder.follow.setText("Following");


                }else{

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("Following").child(user.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId()).child("Followers").child(firebaseUser.getUid()).removeValue();
                    holder.follow.setText("Follow");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }


}
