package com.shuffle.theo.shuffletrip;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements OnClickListener {
    public ImageButton home;
    public ImageButton search;
    public Button register;
    public EditText edit_login;
    public EditText edit_password;
    public String pseudo;
    public String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        home = (ImageButton) findViewById(R.id.home);
        search = (ImageButton) findViewById(R.id.search);
        register = (Button) findViewById(R.id.register);
        edit_login = (EditText) findViewById(R.id.edit_login);
        edit_password = (EditText) findViewById(R.id.edit_password);
        search.setOnClickListener(this);
        home.setOnClickListener(this);
        register.setOnClickListener(this);
    }
    // Create an anonymous implementation of OnClickListener

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.home:

                Intent I_News = new Intent(this, MainActivity.class);
                startActivity(I_News);
                break;
            case R.id.search:

                I_News = new Intent(this, SearchActivity.class);
                startActivity(I_News);
                break;

            case R.id.register:

                pseudo = edit_login.getText().toString();
                password = edit_password.getText().toString();
                new AsyncTaskParseJson().execute(pseudo, password);
                break;

            default:
                break;

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
            String pseudo = args[0];
            String password = args[1];
            String yourJsonStringUrl = "http://theo-hinfray.fr/IIM/ShuffleTrip/inscription.php?pseudo="+pseudo+"&&password="+password;
            try {

                // instantiate our json parser
                JsonParser jParser = new JsonParser();

                // get json string from url
                JSONObject json = jParser.getJSONFromUrl(yourJsonStringUrl);

                // get the array of users
                dataJsonArr = json.getJSONArray("Users");

                // loop through all users
                for (int i = 0; i < dataJsonArr.length(); i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);

                    // Storing each json item in variable
                    pseudo = c.getString("pseudo");

                    // show the values in our logcat
                    Log.e(TAG, "pseudo inscription: " + pseudo);
                    Intent Intent = new Intent(RegisterActivity.this, UserActivity.class);

                    Intent.putExtra("pseudo", pseudo);
                    startActivity(Intent);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {}
    }
}
