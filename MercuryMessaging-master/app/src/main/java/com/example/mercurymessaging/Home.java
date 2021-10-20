package com.example.mercurymessaging;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {
    private Button newReminder;
    private Button sendNow;
    private Button upcomingReminders;
    private TextView logout;
    private static String userName;
    private TextView greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        //user = generateTestUser();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("uid", uid);
        greeting = this.findViewById(R.id.greeting);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userName = snapshot.child("userName").getValue().toString();
                greeting.setText("Hello, " + userName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        newReminder = findViewById(R.id.newReminder);
        newReminder.setOnClickListener(v -> onNewReminderClick());

        sendNow = findViewById(R.id.sendNow);
        sendNow.setOnClickListener(v -> onSendNowClick());

        upcomingReminders = findViewById(R.id.upcomingReminders);
        upcomingReminders.setOnClickListener(v -> upcomingRemindersAction());

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> launchLogin());
    }

    public String generateTestUser() {
        return "Test" + (int) (Math.random() * 1000);
    }

    public static String getUser() {
        return userName;
    }

    public void upcomingRemindersAction() {
        Intent i = new Intent(this, UpcomingReminders.class);
        startActivity(i);
    }

    public void onSendNowClick() {
        Intent intent = new Intent(this, SendNow.class);
        startActivity(intent);
    }

    public void onNewReminderClick()
    {
        Intent intent = new Intent(this, SetReminder.class);
        startActivity(intent);
    }

    public void launchLogin(){
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
        finish();
    }

}