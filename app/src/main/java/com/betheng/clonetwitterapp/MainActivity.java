package com.betheng.clonetwitterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseObject;

public class MainActivity extends AppCompatActivity {
    boolean isLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView titleTextView = findViewById(R.id.titleTextView);
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        TextView questionTextView = findViewById(R.id.questionTextView);
        TextView changeStateTextView = findViewById(R.id.changeStateTextView);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
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