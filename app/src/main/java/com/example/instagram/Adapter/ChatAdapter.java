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

import com.example.instagram.Model.Chats;
import com.example.instagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    public static final int MESSAGE_COUNT_lEFT = 0;
    public static final int MESSAGE_COUNT_RIGHT = 1;

    FirebaseUser firebaseUser;

    String Userimage;


    List<Chats> mchat;
    Context context;

    public ChatAdapter(Context context, List<Chats> chatlist, String image) {
        this.context = context;
        this.mchat = chatlist;
        this.Userimage= image;
       // Log.i("Image",image);
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView chatleftmessage;
        ImageView chatleftimage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chatleftimage = itemView.findViewById(R.id.chatleftimage);
            chatleftmessage = itemView.findViewById( R.id.chatmessage);

        }
    }
    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == MESSAGE_COUNT_RIGHT){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_right, parent, false);
            return new ViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_left, parent, false);
            return new ViewHolder(view);
        }
    }
    public void setUserImage(String image) {
        this.Userimage = image;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {

        Chats chats = mchat.get(position);
        Log.i("mcat size", String.valueOf(mchat.size()));

        if(chats.getMessage() != null){
            Log.i("THis is chat",chats.getMessage());
        }
        else{
            Log.i("No chat","No chat");
        }




        holder.chatleftmessage.setText(chats.getMessage());
        if (Userimage != null && holder.chatleftimage != null) {
            Log.i("chatleftimage",Userimage);
            Picasso.get().load(Userimage).into(holder.chatleftimage);
        }


    }

    @Override
    public int getItemCount() {
        return mchat.size();
    }

    @Override
    public int getItemViewType(int position) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(mchat.get(position).getSender().equals(firebaseUser.getUid())){

            return MESSAGE_COUNT_RIGHT;

        }else{
            return MESSAGE_COUNT_lEFT;
        }


    }
}
