package com.example.instagram.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.instagram.Adapter.RecyclerAdapter;
import com.example.instagram.Model.User;
import com.example.instagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    private List<User> userlist;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private EditText searchtext;
    private RecyclerAdapter recyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //You cannot use findViewById in the onCreateView() method of a Fragment because the layout views have not yet been inflated.
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        userlist= new ArrayList<>();
        searchtext = view.findViewById(R.id.searchedittext);


        recyclerView= view.findViewById(R.id.firstrecycler);
        //Without a LayoutManager, the RecyclerView doesn't know how to position items in the list,
        // so it won't be able to display any content.
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        readUser();

        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUser(charSequence.toString());
                Log.i("textt changed","searchtext");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        recyclerAdapter = new RecyclerAdapter(userlist,getContext(),true);
        recyclerView.setAdapter(recyclerAdapter);
        // Inflate the layout for this fragment
        return view;

    }

    private void searchUser(String s) {
        Query databasequery = FirebaseDatabase.getInstance().getReference().child("Users")
                .orderByChild("Username").startAt(s).endAt(s +"\uf8ff");
        databasequery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userlist.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                       User user = dataSnapshot.getValue(User.class);
                       userlist.add(user);
                }
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readUser() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              if(TextUtils.isEmpty(searchtext.getText().toString())){
                  userlist.clear();
                  for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                      User user = dataSnapshot.getValue(User.class);
                      userlist.add(user);
                  }
                  recyclerAdapter.notifyDataSetChanged();
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}