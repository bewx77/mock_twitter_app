package com.betheng.clonetwitterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {
    boolean isLogin = true;

    private void gotoUserListActivity(){
        if (ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(this, UsersListActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gotoUserListActivity();

        TextView titleTextView = findViewById(R.id.titleTextView);
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        TextView questionTextView = findViewById(R.id.questionTextView);
        TextView changeStateTextView = findViewById(R.id.changeStateTextView);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (isLogin){
                    if (username != null && password != null){
                        ParseUser.logInInBackground(username, password, new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if (e == null){
                                    Log.i("Info","Login Success");
                                    Toast.makeText(getApplicationContext(),"Login Successfully",Toast.LENGTH_SHORT).show();
                                    gotoUserListActivity();
                                } else {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(),e.getMessage().substring(e.getMessage().indexOf(" ")),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Username or Password cannot be empty",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ParseUser user = new ParseUser();
                    user.setUsername(username);
                    user.setPassword(password);

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){
                                Log.i("Info","Sign Up Success");
                                Toast.makeText(getApplicationContext(),"Sign Up Successfully",Toast.LENGTH_SHORT).show();
                                gotoUserListActivity();
                            } else {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),e.getMessage().substring(e.getMessage().indexOf(" ")),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        changeStateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin){
                    isLogin = false;
                    titleTextView.setText("Sign Up for Twitter");
                    questionTextView.setText("Already have an account?");
                    changeStateTextView.setText("Log In >");
                    button.setText("Sign Up");
                } else {
                    isLogin = true;
                    titleTextView.setText("Log in to Twitter");
                    questionTextView.setText("Don't have an account?");
                    changeStateTextView.setText("Sign Up >");
                    button.setText("Log in");
                }
            }
        });



    }
}