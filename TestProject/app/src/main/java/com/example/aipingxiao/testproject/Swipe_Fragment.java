package com.example.aipingxiao.testproject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class Swipe_Fragment extends Fragment {

    //item properties
    TextView item_Id,item_price,item_sold;
    //item position
    private int mID;
    private int mNum;
    private Context mContext;

    private static final String ItemPage = "itemPage";

    //get the position id from swipe activity
    public static Swipe_Fragment newInstance(int position){
        Swipe_Fragment fragment = new Swipe_Fragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ItemPage, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Swipe_Fragment newInstance(){
        Swipe_Fragment swipe_fragment = new Swipe_Fragment();
        return swipe_fragment;
    }

    public static Swipe_Fragment newInstance(int position,int count){
        Swipe_Fragment swipe_fragment = newInstance(position);
        swipe_fragment.mNum = count;
        return swipe_fragment;
    }

    //empty construction
    public Swipe_Fragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mID = getArguments().getInt(ItemPage);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_swipe,container,false);

        //get the information from database
        final FashionAdapter fashionAdapter = DatabaseHandler.getHandler(this.getActivity()).getValue(mID);

        //set item info
        ItemAdapter ia = fashionAdapter.getItemAdapter();
        mContext = this.getActivity();

        item_Id = (TextView) rootView.findViewById(R.id.item_id);
        item_price = (TextView) rootView.findViewById(R.id.item_price);
        item_sold = (TextView) rootView.findViewById(R.id.item_sold);

        item_Id.setText(ia.getItem_id());
        item_price.setText(ia.getPrice());
        item_sold.setText(ia.getSold());

        //dynamically generate imageview
        List<PicAdapter> pa_list = fashionAdapter.getPa_list();
        RelativeLayout relativeLayout = (RelativeLayout)rootView.findViewById(R.id.Pic_layout);

        for(int i = 0;i < pa_list.size();i++){
            PicAdapter pa = pa_list.get(i);
            ImageView imageView = new ImageView(this.getActivity());
            imageView.setLayoutParams(new android.view.ViewGroup.LayoutParams(80,60));
            //image url
            String url = "http://inventory.server.vision/img/"+pa.getPath();
            //get image in background
            new LoadImage(imageView).execute(url);
            relativeLayout.addView(imageView);
        }

        //dynamically generate
        List<String> shop = DatabaseHandler.getHandler(this.getActivity()).getShopNum(ia.getItem_id());
        RelativeLayout shop_button = (RelativeLayout)rootView.findViewById(R.id.shop_button_group);
        final ViewGroup.LayoutParams layoutParams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int j = 0;j < shop.size(); j++){
            Button button = new Button(this.getActivity());
            button.setText(shop.get(j));
            shop_button.addView(button,layoutParams);
            //onclick listener
            final List<Integer> size_list = DatabaseHandler.getHandler(this.getActivity()).getShop_Size(ia.getItem_id(),shop.get(j));
            button.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //dynamically generate size buttons
                            RelativeLayout size_group = (RelativeLayout)rootView.findViewById(R.id.size_button_group);
                            size_group.removeAllViews();//you need to clear the layout first
                            for (int k = 0; k < size_list.size(); k++){
                                Button size_button = new Button(mContext);
                                size_button.setText(size_list.get(k));
                                size_group.addView(size_button,layoutParams);
                            }
                        }
                    }
            );
        }

        return rootView;
    }

    //do in background
    private class LoadImage extends AsyncTask<String,Void,Bitmap>{
        ImageView imageView;

        public LoadImage(ImageView imageView){
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap bitmap = null;
            try{
                InputStream inputStream = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            }catch (Exception e){
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
