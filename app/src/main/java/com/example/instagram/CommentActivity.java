package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagram.Adapter.CommentAdapter;
import com.example.instagram.Model.Comment;
import com.example.instagram.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {
    private CircleImageView commentcircularimage;
    private EditText commenttextview;
    private Button commentPostButton;
    RecyclerView commentRecyclerview;
    FirebaseUser firebaseUser;
    String Imageurl;
    String Postid;
    String Username;
    private CommentAdapter commentAdapter;
    List<Comment> commentList;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        commentcircularimage= findViewById(R.id.commentimage);
        commenttextview= findViewById(R.id.commenttextview);
        commentPostButton= findViewById(R.id.commentpostbutton);
        commentRecyclerview= findViewById(R.id.commentrecyclerview);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        Postid=intent.getStringExtra("postid");
        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            User user = snapshot.getValue(User.class);
                            Username= (String)snapshot.child("Username").getValue();
                            if(user.getImage() != null){
                                Picasso.get().load(user.getImage()).into(commentcircularimage);
                            }
                            else{
                                Picasso.get().load(R.drawable.ic_person).into(commentcircularimage);
                            }

                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        //When i tried to load the image through Picasso out of theondatachange method the image was not
        //loading so here is the solution for that:::



        //If you need to use the Imageurl variable outside of the onDataChange() method,
        // you should do so within the onDataChange() method itself or in a separate method
        // that is called from within it. For example, you could move the code
        // that needs to use the Imageurl variable into a separate method and
        // call that method from within the onDataChange() method:
        if(Imageurl != null){
            Log.i("Imageurl",Imageurl);
        }else{
            Log.i("ImageurlNotfound","Unable to Fetch ImageUrl");
        }

        commentRecyclerview.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(CommentActivity.this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        commentRecyclerview.setLayoutManager(layoutManager);
        commentList = new ArrayList<>();

        commentAdapter = new CommentAdapter(commentList,CommentActivity.this);
        commentRecyclerview.setAdapter(commentAdapter);


        commentPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Uid = firebaseUser.getUid();

                addNotification(Postid,Uid);
                getimage(Uid);



            }
        });
        getCommentItems();

    }

    private void getCommentItems() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(Postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    Comment comment = dataSnapshot.getValue(Comment.class);
                    commentList.add(comment);

                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getimage(String uid){
        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    imageUrl = dataSnapshot.getValue(String.class);
                    image(imageUrl);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });

    }

    private void image(String imageUrl) {
        String commentDescription= commenttextview.getText().toString();
        HashMap<String,Object> map = new HashMap<>();
        map.put("Username",Username);
        map.put("postid",Postid);
        map.put("commentDescription",commentDescription);
        map.put("Uid",firebaseUser.getUid());
        map.put("Imageurl",imageUrl);
        FirebaseDatabase.getInstance().getReference().child("Comments").child(Postid).child(firebaseUser.getUid()).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CommentActivity.this, "Comment Posted Sucessfully", Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CommentActivity.this, "Unable to add Comment", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void addNotification(String postid, String uid) {
        HashMap< String, Object> hashMap= new HashMap<>();
        hashMap.put("userid",uid);
        hashMap.put("postid",postid);
        //hashMap.put("ispost",true);
        hashMap.put("text","  Commented your post");

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(firebaseUser.getUid()).push().setValue(hashMap);

    }
}