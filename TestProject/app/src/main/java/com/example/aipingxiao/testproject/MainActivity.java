package com.example.aipingxiao.testproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String Tag = "ButtonActivity";
    private static Context mContext = null;
    private static String url = "http://uat.fashion.cohesivebits.net/api/getItemsPage/";
    List<FashionAdapter> fs_list;

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
            new AsyncTaskParseJson().execute();
        }
    }

    //get the data from URL in background
    public class AsyncTaskParseJson extends AsyncTask<String,String,String>{

        final String TAG = "AsyncTaskParseIson.java";
        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        //contacts JSON array
        JSONArray dataJsonArr = null;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Downloading the data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    AsyncTaskParseJson.this.cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                //instantiate json parser
                JsonParser jParser = new JsonParser();
                //get json string from url
                String json = jParser.getJSONFromUrl(url);
                //get the big array
                JSONArray jArray = new JSONArray(json);
                fs_list = new ArrayList<FashionAdapter>();
                for (int i = 0;i < jArray.length();i++){
                    JSONObject jObject = jArray.getJSONObject(i);
                    FashionAdapter fa = new FashionAdapter();
                    ItemAdapter ia = new ItemAdapter();
                    ia.setItem_id(jObject.getString("id_outside_items"));
                    ia.setPrice(Integer.parseInt(jObject.getString("price")));
                    ia.setSold(Integer.parseInt(jObject.getString("item_sold")));
                    //get the pic array
                    JSONArray picArray = jObject.getJSONArray("photos");
                    List<PicAdapter> pa_list = new ArrayList<PicAdapter>();
                    for(int j = 0;j<picArray.length();i++){
                        JSONObject jObject1 = picArray.getJSONObject(j);
                        PicAdapter pa = new PicAdapter();
                        pa.setPic_id(jObject1.getString("outside_photo_item_id"));
                        pa.setPic_item(jObject1.getString("id_outside_photo"));
                        pa.setPath(jObject1.getString("outside_photo_name"));
                        pa_list.add(pa);
                    }
                    //get the shop list
                    JSONArray shopArray = jObject.getJSONArray("availability");
                    List<ShopAdapter> sa_list = new ArrayList<ShopAdapter>();
                    for (int k = 0;k<shopArray.length();k++){
                        JSONObject jObject2 = shopArray.getJSONObject(k);
                        ShopAdapter sa = new ShopAdapter();
                        sa.setShop_available(jObject2.getString("id_outside_availables"));
                        sa.setShop_item(jObject2.getString("item_id"));
                        sa.setShop_id(jObject2.getString("shop_id"));
                        sa.setSize(Integer.parseInt(jObject2.getString("size_id")));
                        sa_list.add(sa);

                    }
                    fa.setItemAdapter(ia);
                    fa.setPa_list(pa_list);
                    fa.setSa_list(sa_list);
                    fs_list.add(fa);
                }
            }catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)
            addValue(fs_list);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //change the button status
            checkButtonVisible();
            this.progressDialog.dismiss();
            mLoad = false;
            Toast.makeText(mContext,"Load succeed!",Toast.LENGTH_SHORT);

        }
    }

    //put the data into database
    private void addValue(List<FashionAdapter> fa_list){
        if(fa_list != null){
            DatabaseHandler databaseHandler = DatabaseHandler.getHandler(mContext);
            for (int i = 0;i < fa_list.size();i++){
                FashionAdapter fa = new FashionAdapter();
                fa = fa_list.get(i);
                databaseHandler.addItem(fa.getItemAdapter());
                //add the pics
                List<PicAdapter> pa_list = fa.getPa_list();
                for (int j = 0;i<pa_list.size();j++){
                    databaseHandler.addPic(pa_list.get(j));
                }
                //add the shop
                List<ShopAdapter> sa_list = fa.getSa_list();
                for (int k = 0; k < sa_list.size(); k++){
                    databaseHandler.addShop(sa_list.get(k));
                }
            }
        }
    }

    //go to the swipe view
    public void onViewClicked(View view){
        Intent intent = new Intent(this,SwipeActivity.class);
        startActivity(intent);
    }


}
