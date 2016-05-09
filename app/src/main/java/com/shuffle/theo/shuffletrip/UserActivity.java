package com.shuffle.theo.shuffletrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity implements OnClickListener {
    public ImageButton home;
    public ImageButton search;
    public ImageButton user;
    private TextView pseudo2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        pseudo2.setText("Bonjour "+pseudo);

    }


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
}