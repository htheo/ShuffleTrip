package com.shuffle.theo.shuffletrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class SearchActivity extends AppCompatActivity implements OnClickListener {
    public ImageButton home;
    public ImageButton search;
    public ImageButton user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        home = (ImageButton) findViewById(R.id.home);
        user = (ImageButton) findViewById(R.id.user);

        home.setOnClickListener(this);
        user.setOnClickListener(this);

    }


    public void onClick(View v) {
        if (v == home) {  //si on va sur l'accueil
            Intent I_News = new Intent(SearchActivity.this, MainActivity.class);
            this.startActivity(I_News);
        }

        if (v == user) {  //si on va sur l'accueil
            Intent I_News = new Intent(SearchActivity.this, UserActivity.class);
            this.startActivity(I_News);
        }

    }
}