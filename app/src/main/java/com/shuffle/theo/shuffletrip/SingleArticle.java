package com.shuffle.theo.shuffletrip;

import android.content.Intent;
import android.os.Bundle;
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
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
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

        String result = null;
        InputStream is = null;
        StringBuilder sb=null;
        /*Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/QuinchoScript_PersonalUse.ttf");
        title_ville.setTypeface(face);*/

       // personList = new ArrayList<HashMap<String,String>>();
        //getData();


        home.setOnClickListener(this);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String ville = intent.getStringExtra("ville");

    /*if(ville == null){
        text_id.setText("article "+id);
    }else{
        text_id.setText("article "+id);
        title_ville.setText(ville);
    }*/
        //http post
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://theo-hinfray.fr/IIM/ShuffleTrip/show_article.php");
            //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection"+e.toString());
        }

        //convert response to string
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");
            String line="0";

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            is.close();
            result=sb.toString();

        }catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
        }

        //paring data
        int fd_id;
        String fd_name;
        try{
            JSONArray jArray = new JSONArray(result);
            JSONObject json_data=null;

            for(int i=0;i<jArray.length();i++){
                json_data = jArray.getJSONObject(i);
                title=json_data.getString("title");
                title_view.setText(title);
                //fd_name=json_data.getString("FOOD_NAME");
            }

        }catch(JSONException e1){
            Toast.makeText(getBaseContext(), "No Food Found", Toast.LENGTH_LONG).show();
        }catch (ParseException e1){
            e1.printStackTrace();
        }
    }








   /* public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... arg0) {
                // TODO Auto-generated method stub


                jsonObj = jParser.makeHttpRequest("http://theo-hinfray.fr/IIM/ShuffleTrip/show_article.php");

                try {
                    title = jsonObj.getString("title");
                    /*String describ = jsonObj.getString(TAG_DESCRIB);
                    String ville = jsonObj.getString(TAG_VILLE);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return title;
            }
            protected void onPostExecute(String title){


                title_view.setText(title);
                /*text_id.setText(title);
                System.out.println(title);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }
*/
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
            Intent I_News = new Intent(SingleArticle.this, UserActivity.class);
            this.startActivity(I_News);
        }

    }
}