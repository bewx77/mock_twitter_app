package com.betheng.clonetwitterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        ListView feedListView = findViewById(R.id.feedListView);

        ArrayList<Map<String, String>> tweetData = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweet");
        query.whereContainedIn("username",ParseUser.getCurrentUser().getList("isFollowing"));
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){
                    for (ParseObject object: objects){
                        Map<String, String> tweetInfo = new HashMap<>();
                        tweetInfo.put("username", object.getString("username"));
                        tweetInfo.put("tweet", object.getString("tweet"));
                        tweetData.add(tweetInfo);
                    }
                } else {
                    e.printStackTrace();
                }
                SimpleAdapter arrayAdapter = new SimpleAdapter(getApplicationContext(), tweetData, android.R.layout.simple_list_item_2,new String[] {"username", "tweet"}, new int[]{android.R.id.text1, android.R.id.text2});
                feedListView.setAdapter(arrayAdapter);
            }
        });
    }
}