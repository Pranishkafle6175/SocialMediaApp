package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instagram.Adapter.UserlistAdapter;
import com.example.instagram.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageHomeActivity extends AppCompatActivity {

    String userprofileimageurl;
    RecyclerView friendlistrecyclerview;
    UserlistAdapter userlistAdapter;
    List<User> friendlist;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_home);



        friendlistrecyclerview = findViewById(R.id.chatlistrecyclerview);
        friendlistrecyclerview.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MessageHomeActivity.this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        friendlistrecyclerview.setLayoutManager(layoutManager);

        friendlist = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userlistAdapter = new UserlistAdapter(MessageHomeActivity.this,friendlist);
        friendlistrecyclerview.setAdapter(userlistAdapter);


        getfriendlist();

    }

    private void getfriendlist() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("Following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendlist.clear();
                FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            for(DataSnapshot dataSnapshot1 : snapshot1.getChildren()){
                                if(dataSnapshot1.getKey().equals(dataSnapshot.getKey())){
                                    User user = dataSnapshot1.getValue(User.class);
                                    friendlist.add(user);
                                    Log.i("friendlist",user.getUsername());
                                    break;
                                }
                            }
                        }
                        userlistAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}