package com.example.mercurymessaging;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.mercurymessaging.data.model.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPage extends AppCompatActivity {
    private TextView textView;
    private CardView Continue;

    private FirebaseAuth mAuth;

    private EditText email, userName, password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        mAuth = FirebaseAuth.getInstance();

        email = this.findViewById(R.id.textView4);
        userName = this.findViewById(R.id.textView7);
        password = this.findViewById(R.id.textView8);

        Continue = findViewById(R.id.ContinueCview);
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });

        textView = findViewById(R.id.textView12);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignIn();
            }
        });
    }
    public void openSignIn(){
        Intent intent = new Intent(this,LoginPage.class);
        startActivity(intent);
        finish();
    }

    public void openHome(){

        String emailid = email.getText().toString().trim();
        String userid = userName.getText().toString().trim();
        String pwd = password.getText().toString().trim();

        if(emailid.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailid).matches()){
            email.setError("Enter a Valid Email Id");
            email.requestFocus();
            return;
        }
        if(userid.isEmpty()) {
            userName.setError("UserName is required");
            userName.requestFocus();
            return;
        }
        if(pwd.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if(pwd.length()<6){
            password.setError("Minimum password length should be 6");
            password.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailid,pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            UserInformation user = new UserInformation(emailid,userid);

                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterPage.this,"User Registered Sucessfully", Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(RegisterPage.this, Home.class);
                                        startActivity(intent);
                                        finish();


                                    }else{
                                        Toast.makeText(RegisterPage.this,"Failed to Register", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterPage.this,"Failed to Register", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}