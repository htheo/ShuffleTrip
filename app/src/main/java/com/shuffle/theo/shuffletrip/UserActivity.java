package com.shuffle.theo.shuffletrip;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class UserActivity extends AppCompatActivity implements OnClickListener {

    /* VARAIBLES */

    /* TEMPLATE*/
    public ImageButton home;
    public ImageButton search;
    public ImageButton user;
    private TextView pseudo2;

    /* UPLOAD */

    public static String URL = "http://timothee-dorand.fr/shuffletrip/imgapp/SavePicture.php";
    Button btpic, btnup;
    String ba1;
    String mCurrentPhotoPath;
    ImageView mImageView;

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
            btpic = (Button) findViewById(R.id.cpic);
            mImageView = (ImageView) findViewById(R.id.Imageprev);
            btpic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    captureImage();
                }
            });

            btnup = (Button) findViewById(R.id.up);
            btnup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    upload();
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

    }

    /* TEST NEW UPLOAD
    v3...
     * https://trinitytuts.com/capture-image-upload-server-android/ */

    private void upload() {
        Bitmap bm = BitmapFactory.decodeFile(mCurrentPhotoPath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, bao);
        byte[] ba = bao.toByteArray();
//        ba1 = Base64.encodeBytes(ba);
//        encodeBytes ne fonctionne pas
        ba1 = Base64.encodeToString(ba, Base64.DEFAULT );
//        String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);


        // Upload image to server
        new uploadToServer().execute();

    }


    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, 100);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            setPic();
        }
    }

    public class uploadToServer extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(UserActivity.this);

        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Wait image uploading!");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("base64", ba1));
            nameValuePairs.add(new BasicNameValuePair("ImageName", System.currentTimeMillis() + ".jpg"));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(URL);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                String st = EntityUtils.toString(response.getEntity());
                Log.v("log_tag", "In the try Loop" + st);

            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
            }
            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
        }
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.e("Getpath", "Cool" + mCurrentPhotoPath);
        return image;
    }

}


