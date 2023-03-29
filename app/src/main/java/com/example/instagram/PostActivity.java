package com.example.instagram;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.Fragments.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    ImageView postimageview;
    TextView post;
    ImageView close;
    Uri imageuri;
    private String downloadurl;
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postimageview = (ImageView) findViewById(R.id.postaddimage);
        description = (EditText) findViewById(R.id.postdescription);
        post = (TextView) findViewById(R.id.posttextview);
        close = (ImageView)findViewById(R.id.postclose);


        postimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery,1);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference("uploads").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()+System.currentTimeMillis()+getFileExtension(imageuri));
                //UploadTask is a class in the Firebase Storage API for Android.
                // It represents the task of uploading a file to the Firebase Storage.
                // When you call putFile() on a StorageReference, it returns an UploadTask object that represents
                // the ongoing upload operation. You can use this object to monitor the progress of the upload and
                // to add success or failure listeners to be notified when the upload completes.
                UploadTask uploadTask = storageReference.putFile(imageuri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostActivity.this, "Unsucessful Upload", Toast.LENGTH_LONG).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
                        //push() will create a unique id and getkey() will return us that code
                        String uniquepostid = databaseReference.push().getKey();

                        //creatind hasmap to store object
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadurl = uri.toString();

                                HashMap<String,Object> map = new HashMap<>();
                                map.put("postid",uniquepostid);
                                map.put("Uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                map.put("imageurl",downloadurl);
                                map.put("Description",description.getText().toString());
                                databaseReference.child(uniquepostid).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(PostActivity.this, "Added to Realtime databse Sucessfully", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(PostActivity.this, MainActivity.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PostActivity.this, "Unsucessfull add to realtime databse", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });

                    }
                });



            }
        });
    }

    public String getFileExtension(Uri uri){
        // getContentResolver is a method od ContentResolver but we cannot create a object of that because it is a abstract class
        //we need to implement all the abstract method so getContentResolver gives the object of that class and gettype() gives the mimetype
        String mimetype = getContentResolver().getType(uri);
        // we cannot create object of Mimetype as it has a private constructor and we cannot create object so getsingleton gives the object of mimetype
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String fileextension = mimeTypeMap.getExtensionFromMimeType(mimetype);
        return fileextension;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            imageuri = data.getData();
            postimageview.setImageURI(imageuri);
        }
    }
}