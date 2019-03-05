package com.example.mobileapp.wordly;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class inGame extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    //ArrayList<String> listOfWords = new ArrayList<>();

    private static Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);
        //getHintImage("pizza");

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        WordGame wordGame = new WordGame(this);
        wordGame.newGame(4);
        ArrayList<String> listOfWords = wordGame.getCurrentPath();

        //ArrayList<String> listOfWords

        /*
        listOfWords.add("pie");
        listOfWords.add("fly");
        listOfWords.add("try");
        listOfWords.add("tie");
        listOfWords.add("tye");
        listOfWords.add("lie");
        listOfWords.add("sky");
        listOfWords.add("why");
        listOfWords.add("fry");
        listOfWords.add("rye");
        listOfWords.add("guy");
        listOfWords.add("die");
        listOfWords.add("buy");
        listOfWords.add("my");
        listOfWords.add("sigh");
        listOfWords.add("glide");
        listOfWords.add("delight");
        listOfWords.add("cry");
        listOfWords.add("alibi");
        listOfWords.add("bye");


        ArrayList<String> testList = new ArrayList<>();
        testList.add("gold");
        testList.add("golf");
        testList.add("gulf");

        */

        //listOfWords.add(wordGame.getCurrentPath().toArray());
        //layoutManager = new LinearLayoutManager(this);

        mAdapter = new WordAdapter(listOfWords);
        //mAdapter = new WordAdapter(testList);
        recyclerView.setAdapter(mAdapter);



        appContext = getApplicationContext();
        getHintImage("pizza");

        //WordGame game = new WordGame(appContext,4);
        //System.out.println(game.getCurrentPath());

    }

    public void getHintImage(String word)
    {
        String URLstr = "https://pixabay.com/images/search/" + word;
        getURL search = new getURL();
        search.execute(URLstr);
    }

    public class getURL extends AsyncTask<String, Void, ArrayList<String>> {
        protected void onPreExecute()
        {
            TextView tv = (TextView) findViewById(R.id.inGame_TextView_loading);
            Animation a = AnimationUtils.loadAnimation(appContext, R.anim.blink);
            tv.setAnimation(a);
            tv.animate();
        }

        protected ArrayList<String> doInBackground(String... urlstring)
        {
            ArrayList<String> imageURLs = null;
            try {
                URL url = new URL(urlstring[0]);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                imageURLs = new ArrayList<>();
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String inputLine, imageLink;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.contains("Related Images")) {
                        int startIndex = inputLine.indexOf("https");
                        int endIndex = Math.min(inputLine.indexOf(".jpg"), inputLine.indexOf(".png"));
                        if(endIndex == -1)
                            endIndex = Math.max(inputLine.indexOf(".jpg"), inputLine.indexOf(".png"));
                        while (endIndex >= 0) {
                            imageLink = inputLine.substring(startIndex,endIndex + 4);
                            if(imageLink.contains("480."))
                                imageURLs.add(imageLink);
                            startIndex = inputLine.indexOf("https", startIndex + 1);
                            if(inputLine.contains(".png"))
                                endIndex = Math.min(inputLine.indexOf(".jpg", endIndex + 1), inputLine.indexOf(".png", endIndex+1));
                            else
                                endIndex = inputLine.indexOf(".jpg", endIndex+1);
                        }
                        break;
                    }
                }
                in.close();
            }
            catch(MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch(IOException io)
            {
                io.printStackTrace();
            }
            return imageURLs;
        }
        protected void onPostExecute(final ArrayList<String> imageURLs)
        {
            TimerTask repeatImage = new TimerTask() {
                int i = 0;
                @Override
                public void run() {
                    if(i == imageURLs.size())
                        i = 0;
                    setImage st = new setImage();
                    st.execute(imageURLs.get(i));
                    i++;
                }
            };
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(repeatImage, 0, 4000);
        }
    }

    public class setImage extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... urlstring) {
            Bitmap img = null;
            try {
                URL url = new URL(urlstring[0]);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = new BufferedInputStream(url.openStream());

                byte[] b = new byte[1024];
                int n = 0;
                while (-1 != (n = in.read(b))) {
                    out.write(b, 0, n);
                }
                out.close();
                in.close();
                byte[] r = out.toByteArray();
                img = BitmapFactory.decodeByteArray(r, 0, r.length);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException io) {
                io.printStackTrace();
            }
            return img;
        }

        protected void onPostExecute(Bitmap out) {
            TextView tv = (TextView) findViewById(R.id.inGame_TextView_loading);
            tv.setVisibility(View.INVISIBLE);
            tv.clearAnimation();
            ImageView iv = (ImageView) findViewById(R.id.inGame_ImageView_hint);
            iv.setImageBitmap(out);

            iv.setVisibility(View.VISIBLE);
            Animation a = AnimationUtils.loadAnimation(appContext, R.anim.fade_in);
            iv.setAnimation(a);
            iv.animate();
        }
    }
}
