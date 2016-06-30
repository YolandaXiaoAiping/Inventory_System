package com.example.aipingxiao.testproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String Tag = "ButtonActivity";
    private static Context mContext = null;
    private static String url = "http://uat.fashion.cohesivebits.net/api/getItemsPage/";

    private static boolean mLoad = false;

    Button viewDatabase;
    Button LoadDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_PROGRESS);//CREATE A PROGRESS BAR
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        mContext = this;
        //You must load the database before view it
        //Button
        viewDatabase = (Button)findViewById(R.id.View_Button);
        LoadDatabase = (Button)findViewById(R.id.Load_Button);
        checkButtonVisible();

        //onclick listener
        viewDatabase.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //go to the swipe page
                        onViewClicked(v);
                    }
                }
        );

        //load the database
        LoadDatabase.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLoadClicked(v);
                    }
                }
        );
    }

    //check button visibility
    public void checkButtonVisible(){
        int DatabaseExist = DatabaseHandler.getHandler(mContext).getItemCount();

        if (BuildConfig.DEBUG){
            Log.d(Tag,Integer.toString(DatabaseExist));
        }
        //if there is no database yet
        if (DatabaseExist == 0){
            viewDatabase.setEnabled(false);
            LoadDatabase.setEnabled(true);
        }else{
            viewDatabase.setEnabled(true);
            LoadDatabase.setEnabled(false);
        }
    }

    public void onLoadClicked(View view){
        //get the url
        if (!mLoad){
            new BackgroundHTTP().execute(url);
        }
    }

    //get the data from URL in background



}
