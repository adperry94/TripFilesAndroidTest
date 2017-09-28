package com.example.andrew.tripfilesandroidtest;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import static android.R.attr.format;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    private ProgressDialog pDialog;
    private static String url = "http://www.gregframe.com/pubimage/tfDataExample.php";
    //private static String url = "https://api.androidhive.info/contacts/";
    ArrayList<HashMap<String, String>> contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#46aadd"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new GetContacts().execute();

    }
    private class GetContacts extends AsyncTask<Void, Void, Void> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            URLConnector sh = new URLConnector();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray images = jsonObj.getJSONArray("Images");

                    // looping through All Contacts
                    for (int i = 0; i < images.length(); i++) {
                        JSONObject c = images.getJSONObject(i);

                        String id = c.getString("ImageId");
                        String epochdate = c.getString("ChangeDate");
                        Date date = new Date(Integer.parseInt(epochdate) * 1000L);
                        DateFormat format = new SimpleDateFormat("MMMM dd, yyyy");
                        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
                        String dates = format.format(date);
                        String day = new SimpleDateFormat("EE").format(date);
                        String name = c.getString("Title");
                        String imageTemplate = c.getString("ImageTemplate");
                        String image = "https://qa-tripfiles-s3.imgix.net"+imageTemplate;
                        String comments = c.getString("CommentsCount");
                        String comment = comments+" Comments";
                        String likes = c.getString("LikesCount");
                        String like = likes+" Likes";
                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();
                        String test = "0 Comments";
                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("date", dates);
                        contact.put("day", day);
                        contact.put("name", name);
                        contact.put("imageTemplate", image);
                        contact.put("comments", test);
                        contact.put("likes", like);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

            CustomList adapt = new CustomList(MainActivity.this, contactList,
                    R.layout.image_layout, new String[]{"date", "day", "imageTemplate",
                    "comments", "likes"}, new int[]{R.id.labelDetail, R.id.day,
                    R.id.imgDetail, R.id.comments, R.id.likes});
            lv.setAdapter(adapt);
        }

    }
}
