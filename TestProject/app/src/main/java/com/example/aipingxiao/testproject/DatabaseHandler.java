package com.example.aipingxiao.testproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.w3c.dom.ProcessingInstruction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper{
    private static final String Tag = "DatabaseHandler";

    //item table
    private static final String ITEM_TABLE = "item";
    private static final String COLUMN_KEY = "item_key";//relate to the page
    private static final String COLUMN_ITEM_ID = "item_id";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_SOLD = "sold";

    //picture table
    private static final String PIC_TABLE = "picture";
    private static final String PIC_ID = "pic_id";
    private static final String PIC_ITEM = "pic_item";
    private static final String PIC_PATH = "path";

    //available table
    private static final String SHOP_TABLE = "shop";
    private static final String SHOP_ID = "shop_id";
    private static final String SHOP_ITEM = "shop_item";
    private static final String SHOP_SIZE = "size";
    private static final String SHOP_AVAIBALBE = "available_id";

    //database
    private static final String DATABASE_NAME = "fashion.db";
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_PATH = null;
    private static String DATABASE_BACKUP_PATH = null;

    private static DatabaseHandler databaseHandler = null;
    private static Context mContext = null;

    //Create table SQL
    private static final String ITEM_CREATE = "CREATE TABLE "
            +ITEM_TABLE
            +" ("
            +COLUMN_KEY + " integer primary key autoincrement,"
            +COLUMN_ITEM_ID +" text not null,"
            +COLUMN_PRICE +" integer not null,"
            +COLUMN_SOLD +" integer not null"
            +");";

    private static final String PIC_CREATE = "CREATE TABLE "
            +PIC_TABLE
            +" ("
            +PIC_ID +" text primary key,"
            +PIC_ITEM +" text not null,"
            +PIC_PATH +" text not null,"
            +" FOREIGN KEY ("+PIC_ITEM+") REFERENCES "+ITEM_TABLE+"("+COLUMN_ITEM_ID+"));";

    private static final String SHOP_CREATE = "CREATE TABLE "
            +SHOP_TABLE
            +" ("
            +SHOP_AVAIBALBE +" text primary key,"
            +SHOP_ID + " text not null,"
            +SHOP_ITEM +" text not null,"
            +SHOP_SIZE + " integer not null,"
            +" FOREIGN KEY ("+SHOP_ITEM + ") REFERENCES "+ITEM_TABLE+"("+COLUMN_ITEM_ID+"));";

    private DatabaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        mContext = context;
        databaseHandler = this;
    }

    //get the database
    public static DatabaseHandler getHandler(Context context){
        if(databaseHandler == null){
            //create the database
            return initialHandler(context);
        }
        return databaseHandler;
    }

    //initialization
    public static DatabaseHandler initialHandler(Context context){
        databaseHandler = new DatabaseHandler(context);

        DATABASE_BACKUP_PATH = "/data/data/" +mContext.getPackageName()+"/database/backup/";
        File backupDir = new File(DATABASE_BACKUP_PATH);
        if(!backupDir.exists()){
            backupDir.mkdir();
        }
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        DATABASE_PATH = db.getPath();
        db.close();
        return databaseHandler;
    }

    public static String getDBPath(){
        return DATABASE_PATH;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ITEM_CREATE);
        db.execSQL(PIC_CREATE);
        db.execSQL(SHOP_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (BuildConfig.DEBUG)
            Log.w(Tag,"Upgrading database from version"+oldVersion+" to "
            +newVersion+",which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS "+ITEM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+PIC_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+SHOP_TABLE);
        onCreate(db);
    }

    //delete the whole database
    public void deleteDatabase(){
        mContext.deleteDatabase(DATABASE_NAME);
        databaseHandler = null;
        DatabaseHandler.initialHandler(mContext);
    }

    //export database,the database will be deleted
    public void exportDatabase(String name) throws IOException{
        databaseHandler.close();
        String outFileName = DATABASE_BACKUP_PATH+name+".db";
        File fromFile = new File(DATABASE_PATH);
        File toFile = new File(outFileName);
        FileInputStream fis = new FileInputStream(fromFile);
        FileOutputStream fos = new FileOutputStream(toFile);
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        fromChannel = fis.getChannel();
        toChannel = fos.getChannel();
        fromChannel.transferTo(0,fromChannel.size(),toChannel);
        if (fromChannel!=null){
            fromChannel.close();
        }
        if (toChannel!=null){
            toChannel.close();
        }
        deleteDatabase();
    }

    //import database
    public void importDatabase(String name) throws IOException{
        databaseHandler.close();
        String inFileName = "";
        if (name.contains(".db")){
            inFileName = DATABASE_BACKUP_PATH+name;
        }else{
            inFileName = DATABASE_BACKUP_PATH+name+".db";
        }

        File fromFile = new File(inFileName);
        File toFile = new File(DATABASE_PATH);
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        fromChannel.transferTo(0,fromChannel.size(),toChannel);
        if (fromChannel!=null){
            fromChannel.close();
        }
        if (toChannel!=null){
            toChannel.close();
        }
    }

    //input item value into database
    public void addItem(ItemAdapter ia){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_ID,ia.getItem_id());
        values.put(COLUMN_PRICE,ia.getPrice());
        values.put(COLUMN_SOLD,ia.getSold());

        db.insert(ITEM_TABLE, null, values);
        db.close();
    }

    //input pic value into database
    public void addPic(PicAdapter pa){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PIC_ID,pa.getPic_id());
        values.put(PIC_ITEM,pa.getPic_item());
        values.put(PIC_PATH,pa.getPath());

        db.insert(PIC_TABLE,null,values);
        db.close();
    }

    //input shop values into database
    public void addShop(ShopAdapter sa){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(SHOP_AVAIBALBE,sa.getShop_available());
        values.put(SHOP_ITEM,sa.getShop_item());
        values.put(SHOP_ID,sa.getShop_id());
        values.put(SHOP_SIZE,sa.getSize());

        db.insert(SHOP_TABLE, null, values);
        db.close();
    }

   //get a set of data from database
    public FashionAdapter getValue(int Item_key){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor item = db.query(ITEM_TABLE, new String[]{
                COLUMN_ITEM_ID,
                COLUMN_PRICE,
                COLUMN_SOLD
        }, COLUMN_KEY + "=?", new String[]{
                Integer.toString(Item_key)
        }, null, null, null, null);

        ItemAdapter ia = null;
        if (item.moveToFirst()){
            ia = new ItemAdapter(
                    item.getString(0),                   //item_id
                    Integer.parseInt(item.getString(1)), //price
                    Integer.parseInt(item.getString(2))  //sold
            );
        }
        item.close();

        Cursor pic = db.query(PIC_TABLE, new String[]{
                PIC_ID,
                PIC_ITEM,
                PIC_PATH}, PIC_ITEM+"=?",new String[]{
                ia.getItem_id()
        },null,null,null,null);

        List<PicAdapter> pa_list = new ArrayList<PicAdapter>();
        //PicAdapter pa = null;
        while(pic.moveToNext()){
            PicAdapter pa = new PicAdapter(
                    pic.getString(0),//pic_id
                    pic.getString(1),//pic_item
                    pic.getString(2) //path
            );
            pa_list.add(pa);
        }
        pic.close();

        Cursor shop = db.query(SHOP_TABLE, new String[]{
                SHOP_AVAIBALBE,
                SHOP_ITEM,
                SHOP_ID,
                SHOP_SIZE}, SHOP_ITEM+"=?", new String[]{
                ia.getItem_id()
        },null,null,null,null);

        List<ShopAdapter> sa_list = new ArrayList<ShopAdapter>();
        while(shop.moveToNext()){
            ShopAdapter sa = new ShopAdapter(
                    shop.getString(0),                 //shop_available
                    shop.getString(1),                 //shop_item
                    shop.getString(2),                 //shop_id
                    Integer.parseInt(shop.getString(3))//shop_size
            );
            sa_list.add(sa);
        }
        shop.close();

        FashionAdapter fs = new FashionAdapter(ia,pa_list,sa_list);
        return fs;
    }

    //check if the database already exists
    public boolean isInDatabase(String ID){
        String CHECK_DB = "SELECT * FROM" +ITEM_TABLE+" WHERE"+COLUMN_ITEM_ID+"="+ID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ITEM_TABLE, new String[]{
                COLUMN_ITEM_ID,
                COLUMN_PRICE,
                COLUMN_SOLD
        }, COLUMN_ITEM_ID + "=?", new String[]{ID}, null, null, null, null);

        int returnCount = cursor.getCount();
        cursor.close();

        //return count
        if(returnCount>0)
                return true;
        else
            return false;
    }

    //get all values from database
    public List<FashionAdapter> getAllValue(){
        List<FashionAdapter> fa_list = new ArrayList<FashionAdapter>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor item = db.query(ITEM_TABLE, new String[]{
                COLUMN_ITEM_ID,
                COLUMN_PRICE,
                COLUMN_SOLD
        },null,null,null,null,null,null);
        if (item.moveToFirst()){
            do{
                FashionAdapter fa = new FashionAdapter();
                ItemAdapter itemAdapter = new ItemAdapter(
                        item.getString(0),
                        Integer.parseInt(item.getString(1)),
                        Integer.parseInt(item.getString(2))
                );
                fa.setItemAdapter(itemAdapter);
                //get picture sets
                Cursor pic = db.query(PIC_TABLE,new String[]{
                        PIC_ID,
                        PIC_ITEM,
                        PIC_PATH
                },PIC_ITEM+"=?",new String[]{item.getString(0)},null,null,null,null);
                List<PicAdapter> pa_list = new ArrayList<PicAdapter>();
                if (pic.moveToFirst()){
                    do{
                        PicAdapter picAdapter = new PicAdapter(
                                pic.getString(0),
                                pic.getString(1),
                                pic.getString(2)
                        );
                        pa_list.add(picAdapter);
                    }while (pic.moveToNext());
                }
                pic.close();
                fa.setPa_list(pa_list);
                //get shop sets
                Cursor shop = db.query(SHOP_TABLE,new String[]{
                        SHOP_AVAIBALBE,
                        SHOP_ITEM,
                        SHOP_ID,
                        SHOP_SIZE
                },SHOP_ITEM+"=?",new String[]{item.getString(0)},null,null,null,null);
                List<ShopAdapter> sa_list = new ArrayList<ShopAdapter>();
                if (shop.moveToFirst()){
                    do{
                        ShopAdapter shopAdapter = new ShopAdapter(
                                shop.getString(0),
                                shop.getString(1),
                                shop.getString(2),
                                Integer.parseInt(shop.getString(3))
                        );
                        sa_list.add(shopAdapter);
                    }while (shop.moveToNext());
                }
                shop.close();
                fa.setSa_list(sa_list);
                fa_list.add(fa);
            }while(item.moveToNext());
        }
        item.close();
        db.close();
        return fa_list;
    }

    //get the number of items
    public int getItemCount(){
        String item_Count = "SELECT * FROM"+ITEM_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor item = db.rawQuery(item_Count, null);
        int count = item.getCount();
        item.close();
        db.close();
        return count;
    }

    //get the number of pics for each item
    public int getPicCount(String itemId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor pic = db.query(PIC_TABLE, new String[]{
                PIC_ID,
                PIC_ITEM,
                PIC_PATH
        }, PIC_ITEM + "=?", new String[]{itemId}, null, null, null, null);
        int count = pic.getCount();
        pic.close();
        db.close();
        return count;
    }

    //get the number of shop available
    public int getShopCount(String itemId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor shop = db.query(SHOP_TABLE, new String[]{
                SHOP_AVAIBALBE,
                SHOP_ITEM,
                SHOP_ID,
                SHOP_SIZE
        }, SHOP_ITEM + "=?", new String[]{itemId}, null, null, null, null);
        int count = shop.getCount();
        shop.close();
        db.close();
        return count;
    }

    //get the size each shop has
    public List<Integer> getShopSize(String itemId,String shopId){
        List<Integer> size_list = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor size = db.query(SHOP_TABLE, new String[]{
                SHOP_AVAIBALBE,
                SHOP_ITEM,
                SHOP_ID,
                SHOP_SIZE
        }, SHOP_ITEM + "=? AND " + SHOP_ID + "=?", new String[]{itemId, shopId}, null, null, null, null);
        if (size.moveToFirst()){
            do{
                size_list.add(size.getInt(3));
            }while(size.moveToNext());
        }
        size.close();
        db.close();
        return size_list;
    }
    //get different shop id
    List<String> getShopNum(String item_id){
        List<String> shop_num = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SHOP_TABLE, new String[]{
                SHOP_ID
        },SHOP_ITEM+"=?",new String[]{item_id},null,null,null,null);

        if (cursor.moveToFirst()){
            do{
                shop_num.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return shop_num;
    }

    //get different size of same shop
    public List<Integer> getShop_Size(String item_id,String shop_id){
        List<Integer> size_list = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SHOP_TABLE,new String[]{
                SHOP_SIZE
        },SHOP_ITEM+"=? AND "+SHOP_ID+"=?",new String[]{item_id,shop_id},null,null,null,null);

        if (cursor.moveToFirst()){
            do{
                size_list.add(cursor.getInt(0));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return size_list;
    }



}
