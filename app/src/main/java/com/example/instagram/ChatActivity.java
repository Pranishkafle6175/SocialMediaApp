package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.Adapter.ChatAdapter;
import com.example.instagram.Model.Chats;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    ImageView userimage;
    TextView usernametextview;
    RecyclerView chatrecyclerview;
    EditText chatmessage;
    ImageView chatsendimage;
    String userid;
    List<Chats> chatlist;
    ChatAdapter chatAdapter;
    RecyclerView.Adapter adapter;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userimage = findViewById(R.id.chatuserimage);
        usernametextview = findViewById(R.id.chatusername);
        chatrecyclerview = findViewById(R.id.chatrecyclerview);
        chatmessage = findViewById(R.id.chatedittext);
        chatsendimage = findViewById(R.id.chatsendimageview);

        userid= getIntent().getStringExtra("userid");

        FirebaseDatabase.getInstance().getReference().child("Users").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = (String) snapshot.child("Username").getValue();
                Log.i("Name",name);
                usernametextview.setText(name);
                image = (String) snapshot.child("image").getValue();
                Log.i("Image image",image);
                Picasso.get().load(image).into(userimage);
                chatAdapter.setUserImage(image);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        chatlist = new ArrayList<>();
        //Log.i("Image",image);
        chatAdapter = new ChatAdapter(ChatActivity.this,chatlist , null);
        chatrecyclerview.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(false);
        chatrecyclerview.setLayoutManager(layoutManager);

        adapter = chatrecyclerview.getAdapter();

        getmessages();

        // put the Username in the chat



        chatsendimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> hashMap = new HashMap<>();
                String chat = chatmessage.getText().toString();
                hashMap.put("sender", FirebaseAuth.getInstance().getCurrentUser().getUid());
                hashMap.put("receiver",userid);
                hashMap.put("message",chat);
                if(!TextUtils.isEmpty(chat)){

                    FirebaseDatabase.getInstance().getReference().child("Chats").push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ChatActivity.this, "Message Send", Toast.LENGTH_LONG).show();
                            chatmessage.setText("");
                        }
                    });

                }else{
                    chatmessage.setError("Text is Empty! Please Type...");
                }
                chatAdapter.notifyDataSetChanged();
            }

        });
    }

    private void getmessages() {
        FirebaseDatabase.getInstance().getReference().child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatlist.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chats chats = dataSnapshot.getValue(Chats.class);
                    if(((chats.getSender().equals(userid)) && (chats.getReceiver().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))) || (((chats.getReceiver().equals(userid)) && (chats.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))))){

                        chatlist.add(chats);
                        Log.i("chats Added",chats.getMessage());
                        chatrecyclerview.scrollToPosition(adapter.getItemCount() - 1);



                    }else{
//                        Log.i("userid",userid);
//                        Log.i("My id", FirebaseAuth.getInstance().getCurrentUser().getUid());
//                        Log.i("sender",chats.getSender());
//                        Log.i("Receiver",chats.getReceiver());
//                        Log.i(" No chats","no chat");
                    }
                }
                chatAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}