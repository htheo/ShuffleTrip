package com.shuffle.theo.shuffletrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    public ImageButton home;
    public ImageButton user;
    public ImageButton search;
    public ImageView article_alea;
    public EditText name_ville;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        home = (ImageButton) findViewById(R.id.home);
        user = (ImageButton) findViewById(R.id.user);
        search = (ImageButton) findViewById(R.id.search);
        article_alea = (ImageView) findViewById(R.id.article_alea);
        name_ville = (EditText) findViewById(R.id.name_ville);

        search.setOnClickListener(this);
        user.setOnClickListener(this);
        home.setOnClickListener(this);
        article_alea.setOnClickListener(this);
        
        }




    public void onClick(View v) {

        if (v == user) {

            Intent I_News = new Intent(MainActivity.this, UserActivity.class);
            this.startActivity(I_News);
        }
        if (v == search) {    //si on va sur son compte

            Intent I_News = new Intent(MainActivity.this, SearchActivity.class);
            this.startActivity(I_News);
        }
        if (v == article_alea) {    //si on va sur son compte

            Intent Intent = new Intent(MainActivity.this, SingleArticle.class);
            Random r = new Random();
            int id_alea = 0 + r.nextInt(12 - 0);


            String ville = name_ville.getText().toString();

            Intent.putExtra("id", id_alea);
            Intent.putExtra("ville", ville);


            this.startActivity(Intent);
        }


    }

}
