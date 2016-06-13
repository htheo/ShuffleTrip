package com.shuffle.theo.shuffletrip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class SingleArticle extends AppCompatActivity implements OnClickListener {

// Swipe
    float x1,x2;
    float y1, y2;

    public ImageButton home;
    public ImageButton search;
    public ImageButton user;
    public Button button_next;

    // Récupération de l'image
    private static final String SERVER_ADRESS = "http://timothee-dorand.fr/shuffletrip/";
    public ImageView img_single_article;
    public String image;

    private TextView text_id;
    private TextView title_view;
    private TextView title_ville;
    String myJSON;
    JSONObject jsonObj = null;
    String title;

    private static final String TAG_RESULTS="output";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DESCRIB = "describ";
    private static final String TAG_VILLE ="ville";
    private static final String TAG_IMAGE ="iamge";

    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_article);

        home = (ImageButton) findViewById(R.id.home);
        user = (ImageButton) findViewById(R.id.user);
        search = (ImageButton) findViewById(R.id.search);
        text_id = (TextView) findViewById(R.id.title_article);
        title_view = (TextView) findViewById(R.id.title);
        title_ville = (TextView) findViewById(R.id.title_ville);
//        button_next = (Button) findViewById(R.id.button_next);
        Button buttonNext = (Button)findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                Toast.makeText(SingleArticle.this, "Suivant", Toast.LENGTH_SHORT).show();
//                Intent Intent = new Intent(this, SingleArticle.class);
                Intent Intent = getIntent();
                finish();
                String ville = title_ville.getText().toString();

                Intent.putExtra("id", -1);
                Intent.putExtra("ville", ville);
                startActivity(getIntent());

//                this.startActivity(SingleArticle);
            }
        });

        InputStream is = null;
        StringBuilder sb=null;
        home.setOnClickListener(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String ville_choisi = intent.getStringExtra("ville");

        img_single_article = (ImageView) findViewById(R.id.img_single_article);


        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("http://timothee-dorand.fr/shuffletrip/show_articles?ville="+ville_choisi+"ID="+id);

        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);

                JSONArray arr = new JSONArray(result);
                JSONObject jObj = arr.getJSONObject(0);
                String title = jObj.optString("title");
                String ville = jObj.optString("ville");
                String describ = jObj.optString("describ");
                String image = jObj.optString("image");

                if(image != null) {
                    new DownloadImage(image).execute();
                }
                title_view.setText(title);
                title_ville.setText(ville);
                text_id.setText(describ);
                Toast.makeText(this, title, Toast.LENGTH_LONG).show();
                instream.close();

            }

        } catch (Exception e) {
            Log.e("Error",e.toString());
        }
    }

    private class DownloadImage extends AsyncTask<Void, Void, Bitmap> {

        String name;

        public DownloadImage(String name){
            this.name = name;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            String url = SERVER_ADRESS + "images/" + name;

            try {
                URLConnection connection = new URL(url).openConnection();
                connection.setConnectTimeout(1000 * 30);
                connection.setReadTimeout(1000 * 30);

                return BitmapFactory.decodeStream((InputStream) connection.getContent(), null, null);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(null != bitmap) {
                img_single_article.setImageBitmap(bitmap);
            }
        }
    }




    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


        // Swipe vers la droite pour accéder à un autre post dans la même ville
        // Si pas de post dans la même ville -> Toast: Pas de post trouvé à proximité -> random

    // onTouchEvent () method gets called when User performs any touch event on screen
    // Method to handle touch event like left to right swap and right to left swap

    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN:
            {
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                x2 = touchevent.getX();
                y2 = touchevent.getY();

                // if left to right sweep event on screen
                if (x1 < x2)
                {
                    Toast.makeText(this, "Left to Right Swap Performed", Toast.LENGTH_SHORT).show();
                    Intent Intent = getIntent();
                    finish();
                    String ville = title_ville.getText().toString();

                    Intent.putExtra("id", -1);
                    Intent.putExtra("ville", ville);
                    startActivity(getIntent());
                }

                // if right to left sweep event on screen
                if (x1 > x2)
                {
                    Toast.makeText(this, "Right to Left Swap Performed", Toast.LENGTH_SHORT).show();
                    Intent Intent = getIntent();
                    finish();
                    String ville = title_ville.getText().toString();

                    Intent.putExtra("id", -1);
                    Intent.putExtra("ville", ville);
                    startActivity(getIntent());
                }

                // if UP to Down sweep event on screen
                if (y1 < y2)
                {
                    Toast.makeText(this, "UP to Down Swap Performed", Toast.LENGTH_LONG).show();
                }

                // if Down to UP sweep event on screen
                if (y1 > y2)
                {
                    Toast.makeText(this, "Down to UP Swap Performed", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
        return false;
    }



    public void onClick(View v) {
        if (v == home) {  //si on va sur l'accueil
            Intent I_News = new Intent(SingleArticle.this, MainActivity.class);
            this.startActivity(I_News);
        }
        if (v == search) {  //si on va sur l'accueil
            Intent I_News = new Intent(SingleArticle.this, SearchActivity.class);
            this.startActivity(I_News);
        }
        if (v == user) {  //si on va sur l'accueil
            Intent I_News = new Intent(SingleArticle.this, LoginActivity.class);
            this.startActivity(I_News);
        }


    }
}