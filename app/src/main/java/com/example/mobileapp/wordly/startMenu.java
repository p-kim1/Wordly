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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class startMenu extends AppCompatActivity {
    private Context appContext;
    private WordGame game = null;
    private String randomStart = null;
    private String randomEnd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);


        appContext = getApplicationContext();
        UIenabled(false);
        TextView tv = (TextView) findViewById(R.id.startMenu_TextView_initializing);
        Animation a = AnimationUtils.loadAnimation(appContext, R.anim.blink);
        tv.setAnimation(a);
        tv.animate();
        buildGame bg = new buildGame();
        bg.execute();

        // Show welcome screen.
        Intent intent = new Intent(this, WelcomeScreen.class);
        startActivity(intent);
    }

    public void startGame(View v)
    {
        // Ensure game is playable.

        game.resetGame();
        EditText etStart = (EditText) findViewById(R.id.startMenu_EditText_fromWord);
        EditText etEnd = (EditText) findViewById(R.id.startMenu_EditText_toWord);
        String startWord = etStart.getText().toString();
        String endWord = etEnd.getText().toString();
        if(!randomStart.equals(startWord) || !randomEnd.equals(endWord))
            game.setPath(startWord, endWord,9 );
        if(game.getPathSize() <= 2)
        {
            Toast toast = Toast.makeText(this, "The path is too short.", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(startWord.equals(game.getCurrentWord()) && endWord.equals(game.getTargetWord())) {
            Intent i = new Intent(this, inGame.class);
            startActivity(i);
        }
        else{
            Toast toast = Toast.makeText(this, "No path exists.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void UIenabled(Boolean b)
    {
        EditText etStart = (EditText) findViewById(R.id.startMenu_EditText_fromWord);
        EditText etEnd = (EditText) findViewById(R.id.startMenu_EditText_toWord);
        Button np = (Button) findViewById(R.id.startMenu_Button_newPuzzle);
        Button play = (Button) findViewById(R.id.startMenu_Button_play);
        np.setEnabled(b);
        play.setEnabled(b);
        etStart.setEnabled(b);
        etEnd.setEnabled(b);
    }

    public void newPuzzle(View v)
    {
        Random r = new Random();
        game.newGame(r.nextInt(4)+4);
        randomStart = game.getCurrentWord();
        randomEnd = game.getTargetWord();
        EditText etStart = (EditText) findViewById(R.id.startMenu_EditText_fromWord);
        EditText etEnd = (EditText) findViewById(R.id.startMenu_EditText_toWord);
        etStart.setText(randomStart);
        etEnd.setText(randomEnd);
    }

    public class buildGame extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... args) {
            game = WordGame.getInstance(appContext);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            newPuzzle(null);
            TextView tv = (TextView) findViewById(R.id.startMenu_TextView_initializing);
            tv.setVisibility(View.INVISIBLE);
            tv.clearAnimation();
            UIenabled(true);
        }

    }


    /*
    public void playGame(View v){
        Intent startGame = new Intent(startMenu.this, inGame.class);
        startActivity(startGame);
    }
    */

}
