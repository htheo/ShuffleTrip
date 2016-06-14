package com.shuffle.theo.shuffletrip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class SearchActivity extends AppCompatActivity implements OnClickListener {
        protected ImageButton home;
        protected ImageButton search;
        protected ImageButton user;
        public TextView id_text;
        private String single_article_id;
        public LinearLayout layout;
        public String pseudo;
        public ImageView single_article;
        public int resID;
        private static final String SERVER_ADRESS = "http://timothee-dorand.fr/shuffletrip/";
        public ImageView img_single_article,img_single_article2,img_single_article3,img_single_article4,img_single_article5,img_single_article6,img_single_article7,img_single_article8,img_single_article9;
        public String image;
        public Integer imageNb =1;
        public EditText edittext;
        public Integer id = 1;
        public String id_article = "0";
        public ImageButton search_article;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search);
            id_text = (TextView) findViewById(R.id.id_text);
            img_single_article = (ImageView) findViewById(R.id.single_article1);
            img_single_article2 = (ImageView) findViewById(R.id.single_article2);
            img_single_article3 = (ImageView) findViewById(R.id.single_article3);
            img_single_article4 = (ImageView) findViewById(R.id.single_article4);
            img_single_article5 = (ImageView) findViewById(R.id.single_article5);
            img_single_article6 = (ImageView) findViewById(R.id.single_article6);
            img_single_article7 = (ImageView) findViewById(R.id.single_article7);
            img_single_article8 = (ImageView) findViewById(R.id.single_article8);
            img_single_article9 = (ImageView) findViewById(R.id.single_article9);
            search_article = (ImageButton) findViewById(R.id.search_article);
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
            int id = intent.getIntExtra("id", 0);
            if(id>0){
                new AsyncTaskParseJson().execute(id);
            }else{
                new AsyncTaskParseJson().execute(1);
            }
            Log.e("pseudo", pseudo);




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

        public class AsyncTaskParseJson extends AsyncTask<Integer, String, String> {

            final String TAG = "AsyncTaskParseJson.java";
            // set your json string url here


            // contacts JSONArray

            @Override
            protected void onPreExecute() {}

            @Override
            protected String doInBackground(Integer... args) {
                id=args[0];
                JSONArray dataJsonArr = null;
                StrictMode.ThreadPolicy policy = new
                        StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                String yourJsonStringUrl = "http://timothee-dorand.fr/shuffletrip/show_articles_multi?id="+id;
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
                        String image = c.getString("image");
                        id_article = c.getString("ID");

                        if (image != null) {
                            new DownloadImage(image).execute();
                        }
                        Log.e("TAG", "id"+id_article);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
            @Override
            protected void onPostExecute(String ville){
                Log.e(TAG, "ID : "+id_article);
                search_article.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Add your code in here!
                        if (v==search_article){
                            Log.e("id search", id_article);
                        }
                    }
                });


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
                        int value = 0;
                        if (bitmap.getHeight() <= bitmap.getWidth()) {
                            value = bitmap.getHeight();
                        } else {
                            value = bitmap.getWidth();
                        }

                        Bitmap finalBitmap = null;
                        finalBitmap = Bitmap.createBitmap(bitmap, 0, 0, value, value);
                        switch (imageNb){
                            case 1:
                                img_single_article.setImageBitmap(finalBitmap);
                                break;
                            case 2:
                                img_single_article2.setImageBitmap(finalBitmap);
                                break;
                            case 3:
                                img_single_article3.setImageBitmap(finalBitmap);
                                break;
                            case 4:
                                img_single_article4.setImageBitmap(finalBitmap);
                                break;
                            case 5:
                                img_single_article5.setImageBitmap(finalBitmap);
                                break;
                            case 6:
                                img_single_article6.setImageBitmap(finalBitmap);
                                break;
                            case 7:
                                img_single_article7.setImageBitmap(finalBitmap);
                                break;
                            case 8:
                                img_single_article8.setImageBitmap(finalBitmap);
                                break;
                            case 9:
                                img_single_article9.setImageBitmap(finalBitmap);
                                break;
                        }
                        imageNb++;

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






        }



    }
