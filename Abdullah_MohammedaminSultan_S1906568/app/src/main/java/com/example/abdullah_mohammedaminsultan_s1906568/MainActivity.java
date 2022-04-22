package com.example.abdullah_mohammedaminsultan_s1906568;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    ProgressBar p;

    CheckChange c = new CheckChange();
    private ArrayList<RssFeedModel> mFeedModelList;
    RssFeedListAdapter adapter;

    private Button startButton;

    SearchView ss;
    SearchView searchView;
    private int year, month, day;
    ImageButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ss = findViewById(R.id.ss);
        btn =findViewById(R.id.btn);
        startButton = (Button)findViewById(R.id.startButton);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        p = findViewById(R.id.progress);
        searchView = findViewById(R.id.searchView);
        mFeedModelList = new ArrayList<RssFeedModel>();
        adapter = new RssFeedListAdapter(mFeedModelList, MainActivity.this);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(p.getVisibility() == View.INVISIBLE){
                    p.setVisibility(View.VISIBLE);                      //Progress Bar making Visible
                    new FetchFeedTask().execute((Void) null);           //Calling The FetchFeed class
                }
            }
        });
        findViewById(R.id.Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyEvents.class));
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();                                              //Opening The Date Dialog to filter the search
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ss.setImeOptions(EditorInfo.IME_ACTION_DONE);
        ss.setQueryHint("Search By Road");
        ss.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.performFiltering(newText);
                return false;
            }
        });
        int orientation = MainActivity.this.getResources().getConfiguration().orientation;      //This is to get the orientation of screen
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {                        //Changing the screen orientation accordingly
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void setDate() {
        showDialog(2);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {                               // Date Dialog
        if (id == 2) {
            Calendar c = Calendar.getInstance();
            c.set(2022, 0, 1);                             //Putting a start date
            DatePickerDialog dialog = new DatePickerDialog(this, myDateListener, year, month, day);
            dialog.getDatePicker().setMinDate(c.getTimeInMillis());
            return  dialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new         //When the set button in the date picker is pressed
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2+1, arg3);                      //It will send the date to another method
                }
            };

    private void showDate(int year, int month, int day) {
        String text = day +"-" + month + "-" + year;                        //Receive the date and put it on a string
        try {
            adapter.filterss(text);                                           //Adapter of the Recyclerview will be filtered by the given text
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public List<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        String description = null;
        String lon = null;
        String lat = null;
        String road = null;
        String date = null;
        String strt = null;
        String end = null;
        boolean isItem = false;
        List<RssFeedModel> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();                                  //XML PULL PARSER is used to parse the xml file given in the link
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if(name == null)
                    continue;

                if(eventType == XmlPullParser.END_TAG) {                                        //get start and end of a item tag
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {                               // Pass every tag one by one and catch all the data and store it inside a local variable
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("category")) {
                    link = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result;
                } else if (name.equalsIgnoreCase("region")) {
                    date = result;
                } else if (name.equalsIgnoreCase("latitude")) {
                    lat = result;
                }  else if (name.equalsIgnoreCase("road")) {
                    road = result;
                }else if (name.equalsIgnoreCase("longitude")) {
                    lon = result;
                }else if (name.equalsIgnoreCase("overallStart")) {
                    strt = formateDateFromstring("yyyy-MM-dd'T'HH:mm:ss+SS:SS", "d-M-yyyy", result);        // Formatting the dates to a suitable format to compare in search filter
                }else if (name.equalsIgnoreCase("overallEnd")) {
                    end = formateDateFromstring("yyyy-MM-dd'T'HH:mm:ss+SS:SS", "d-M-yyyy", result);
                }

                if (title != null && link != null && description != null && date != null && road != null && lat != null && lon != null && strt != null && end != null) { //Check if any of the variable is empty or not
                    if(isItem) {
                        RssFeedModel item = new RssFeedModel(title, link, description, date, lon, lat, road, strt, end);                                          // Adding all the variable to the rss model class
                        items.add(item);
                    }

                    title = null;                           //Making All the variables empty
                    link = null;
                    description = null;
                    road = null;
                    lat = null;
                    lon = null;
                    date = null;
                    strt = null;
                    end = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }
    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){          //Date Formatting method

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }

        return outputDate;

    }
    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {            // Class to visit the given link and get the feed

        private String urlLink;

        @Override
        protected void onPreExecute() {
            urlLink = "http://m.highwaysengland.co.uk/feeds/rss/AllEvents.xml";     //Saving the link in a String
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                URL url = new URL(urlLink);                                         // Making the Sting a url
                InputStream inputStream = url.openConnection().getInputStream();
                mFeedModelList = (ArrayList<RssFeedModel>) parseFeed(inputStream);  // Saving the feeds inside the arraylist
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {                                      //When its success is true then
                p.setVisibility(View.INVISIBLE);                            //Make Hidden things visible
                searchView.setVisibility(View.VISIBLE);
                btn.setVisibility(View.VISIBLE);
                ss.setVisibility(View.VISIBLE);
                adapter = new RssFeedListAdapter(mFeedModelList, MainActivity.this);        //Setting the list to the adapter
                mRecyclerView.setAdapter(adapter);                                                  // setting the adapter to the list
            }
        }
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
