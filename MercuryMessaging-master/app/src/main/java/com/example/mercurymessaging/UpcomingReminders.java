package com.example.mercurymessaging;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mercurymessaging.data.SetReminderHelperClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpcomingReminders extends AppCompatActivity {

    List<String> values;
    ListView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_reminders);
        values = new ArrayList<String>();
        view = findViewById(R.id.list);
        // fetch data if user set
        view.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,values));
        new getData().execute();

    }

    class getData extends AsyncTask<Void, SetReminderHelperClass, List<SetReminderHelperClass>>{
        private ArrayAdapter<String> adapter;
        @Override
        protected void onPreExecute() {
            adapter = (ArrayAdapter<String>) view.getAdapter();
        }
        @Override
        protected List<SetReminderHelperClass> doInBackground(Void... voids) {
            List<SetReminderHelperClass> data = new ArrayList<SetReminderHelperClass>();
            System.out.println("In Background task");

            Calendar calendar1 = Calendar.getInstance();
            SimpleDateFormat formatter1 = new SimpleDateFormat("M/dd/yyyy h:mm aa");
            String currentDate = formatter1.format(calendar1.getTime());
            Log.d("currentDate", currentDate);


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Reminders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot datas: dataSnapshot.getChildren()){
                        String message=datas.child("message").getValue().toString();
                        String date=datas.child("date").getValue().toString();
                        String timeData=datas.child("time").getValue().toString();
                        List<String> recepients = (List<String>) datas.child("receipnts").getValue();
                        List<String> services = (List<String>) datas.child("services").getValue();
                        String curDate = datas.child("currentDate").getValue().toString();
                        String curTime = datas.child("currentTime").getValue().toString();
                        System.out.println(message);
                        SetReminderHelperClass obj = new SetReminderHelperClass(message,timeData,date,recepients,services,curDate,curTime);

                        if(currentDate.compareTo(date+" "+timeData)<=0){
                            publishProgress(obj);
                            data.add(obj);
                        }
                   }
               }
               @Override
               public void onCancelled(DatabaseError databaseError) {
               }
            });
            if(data.size()==0){

            }
            return data;
        }
        @Override
        protected void onPostExecute(List<SetReminderHelperClass> object) {
            //super.onPostExecute(aVoid);
            view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    Intent i = new Intent(UpcomingReminders.this, Upcoming.class);
                    Bundle b = new Bundle();
                    System.out.println(position);

                    b.putString("Message", object.get(position).getMessage());
                    b.putString("Date", object.get(position).getDate());
                    b.putString("time", object.get(position).getTime());
                    b.putStringArrayList("receipents", (ArrayList<String>) object.get(position).getReceipnts());
                    b.putStringArrayList("services", (ArrayList<String>) object.get(position).getServices());
                    i.putExtras(b);
                    startActivity(i);
                }

            });
        }

        @Override
        protected void onProgressUpdate(SetReminderHelperClass... values) {
            adapter.add(values[0].getMessage());
        }
    }
}