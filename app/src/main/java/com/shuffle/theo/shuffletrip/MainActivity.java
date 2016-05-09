package com.shuffle.theo.shuffletrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

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
        switch (v.getId()) {

            case R.id.user:

                Intent I_News = new Intent(this, LoginActivity.class);
                startActivity(I_News);
                break;
            case R.id.search:
                I_News = new Intent(this, SearchActivity.class);
                this.startActivity(I_News);
                break;
            case R.id.article_alea:
                Intent Intent = new Intent(this, SingleArticle.class);



                String ville = name_ville.getText().toString();

                Intent.putExtra("id", -1);
                Intent.putExtra("ville", ville);


                this.startActivity(Intent);
                break;
        }




    }

}
