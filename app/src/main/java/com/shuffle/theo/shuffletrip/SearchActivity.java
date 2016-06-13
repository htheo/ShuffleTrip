package com.shuffle.theo.shuffletrip;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity implements OnClickListener {
    protected ImageButton home;
    protected ImageButton search;
    protected ImageButton user;
    private String single_article_id;
    public LinearLayout layout;
    public String pseudo;
    public ImageView single_article;
    public int resID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.home = (ImageButton) findViewById(R.id.home);
        this.user = (ImageButton) findViewById(R.id.user);

        home.setOnClickListener(this);
        user.setOnClickListener(this);
        Intent intent = getIntent();
        if((intent.getStringExtra("pseudo") != null)&&(!"false".equals(intent.getStringExtra("pseudo")))){
            pseudo = intent.getStringExtra("pseudo");
        }else{
            pseudo="false";
        }

        Log.e("pseudo", pseudo);
        new AsyncTaskParseJson().execute();



    }





    public void onClick(View v) {
        if (v == home) {  //si on va sur l'accueil
            Intent I_News = new Intent(SearchActivity.this, MainActivity.class);
            I_News.putExtra("pseudo", pseudo);
            this.startActivity(I_News);
        }

        if (v == user) {  //si on va sur l'accueil
            Intent I_News = new Intent(SearchActivity.this, LoginActivity.class);
            I_News.putExtra("pseudo", pseudo);
            this.startActivity(I_News);
        }


    }

    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";
        // set your json string url here


        // contacts JSONArray
        JSONArray dataJsonArr = null;

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... args) {
            String yourJsonStringUrl = "http://timothee-dorand.fr/shuffletrip/show_articles?ID=1";
            Log.e(TAG, "je passe par la");
            try {

                // instantiate our json parser
                JsonParser jParser = new JsonParser();

                // get json string from url
                JSONObject json = jParser.getJSONFromUrl(yourJsonStringUrl);

                // get the array of users
                dataJsonArr = json.getJSONArray("Articles");

                // loop through all users
                for (int i = 0; i < dataJsonArr.length(); i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);

                    String ville = c.getString("ville");
                    String describ = c.getString("describ");

                    // show the values in our logcat
                    /*single_article_id = "single_article"+i;
                    int id = getResources().getIdentifier(single_article_id, "id", getPackageName());
                    single_article = (ImageView) findViewById(id);


                    single_article.setImageDrawable(R.drawable.chateau);*/



                    //single_article.setText(ville);
                    Log.e(TAG, "ID: " + ville);
                    Log.e(TAG, "ID: " + describ);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {



        }

    }



}
