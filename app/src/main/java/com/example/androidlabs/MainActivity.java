package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private Button login;
    public SharedPreferences sp;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3);

        sp = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = findViewById(R.id.Email);

        login = (Button) findViewById(R.id.Login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent nextPage = new Intent(MainActivity.this, ProfileActivity.class);
                nextPage.putExtra(SHARED_PREFS, email.getText().toString());
                //openSecondActivity();
                startActivityForResult(nextPage, 345);
            }

        });

        loadData();
    }

    public void saveData() {
        //save what was typed under the name "text"
        String whatWasTyped = email.getText().toString();
        //get an editor object
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(TEXT, whatWasTyped);
        //write it to disk:
        editor.commit();
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        String savedString = sp.getString(TEXT, ""); //fetching the data
        email.setText(savedString); //setting the data
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

}