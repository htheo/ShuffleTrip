package com.shuffle.theo.shuffletrip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class RegisterActivity extends AppCompatActivity implements OnClickListener {
    public ImageButton home;
    public ImageButton search;
    public ImageButton user;
    public Button register;
    public EditText edit_login;
    public EditText edit_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        home = (ImageButton) findViewById(R.id.home);
        search = (ImageButton) findViewById(R.id.search);
        user = (ImageButton) findViewById(R.id.user);
        register = (Button) findViewById(R.id.register);
        edit_login = (EditText) findViewById(R.id.edit_login);
        edit_password = (EditText) findViewById(R.id.edit_password);
        search.setOnClickListener(this);
        home.setOnClickListener(this);
        user.setOnClickListener(this);
        register.setOnClickListener(this);
    }
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
            case R.id.user:

                I_News = new Intent(this, LoginActivity.class);
                startActivity(I_News);
                break;
            case R.id.register:

                I_News = new Intent(this, RegisterActivity.class);
                startActivity(I_News);
                break;

        }

    }
}
