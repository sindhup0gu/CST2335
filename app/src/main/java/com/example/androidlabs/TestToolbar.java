package com.example.androidlabs;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import static com.example.androidlabs.ProfileActivity.ACTIVITY_NAME;
import static com.example.androidlabs.R.layout.activity_test_toolbar;


public class TestToolbar extends AppCompatActivity {
    Toolbar toolbar;
    private String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_test_toolbar);
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu1:
                if (message == null) {
                    Toast.makeText(this, "This is the initial message", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.menu2:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater layoutInflater = getLayoutInflater();
                final View customDialog = layoutInflater.inflate(R.layout.content_custom_dialog, null);
                builder.setView(customDialog).setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) customDialog.findViewById(R.id.custom_messageText);

                        message = editText.getText().toString();
                        Toast.makeText(builder.getContext(), message, Toast.LENGTH_LONG).show();

                    }

                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();

                return true;
            case R.id.menu3:

                final Snackbar snackBar = Snackbar.make(this.findViewById(R.id.menu3), "Go BACK?", Snackbar.LENGTH_INDEFINITE);

                snackBar.setAction("Yes",new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                finish();
                                //snackBar.dismiss();
                                //onStop();
                            }
                        }).show();
                return true;



            case R.id.menu4:
                Toast.makeText(this, "You clicked on the overflow menu", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void onStop(){
        super.onStop();
        Log.e(ACTIVITY_NAME, "onStop called");
    }
}


