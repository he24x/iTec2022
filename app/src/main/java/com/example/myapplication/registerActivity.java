package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class registerActivity extends AppCompatActivity {

    TextInputLayout usernameRegister,emailRegister,passwordRegister,passwordConfirmationRegister;
    FirebaseAuth firebaseAuth;
    Button registerButton;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameRegister=findViewById(R.id.userRegister);
        emailRegister=findViewById(R.id.emailRegister);
        passwordRegister=findViewById(R.id.passwordRegister);
        passwordConfirmationRegister=findViewById(R.id.passwordConfirmationRegister);
        firebaseAuth= FirebaseAuth.getInstance();
        registerButton=findViewById(R.id.registerButton);
        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(registerActivity.this,mainApp.class));
            finish();
        }
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = usernameRegister.getEditText().getText().toString().trim();
                String email = emailRegister.getEditText().getText().toString().trim();
                String password = passwordRegister.getEditText().getText().toString().trim();
                String passwordConfirm = passwordConfirmationRegister.getEditText().getText().toString().trim();
                if(TextUtils.isEmpty(user)){
                    usernameRegister.setError("Username is required!");
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    emailRegister.setError("Email is required!");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    passwordRegister.setError("Password is required!");
                    return;
                }
                if(TextUtils.isEmpty(passwordConfirm)){
                    passwordConfirmationRegister.setError("Password confirmation is required!");
                    return;
                }
                if(password.length()<6){
                    passwordRegister.setError("Password must be longer than 6 characters!");
                }
                if(!password.equals(passwordConfirm)){
                    passwordConfirmationRegister.setError("Password are not matching!");
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailRegister.setError("The email is invalid!");
                }

                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            assert  firebaseUser != null;
                            String userid = firebaseUser.getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username", user);
                            hashMap.put("email",email);
                            hashMap.put("tickets","0");
                            hashMap.put("qrcode","");
                            hashMap.put("confirmation", "");
                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(registerActivity.this,"The user was created!",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(registerActivity.this,mainApp.class));
                                        finish();
                                    }else{
                                        Toast.makeText(registerActivity.this,"Error!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(registerActivity.this,"Error!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}