package com.shuffle.theo.shuffletrip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class UserActivity extends AppCompatActivity implements OnClickListener {
    public ImageButton home;
    public ImageButton search;
    public ImageButton user;
    private TextView pseudo2;


    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String SERVER_ADRESS = "http://timothee-dorand.fr/shuffletrip/SavePicture.php";

    ImageView imageToUpload, downloadedImage;
    Button bUploadImage, bDownloadImage;
    EditText uploadImageName, downloadImageName;

    Button btpic, btnup;
    private Uri fileUri;
    String picturePath;
    Uri selectedImage;
    Bitmap photo;
    String ba1;
    public static String URL = "http://timothee-dorand.fr/shuffletrip/SavePicture.php";


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
        btpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickpic();
            }
        });

        btnup = (Button) findViewById(R.id.up);
        btnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });


        /*OLD UPLOAD*/

        /*super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        downloadedImage = (ImageView) findViewById(R.id.downloadedImage);

//        bSelectImage = (Button) findViewById(R.id.selectImageToUpload);
        bUploadImage = (Button) findViewById(R.id.bUploadImage);
        bDownloadImage = (Button) findViewById(R.id.bDownloadImage);

        uploadImageName = (EditText) findViewById(R.id.etUploadName);
        downloadImageName = (EditText) findViewById(R.id.etDownloadName);

        imageToUpload.setOnClickListener(this);
//        bSelectImage.setOnClickListener(this);
        bUploadImage.setOnClickListener(this);
        bDownloadImage.setOnClickListener(this);*/
    }

    private void upload() {
        // Image location URL
        Log.e("path", "----------------" + picturePath);

        // Image
        Bitmap bm = BitmapFactory.decodeFile(picturePath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        int flag = 0; // On passe par défaut 0 = Base64.DEFAULT
        ba1 = Base64.encodeToString(ba, flag);

        Log.e("base64", "-----" + ba1);

        // Upload image to server
        new uploadToServer().execute();

    }

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

            /*switch (v.getId()) {
                case R.id.imageToUpload:
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                    break;
                case R.id.bUploadImage:
                    Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
                    new UploadImagew(image, uploadImageName.getText().toString()).execute();
                    break;
                case R.id.bDownloadImage:

                    break;

            }*/

        }

    /* TEST NEW UPLOAD */


    private void clickpic() {
        // Check Camera
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // Open default camera
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, 100);

        } else {
            Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {

            selectedImage = data.getData();
            photo = (Bitmap) data.getExtras().get("data");

            // Cursor to get image uri to display

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ImageView imageView = (ImageView) findViewById(R.id.Imageprev);
            imageView.setImageBitmap(photo);
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
}

    /*OLD UPLOAD*/
/*

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                imageToUpload.setImageURI(selectedImage);
            }
        }

        private class UploadImage extends AsyncTask<Void, Void, Void> {
            Bitmap image;
            String name;

            public UploadImage(Bitmap image, String name) {
                this.image = image;
                this.name = name;
            }

            @Override
            protected Void doInBackground(Void... param) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

                // ERREUR NAMEVALUEPAIR DEPRECIE

                ArrayList<NameValuePair> dataToSend = new ArrayList<>();
                dataToSend.add(new BasicNameValuePair("image", encodedImage));
                dataToSend.add(new BasicNameValuePair("name", name));

                HttpParams httpRequestParams = getHttpRequestParams();

                HttpClient client = new DefaultHttpClient(httpRequestParams);
                HttpPost post = new HttpPost(SERVER_ADRESS + "SavePicture.php");

                try {
                    post.setEntity(new UrlEncodedFormEntity(dataToSend));
                    client.execute(post);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                Toast.makeText(getApplicationContext(), "Image mise en ligne !", Toast.LENGTH_SHORT).show();
            }
        }

        private class DownloadImage extends AsyncTask<Void, Void, Bitmap> {
            String name;

            public DownloadImage(String name) {
                this.name = name;
            }

            @Override
            protected Bitmap doInBackground(Void... params) {

                String url = SERVER_ADRESS + "pictures/" + name + ".JPG";

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
                if (bitmap != null) {
                    downloadedImage.setImageBitmap(bitmap);
                }

            }
        }

    private HttpParams getHttpRequestParams() {
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
        return httpRequestParams;
    }
*/




