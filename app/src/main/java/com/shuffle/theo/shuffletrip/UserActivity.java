package com.shuffle.theo.shuffletrip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class UserActivity extends AppCompatActivity implements OnClickListener {

    /* VARAIBLES */

    /* TEMPLATE*/
    public ImageButton home;
    public ImageButton search;
    public ImageButton user;
    private TextView pseudo2;

    /* UPLOAD */

    private Button button;
    private String encoded_string, image_name;
    private Bitmap bitmap;
    private File file;
    private Uri file_uri;

    public String ville, titre, description;

    private EditText input_desc, input_ville, input_titre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*Code Théo*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_user);
        home = (ImageButton) findViewById(R.id.home);
        home.setOnClickListener(this);
        user = (ImageButton) findViewById(R.id.user);
        user.setOnClickListener(this);
        search = (ImageButton) findViewById(R.id.search);
        search.setOnClickListener(this);
        pseudo2 = (TextView) findViewById(R.id.pseudo);
        Intent intent = getIntent();
        String pseudo = intent.getStringExtra("pseudo");
        pseudo2.setText("Bonjour " + pseudo);


        /*Code Tim*/

        button = (Button) findViewById(R.id.cpic);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getFileUri();
                i.putExtra(MediaStore.EXTRA_OUTPUT,file_uri);
                startActivityForResult(i,10);
            }
        });

        }

    /* TEMPLATE */

    @Override
    public void onClick(View v) {

        if (v == home) {  //si on va sur l'accueil
            Intent I_News = new Intent(UserActivity.this, MainActivity.class);
            this.startActivity(I_News);
        }
        if (v == search) {  //si on va sur l'accueil
            Intent I_News = new Intent(UserActivity.this, SearchActivity.class);
            this.startActivity(I_News);
        }

/*        EditText input_titre = (EditText)findViewById(R.id.input_titre);
        titre = input_titre.getText().toString();

        EditText input_ville = (EditText)findViewById(R.id.input_ville);
        ville = input_ville.getText().toString();

        EditText input_desc = (EditText)findViewById(R.id.input_desc);
        description = input_desc.getText().toString();*/

    }

    /* TEST NEW UPLOAD
    v3...
     * https://trinitytuts.com/capture-image-upload-server-android/
      *
      * v4 !
      * https://www.youtube.com/watch?v=dV46_-AS4Pg <3
      * */


    private void getFileUri() {

        SimpleDateFormat formater = null;
        Date aujourdhui = new Date();
        formater = new SimpleDateFormat("yyyyMMddHHmmss");

//        Format de la date: AnnéeMoisJoursHeureMinuteSecondes

        image_name = formater.format(aujourdhui)+".jpg";

//        Récupération du formulaire EditText

        EditText input_titre = (EditText)findViewById(R.id.input_titre);
        titre = input_titre.getText().toString();

        EditText input_ville = (EditText)findViewById(R.id.input_ville);
        ville = input_ville.getText().toString();

        EditText input_desc = (EditText)findViewById(R.id.input_desc);
        description = input_desc.getText().toString();


        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + image_name
        );

        file_uri = Uri.fromFile(file);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10 && resultCode == RESULT_OK) {
            new Encode_image().execute();
        }
    }

    private class Encode_image extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            bitmap = BitmapFactory.decodeFile(file_uri.getPath());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            byte[] array = stream.toByteArray();
            encoded_string = Base64.encodeToString(array, 0);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest();
        }
    }

    private void makeRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, "http://timothee-dorand.fr/shuffletrip/connection.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("encoded_string",encoded_string);
                map.put("image_name",image_name);
                map.put("title",titre);
                map.put("describ",description);
                map.put("ville",ville);

                return map;
            }
        };
        requestQueue.add(request);
    }
}
