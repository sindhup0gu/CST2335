package com.example.androidlabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity {

    EditText secondEmail;
    ImageButton mImageButton;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String ACTIVITY_NAME = "ProfileActivity";
    private Button chatButton;
    private Button toolBarButton;
    private Button weatherForecastButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        mImageButton = findViewById(R.id.mImageButton);
        String savedEmail = intent.getStringExtra("sharedPrefs");
        secondEmail = findViewById(R.id.SecondEmail);
        secondEmail.setText(savedEmail);


        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        chatButton = (Button) findViewById(R.id.chatButton);

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextPage = new Intent(ProfileActivity.this, ChatRoomActivity.class);
                startActivity(nextPage);

            }

        });

        toolBarButton = findViewById(R.id.toolBarButton);

        toolBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent tools = new Intent(ProfileActivity.this, TestToolbar.class);
                startActivity(tools);
            }
        });
        weatherForecastButton = findViewById(R.id.currentWeather);
        weatherForecastButton.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent weatherForecast = new Intent(ProfileActivity.this, WeatherForecast.class);
                startActivity(weatherForecast);
            }
        });

        Log.e(ACTIVITY_NAME, "onCreate called");


    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
        Log.e(ACTIVITY_NAME, "onActivity called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, "onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "onDestroy called");
    }

}