package com.example.mercurymessaging;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mercurymessaging.data.SetReminderHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class confirmation_screen_simulated extends AppCompatActivity {
    String RemDate;
    String RemTime;
    String RemMessage;
    String name;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_screen_simulated);
        Button Done = this.findViewById(R.id.Confirm);
        Done.setOnClickListener(v -> new WriteData().execute());
        Button cancel = this.findViewById(R.id.Cancel);
        list = this.findViewById(R.id.confirmRecipients);

        cancel.setOnClickListener(v -> finish());

        TextView RMessage = findViewById(R.id.ReminderMessage);
        TextView RTime = findViewById(R.id.ReminderTime);
        TextView RDate = findViewById(R.id.ReminderDate);

        RemTime = getIntent().getStringExtra("Rtime");
        RemDate = getIntent().getStringExtra("Rdate");
        RemMessage = getIntent().getStringExtra("Rmessage");
        name = Home.getUser();

        Log.d("BUG", "RemTime: " + RemTime);
        Log.d("BUG", "RemDate: " + RemDate);
        Log.d("BUG", "RemMessage: " + RemMessage);

        //set the viewtext values
        RMessage.setText(RemMessage);
        RDate.setText(RemDate);
        RTime.setText(RemTime);

        list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, SetReminder.recipients.toArray()));

    }

    class WriteData extends AsyncTask<Void, Void, Void> {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        Calendar calendar = Calendar.getInstance();

        @Override
        protected Void doInBackground(Void... voids) {

            String mins;
            if(calendar.get(Calendar.MINUTE) < 10) {
                mins = "0" + calendar.get(Calendar.MINUTE);
            } else {
                mins = String.valueOf(calendar.get(Calendar.MINUTE));
            }
            String currDate = (calendar.get(Calendar.MONTH) + 1)+"/"+calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.YEAR);
            String currTime = calendar.get(Calendar.HOUR) + ":" + mins;
            SetReminderHelperClass reminder = new SetReminderHelperClass(RemMessage,RemTime,RemDate,SetReminder.recipients,SetReminder.services,currDate,currTime);

            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy:HH:mm:ss");
            Date date = new Date();
            Log.d("BUG", "reminders2: " + formatter.format(date).toString());
            db.getReference("Reminders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(formatter.format(date).toString())
                    .setValue(reminder).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        SetReminder.clearCache();
                        Snackbar.make(findViewById(R.id.confirmedLayout),
                            "Entry was created successfully",
                        Snackbar.LENGTH_SHORT)
                            .addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                finish();
                            }
                        })
                        .show();
                        Upcoming.setUserCache(name);
                    }else{
                        Snackbar.make(findViewById(R.id.confirmedLayout),
                    "Issue with creating entry",
                    Snackbar.LENGTH_SHORT)
                    .addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);
                            finish();
                        }
                    })
                    .show();
                    }
                }
            });
            return null;
        }
    }

}