package ru.aginf.schoolringerver3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    SharedPreferences mSharedPref;

    String[] names = {"8:10", "8:50", "9:00", "9:40", "9:50", "10:30",
            "10:40", "11:20", "11:30", "12:10", "12:30", "13.10", "13:20", "14:00", "14:10", "14:50","15:00","15:40"};

    ArrayList arrayMainList = new ArrayList();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listRaspUrokov = (ListView) findViewById(R.id.listRaspUrokov);
        registerForContextMenu(listRaspUrokov);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrayMainList);


        listRaspUrokov.setAdapter(adapter);



        listRaspUrokov.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(9)
                        .setMinute(0)
                        .setTitleText("Задайте время урока")
                        .build();

                materialTimePicker.addOnPositiveButtonClickListener(viewpic -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(calendar.SECOND,0);
                    calendar.set(calendar.MILLISECOND,0);
                    calendar.set(calendar.MINUTE, materialTimePicker.getMinute());
                    calendar.set(calendar.HOUR_OF_DAY, materialTimePicker.getHour());
                    Toast.makeText(getApplicationContext(), "Звонок установлен:  <"+id+"> "+materialTimePicker.getHour()+":"+materialTimePicker.getMinute(), Toast.LENGTH_SHORT).show();

                    String txtTime = materialTimePicker.getHour()+":"+materialTimePicker.getMinute();
                    names[(int) id] = txtTime;
                    ((TextView)view).setText(txtTime);
                });
                materialTimePicker.show(getSupportFragmentManager(),"tag_picker");


            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
    @Override
    public boolean onContextItemSelected (MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.edit:
                editZv(info.position);
                return true;
            case R.id.delete:
                deleteZv(info.position);
                return  true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    public void editZv(int id){

    }

    public void deleteZv(int id){

    }
    public void setZvonki(String str) {
        //AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager;
        int id = -1;
        for (String val : names) {
            id++;
            PendingIntent pi = getAlarmInfoPendingIntent(id);
            pi.cancel();

            if (val == "")
                continue;

            int hTime = getHoureFromText(val);
            int mTime = getMinuteFromText(val);
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.SECOND,0);
            calendar.set(calendar.MILLISECOND,0);
            calendar.set(calendar.MINUTE, mTime);
            calendar.set(calendar.HOUR_OF_DAY, hTime);

            pi = getAlarmInfoPendingIntent(id);
            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), pi);
        }
    }

    public void onNewZv(View view){
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(9)
                .setMinute(0)
                .setTitleText("Задайте время урока")
                .build();

        materialTimePicker.addOnPositiveButtonClickListener(viewpic -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.SECOND,0);
            calendar.set(calendar.MILLISECOND,0);
            calendar.set(calendar.MINUTE, materialTimePicker.getMinute());
            calendar.set(calendar.HOUR_OF_DAY, materialTimePicker.getHour());
            Toast.makeText(getApplicationContext(), "Звонок установлен:  "+materialTimePicker.getHour()+":"+materialTimePicker.getMinute(), Toast.LENGTH_SHORT).show();

            String txtTime = materialTimePicker.getHour()+":"+materialTimePicker.getMinute();
            arrayMainList.add(txtTime);
            adapter.notifyDataSetChanged();
            //((TextView)view).setText(txtTime);
        });
        materialTimePicker.show(getSupportFragmentManager(),"tag_picker");

    }

    private PendingIntent getAlarmInfoPendingIntent(int id){
        Intent alarmInfoIntent = new Intent(this, MainActivity.class);
        alarmInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        return PendingIntent.getActivity(this, id, alarmInfoIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public String setDefaultVal(String str) {
        if(str==null||str.equals("")){
            return "00:00";
        }else{
            return str;
        }

    }

    public Integer getMinuteFromText(String str) {
        String m = "0";
        try {

            if (str.substring(2, 3).equals(":")) {
                m = str.substring(3);
            } else if (str.substring(1, 2).equals(":")) {
                m = str.substring(2);
            } else {
                m = "0";
            }
        }
        finally {
            return Integer.parseInt(m);
        }
    }

    public Integer getHoureFromText(String str) {
        String h = "0";

        try {


            if (str.substring(2, 3).equals(":")) {
                h = str.substring(0, 2);
            } else if (str.substring(1, 2).equals(":")) {
                h = str.substring(0, 1);
            } else {
                h = "0";
            }
        }
        finally {
            return Integer.parseInt(h);
        }

    }

    public void load() {
        //names= new String[];

        mSharedPref = getPreferences(MODE_PRIVATE);
        int countZv = Integer.parseInt(mSharedPref.getString("countZv", ""));
        for (int k=0; k < countZv; k++) {

        }


    }

}