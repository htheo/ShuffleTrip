package com.shuffle.theo.shuffletrip;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
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
import java.util.ArrayList;
import java.util.HashMap;

public class SingleArticle extends AppCompatActivity implements OnClickListener {
    public ImageButton home;
    public ImageButton search;
    public ImageButton user;
    private TextView text_id;
    private TextView title_view;
    private TextView title_ville;String myJSON;
    JSONObject jsonObj = null;
    String title;

    private static final String TAG_RESULTS="output";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DESCRIB = "describ";
    private static final String TAG_VILLE ="ville";

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

        InputStream is = null;
        StringBuilder sb=null;
        home.setOnClickListener(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String ville_choisi = intent.getStringExtra("ville");



        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("http://theo-hinfray.fr/IIM/ShuffleTrip/show_article?ville="+ville_choisi+"ID="+id);

        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);

                JSONArray arr = new JSONArray(result);
                JSONObject jObj = arr.getJSONObject(0);
                String title = jObj.getString("title");
                String ville = jObj.getString("ville");
                String describ = jObj.getString("describ");
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