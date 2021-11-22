package com.example.messagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.messagingapp.Models.Users;
import com.example.messagingapp.databinding.ActivitySignUpBinding;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Please wait.....");
        progressDialog.setMessage("We are creating your account");
        binding.btnSignUp.setOnClickListener(v -> {
            progressDialog.show();
            btnSignUp();

        });

        binding.tvAlready.setOnClickListener(v -> tvAlreadyOnClick());
    }


    private void tvAlreadyOnClick() {
        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
    }

    private void btnSignUp() {
        auth.createUserWithEmailAndPassword(binding.etEmail.getText().toString(), binding.etPassword.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Users user = new Users(binding.etUsername.getText().toString().trim(),
                                binding.etEmail.getText().toString().trim(),
                                binding.etPassword.getText().toString().trim());

                        String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
                        database.getReference().child("Users").child(id).setValue(user);
                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));

                        finish();
                        Toast.makeText(SignUpActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}