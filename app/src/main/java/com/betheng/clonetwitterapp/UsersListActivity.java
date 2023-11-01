package com.betheng.clonetwitterapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends AppCompatActivity {
    ListView usersListView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> usersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        TextView logOutTextView = findViewById(R.id.logOutTextView);
        Button tweetBtn = findViewById(R.id.tweetButton);

        ListView usersListView = findViewById(R.id.usersListView);
        usersListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, usersList);
        usersListView.setAdapter(arrayAdapter);

        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
        userParseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        userParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null && objects.size() > 0){
                    Log.i("Info", objects.toString());
                    for (ParseUser object : objects){
                        usersList.add(object.getUsername());
                    }
                    arrayAdapter.notifyDataSetChanged();

                    for (String username:usersList){
                        if (ParseUser.getCurrentUser().getList("isFollowing").contains(username)){
                            usersListView.setItemChecked(usersList.indexOf(username), true);
                        }
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView checkedTextView = (CheckedTextView) view;
                if (checkedTextView.isChecked()){
                    Log.i("Info", "Checked");
                    ParseUser.getCurrentUser().add("isFollowing", usersList.get(i));
                } else {
                    Log.i("Info", "NOT Checked");
                    ParseUser.getCurrentUser().getList("isFollowing").remove(usersList.get(i));
                    List tempUsers = ParseUser.getCurrentUser().getList("isFollowing");
                    ParseUser.getCurrentUser().remove("isFollowing");
                    ParseUser.getCurrentUser().put("isFollowing",tempUsers);
                }

                ParseUser.getCurrentUser().saveInBackground();
            }
        });

        logOutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            Log.i("Info","Log Out Success");
                            Toast.makeText(getApplicationContext(),"Logout Successfully",Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        tweetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Info", "Tweet Button Pressed");
                AlertDialog.Builder builder = new AlertDialog.Builder(UsersListActivity.this);
                builder.setTitle("Send a Tweet");
                EditText tweetEditText = new EditText(getApplicationContext());
                builder.setView(tweetEditText);

                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("Info", tweetEditText.getText().toString());

                        ParseObject tweet = new ParseObject("Tweet");
                        tweet.put("username", ParseUser.getCurrentUser().getUsername());
                        tweet.put("tweet", tweetEditText.getText().toString());

                        tweet.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null){
                                    Log.i("Info", "Tweet Success");
                                    Toast.makeText(getApplicationContext(),"Tweet Successfully",Toast.LENGTH_SHORT).show();
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
    }
}