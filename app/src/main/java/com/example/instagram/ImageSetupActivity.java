package com.example.instagram;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class ImageSetupActivity extends AppCompatActivity {
    ImageView setupimage;
    ActivityResultLauncher<Intent> activityResultLauncher;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_setup);

        setupimage= findViewById(R.id.setupimage);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK && result.getData() != null){
//
//                    Uri imageUri = result.getData().getData();
//                    setupimage.setImageURI(imageUri);
//
//                    //In this case, the Intent that is returned by the gallery app contains the URI of the selected image
//                    // in its data field. So when we call result.getData(), we get an Intent object that contains the image URI as its data field.
//                    //
//                    //To extract the actual URI of the selected image, we need to call getData() again on the Intent object.
//                    // This returns the Uri object that represents the image data.
//                    //
//                    //So the expression result.getData().getData() returns the Uri object that represents the selected image.
//                    // We can then convert this to a string URL using the toString() method, as shown in the updated implementation.
//
//                    //FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("image").setValue(imageUri);
//                    FirebaseDatabase.getInstance().getReference()
//                            .child("Users")
//                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                            .child("image")
//                            .setValue(imageUri.toString());
//                }
//            }
                    Uri imageUri = result.getData().getData();
                    setupimage.setImageURI(imageUri);

                    // Get a reference to the Firebase Storage location where you want to upload the image
                    StorageReference imageRef = storageRef.child("images/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + UUID.randomUUID().toString());

                    // Upload the image to Firebase Storage
                    imageRef.putFile(imageUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                // Get the download URL of the uploaded image
                                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    // Save the download URL in Firebase Realtime Database as the image URL for the current user
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("image")
                                            .setValue(uri.toString());
                                });
                                startActivity(new Intent(ImageSetupActivity.this, MainActivity.class));
                            })
                            .addOnFailureListener(e -> {
                            });
                }
            }
        });

        setupimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                activityResultLauncher.launch(gallery);
            }
        });
    }
}