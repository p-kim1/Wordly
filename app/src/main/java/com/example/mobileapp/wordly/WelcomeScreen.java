package com.example.mobileapp.wordly;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class WelcomeScreen extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        ConstraintLayout constraintLayout = findViewById(R.id.WelcomeScreen_constraintLayout);
        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return true;
            }
        });

        TextView textView = findViewById(R.id.WelcomeScreen_textView_description);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setText(R.string.welcome_screen);
    }
}
