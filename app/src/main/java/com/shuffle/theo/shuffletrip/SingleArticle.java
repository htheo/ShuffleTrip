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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.util.HashMap;
import java.util.Map;


public class SingleArticle extends AppCompatActivity implements OnClickListener {

// Swipe
    float x1,x2;
    float y1, y2;

    public ImageButton home;
    public ImageButton search;
    public ImageButton user;
    public String pseudo;
    public Button button_next;
    public Button like_up;
    public Button like_down;
    public String like;

    // Récupération de l'image
    private static final String SERVER_ADRESS = "http://timothee-dorand.fr/shuffletrip/";

    public ImageView img_single_article;
    public String image;
    public String post_id;

    private TextView text_id;
    private TextView title_view;
    private TextView title_ville;
    private TextView likes;
    String myJSON;
    JSONObject jsonObj = null;
    String title;

    private static final String TAG_RESULTS="output";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DESCRIB = "describ";
    private static final String TAG_VILLE ="ville";
    private static final String TAG_IMAGE ="image";

    JSONArray peoples = null;

//    ArrayList<HashMap<String, String>> personList;

    ListView list;

    RequestQueue requestQueue;

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
        likes = (TextView) findViewById(R.id.likes);
        like_up = (Button) findViewById(R.id.like_up);
//        button_next = (Button) findViewById(R.id.button_next);

        // ACTION QUAND CLIQUE SUR NEXT
        Button buttonNext = (Button)findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent Intent = getIntent();
                finish();
                String ville = title_ville.getText().toString();
                startActivity(getIntent());
            }
        });





        InputStream is = null;
        StringBuilder sb=null;
        home.setOnClickListener(this);

        Intent intent = getIntent();
        if((intent.getStringExtra("pseudo") != null)&&(!"false".equals(intent.getStringExtra("pseudo")))){
            pseudo = intent.getStringExtra("pseudo");
        }else{
            pseudo="false";
        }

        Log.e("pseudo", pseudo);
        int id = intent.getIntExtra("id", 0);
        String ville_choisi = intent.getStringExtra("ville");

        img_single_article = (ImageView) findViewById(R.id.img_single_article);


        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpClient httpclient = new DefaultHttpClient();

        HttpGet httpget = new HttpGet("http://timothee-dorand.fr/shuffletrip/show_articles?ville="+ville_choisi);

        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);

                JSONArray arr = new JSONArray(result);
                JSONObject jObj = arr.getJSONObject(0);

                String article_id = jObj.optString("id");

                post_id = jObj.optString("id");
                String article_likes = jObj.optString("like_nb");

                String title = jObj.optString("title");
                post_id = jObj.optString("ID");
                String ville = jObj.optString("ville");
                String nb_like = jObj.optString("like_nb");
                String describ = jObj.optString("describ");
                String image = jObj.optString("image");

                if(image != null) {
                    new DownloadImage(image).execute();
                }
                title_view.setText(title);
                title_ville.setText(ville);
                text_id.setText(describ);
                likes.setText(nb_like);
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
                int value = 0;
                if (bitmap.getHeight() <= bitmap.getWidth()) {
                    value = bitmap.getHeight();
                } else {
                    value = bitmap.getWidth();
                }

                Bitmap finalBitmap = null;
                finalBitmap = Bitmap.createBitmap(bitmap, 0, 0, value, value);
                img_single_article.setImageBitmap(finalBitmap);
                requestQueue = Volley.newRequestQueue(getApplicationContext());

                // ACTION QUAND CLIQUE SUR UP!


                like_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        like = "1";
                        String url ="http://timothee-dorand.fr/shuffletrip/add_like?pseudo="+pseudo+"&like="+like+"&post_id="+post_id;
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                System.out.println(response.toString());
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Log.e("pseudo envoyé", pseudo);

                                Log.e("post envoyé", post_id);
                                Log.e("like envoyé", like);

                                Map<String,String> parameters  = new HashMap<String, String>();
                                return parameters;
                            }
                        };
                        requestQueue.add(request);
                    }

                });
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
            I_News.putExtra("pseudo", pseudo);
            this.startActivity(I_News);
        }
        if (v == search) {  //si on va sur l'accueil
            Intent I_News = new Intent(SingleArticle.this, SearchActivity.class);
            I_News.putExtra("pseudo", pseudo);
            this.startActivity(I_News);
        }
        if (v == user) {  //si on va sur l'accueil
            Intent I_News = new Intent(SingleArticle.this, LoginActivity.class);
            I_News.putExtra("pseudo", pseudo);
            this.startActivity(I_News);
        }


    }
}