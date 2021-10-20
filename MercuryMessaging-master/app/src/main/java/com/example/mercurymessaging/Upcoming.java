package com.example.mercurymessaging;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class Upcoming extends AppCompatActivity {
    private static String userCache = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);
        System.out.println("In mainActivity1");

        Intent i = getIntent();
        Bundle bundle = getIntent().getExtras();
            // fetch data if user set
            System.out.println("In mainActivity");

                        TextView text = findViewById(R.id.reminder);
                        text.setText(bundle.getString("Message"));
                        TextView time = findViewById(R.id.time);
                        time.setText(bundle.getString("Date"));
                        time.setVisibility(View.VISIBLE);
                        TextView msg = findViewById(R.id.message);
                        msg.setText(bundle.getString("time"));
                        msg.setVisibility(View.VISIBLE);
                        ListView receipients = findViewById(R.id.recipients);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, bundle.getStringArrayList("receipents"));
        receipients.setAdapter(adapter);
        receipients.setVisibility(View.VISIBLE);



//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            db.collection("Users").document(userCache).get()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                Map<String, Object> user = document.getData();
//                                TextView text = findViewById(R.id.reminder);
//                                text.setText(user.get("AppointmentDate").toString());
//                                TextView time = findViewById(R.id.time);
//                                time.setText(user.get("TimeScheduled").toString());
//                                time.setVisibility(View.VISIBLE);
//                                TextView msg = findViewById(R.id.message);
//                                msg.setText(user.get("message").toString());
//                                msg.setVisibility(View.VISIBLE);
//                                TextView recipients = findViewById(R.id.recipients);
//                                recipients.setText(user.get("recipients").toString());
//                                recipients.setVisibility(View.VISIBLE);
//                            } else {
//                                Log.d("BUG", "No such document");
//                            }
//                        } else {
//                            Log.d("BUG", "get failed with ", task.getException());
//                        }
//                    });
        }


    public static void setUserCache(String s) {
        userCache = s;
    }

}
