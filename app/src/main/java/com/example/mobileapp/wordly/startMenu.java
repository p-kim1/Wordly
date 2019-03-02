package com.example.mobileapp.wordly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

public class startMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

    }


    public void playGame(View v){
        Intent startGame = new Intent(startMenu.this, inGame.class);
        startActivity(startGame);
    }

}
