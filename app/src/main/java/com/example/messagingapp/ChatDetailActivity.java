package com.example.messagingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.messagingapp.Adapters.ChatAdapter;
import com.example.messagingapp.Models.MessageModel;
import com.example.messagingapp.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<MessageModel> arrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        ChatAdapter chatAdapter = new ChatAdapter(arrayList, this);
        binding.chatDetailRecyclerView.setAdapter(chatAdapter);
        binding.chatDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        String chatUserName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");
        String senderId = auth.getUid();
        String receiverId = getIntent().getStringExtra("userId");


        binding.chatUserName.setText(chatUserName);
        Picasso.get().load(profilePic).placeholder(R.drawable.user).into(binding.chatProfileImage);
        binding.chatBackArrow.setOnClickListener(v -> startActivity(new Intent(ChatDetailActivity.this,MainActivity.class)));

        String senderRoom = senderId + receiverId;
        String receiverRoom = receiverId + senderId;


        binding.send.setOnClickListener(v -> {
            String message = binding.edtMessage.getText().toString();
            final MessageModel messageModel = new MessageModel(senderId, message);
            messageModel.setTimestamp(new Date().getTime());
            binding.edtMessage.setText("");

            database.getReference().child("chats")
                    .child(senderRoom)
                    .push()
                    .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    database.getReference().child("chats")
                            .child(receiverRoom)
                            .push()
                            .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                }
            });
        });


        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        arrayList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            MessageModel model = dataSnapshot.getValue(MessageModel.class);
                            arrayList.add(model);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }
}