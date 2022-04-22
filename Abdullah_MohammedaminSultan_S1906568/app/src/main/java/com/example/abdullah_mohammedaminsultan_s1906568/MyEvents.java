package com.example.abdullah_mohammedaminsultan_s1906568;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MyEvents extends AppCompatActivity {

    private EventAdapter adapter;
    private RecyclerView rv;
    CheckChange c = new CheckChange();
    private ArrayList<mymodel> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        data = new ArrayList<>();
        rv = findViewById(R.id.list);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder builder = new AlertDialog.Builder(MyEvents.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(MyEvents.this).inflate(R.layout.content, viewGroup, false);

                TextView saveBtn = dialogView.findViewById(R.id.transfer_btn);
                TextView cancelBtn = dialogView.findViewById(R.id.cancel_btn);
                TextView titleEt = dialogView.findViewById(R.id.title_et);
                TextView descEt = dialogView.findViewById(R.id.description_et);
                LinearLayout dateTv = dialogView.findViewById(R.id.date_tv);
                TextView dateTv2 = dialogView.findViewById(R.id.date_tv2);


                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                        String title = titleEt.getText().toString().trim();
                        String desc = descEt.getText().toString().trim();
                        String date = dateTv2.getText().toString().trim();
                        if (TextUtils.isEmpty(title)) {
                            titleEt.setError("Enter title");
                            titleEt.requestFocus();
                        } else if (TextUtils.isEmpty(desc)) {
                            descEt.setError("Enter description");
                            descEt.requestFocus();
                        } else if (TextUtils.isEmpty(date)) {
                            Toast.makeText(MyEvents.this, "Select date..", Toast.LENGTH_SHORT).show();
                        } else {
                            mymodel event = new mymodel(title, desc, date);
                            data.add(event);


                            adapter = new EventAdapter(data, MyEvents.this);
                            rv.setNestedScrollingEnabled(false);
                            rv.setLayoutManager(new LinearLayoutManager(MyEvents.this));
                            rv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

                dateTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar calendar = Calendar.getInstance();
                        int yy = calendar.get(Calendar.YEAR);
                        int mm = calendar.get(Calendar.MONTH);
                        int dd = calendar.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePicker = new DatePickerDialog(MyEvents.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String date = String.valueOf(dayOfMonth) + "." + String.valueOf(monthOfYear + 1)
                                        + "." + String.valueOf(year);
                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.DAY_OF_MONTH, view.getDayOfMonth());
                                cal.set(Calendar.MONTH, view.getMonth());
                                cal.set(Calendar.YEAR, view.getYear());
                                cal.set(Calendar.HOUR_OF_DAY, 0);
                                cal.set(Calendar.MINUTE, 0);
                                cal.set(Calendar.SECOND, 0);

                                long startDateInMili = cal.getTimeInMillis();
                                Log.d("selectTimeInmili", startDateInMili + "");
                                String dateString = getDate(startDateInMili, "EE, dd MMM yyyy");

                                dateTv2.setText(dateString);

                            }
                        }, yy, mm, dd);
                        datePicker.show();

                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

    }

    public static String getDate(long milliSeconds, String dateFormat) {

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    @Override
    protected void onStart() {
        IntentFilter f = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(c,f);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(c);
        super.onStop();
    }
}