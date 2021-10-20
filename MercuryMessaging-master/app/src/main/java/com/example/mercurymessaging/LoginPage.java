package com.example.mercurymessaging;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {
    private TextView textView;
    private CardView Login;

    private EditText email, password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Login = findViewById(R.id.LoginCview);

        mAuth = FirebaseAuth.getInstance();
        email = this.findViewById(R.id.editTextTextPersonName3);
        password = this.findViewById(R.id.editTextTextPassword);


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });

        textView = findViewById(R.id.textView2);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

    }
    public void openRegister(){
        Intent intent = new Intent(this, RegisterPage.class);
        startActivity(intent);
        finish();
    }
    public void openHome(){
        String emailid = email.getText().toString().trim();
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

        if(pwd.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(emailid,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginPage.this, Home.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginPage.this,"Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}