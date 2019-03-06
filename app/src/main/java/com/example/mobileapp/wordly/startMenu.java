package com.example.mobileapp.wordly;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class startMenu extends AppCompatActivity {

    private static Context appContext;
    protected static WordGame game = null;
    protected static int puzzleSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);


        appContext = getApplicationContext();
        buttonsEnabled(false);
        TextView tv = (TextView) findViewById(R.id.startMenu_TextView_initializing);
        Animation a = AnimationUtils.loadAnimation(appContext, R.anim.blink);
        tv.setAnimation(a);
        tv.animate();
        buildGame bg = new buildGame();
        bg.execute();
    }

    public void startGame(View v)
    {
        Intent i = new Intent(this, inGame.class);
        //i.putExtra("path",game);
        startActivity(i);
    }

    public void buttonsEnabled(Boolean b)
    {
        Button np = (Button) findViewById(R.id.startMenu_Button_newPuzzle);
        Button play = (Button) findViewById(R.id.startMenu_Button_play);
        np.setEnabled(b);
        play.setEnabled(b);
    }


    public void newPuzzle(View v)
    {
        Random r = new Random();
        game.newGame(r.nextInt(4)+4);
        puzzleSize = game.getCurrentPath().size();
        String startWord = game.getCurrentWord();
        String endWord = game.getCurrentPath().get(puzzleSize-1);
        TextView tvStart = (TextView) findViewById(R.id.startMenu_TextView_fromWord);
        TextView tvEnd = (TextView) findViewById(R.id.startMenu_TextView_toWord);
        tvStart.setText(startWord);
        tvEnd.setText(endWord);
    }

    public class buildGame extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... args) {
            game = new WordGame(appContext);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            newPuzzle(null);
            TextView tv = (TextView) findViewById(R.id.startMenu_TextView_initializing);
            tv.setVisibility(View.INVISIBLE);
            tv.clearAnimation();
            buttonsEnabled(true);
        }

    }


    /*
    public void playGame(View v){
        Intent startGame = new Intent(startMenu.this, inGame.class);
        startActivity(startGame);
    }
    */

}
