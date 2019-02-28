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
import android.util.Log;
import android.widget.ImageView;

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

import javax.net.ssl.HttpsURLConnection;

public class inGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);
        //getHintImage("pizza");
    }

    public void getHintImage(String word)
    {
        String URLstr = "https://pixabay.com/images/search/" + word;
        getURL search = new getURL();
        search.execute(URLstr);
    }

    public class getURL extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... urlstring)
        {
            try {
                URL url = new URL(urlstring[0]);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                ArrayList<String> imageURLs = new ArrayList<>();
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String inputLine, imageLink;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.contains("Related Images")) {
                        int startIndex = inputLine.indexOf("https");
                        int endIndex = Math.min(inputLine.indexOf(".jpg"),inputLine.indexOf(".png"));
                        while (endIndex >= 0) {
                            imageLink = inputLine.substring(startIndex,endIndex+4);
                            if(imageLink.contains("480."))
                                imageURLs.add(imageLink);
                            startIndex = inputLine.indexOf("https", startIndex + 1);
                            endIndex = Math.min(inputLine.indexOf(".jpg", endIndex + 1),inputLine.indexOf(".png", endIndex + 1));
                        }
                        break;
                    }
                }
                in.close();
                Random r = new Random();
                String randomImageURL = imageURLs.get(r.nextInt(imageURLs.size()));
                setImage st = new setImage();
                System.out.println(randomImageURL);
                st.execute(randomImageURL);
            }
            catch(MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch(IOException io)
            {
                io.printStackTrace();
            }
            return null;
        }
    }

    public class setImage extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... urlstring)
        {
            Bitmap img = null;
            try {
                URL url = new URL(urlstring[0]);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = new BufferedInputStream(url.openStream());

                byte[] b = new byte[1024];
                int n  = 0;
                while(-1!=(n=in.read(b)))
                {
                    out.write(b,0,n);
                }
                out.close();
                in.close();
                byte[] r = out.toByteArray();
                img = BitmapFactory.decodeByteArray(r, 0, r.length);

            }
            catch(MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch(IOException io)
            {
                io.printStackTrace();
            }
            return img;
        }
        protected void onPostExecute(Bitmap out)
        {
            ImageView iv = (ImageView)findViewById(R.id.hint_image);
            iv.setImageBitmap(out);
        }
    }
}
