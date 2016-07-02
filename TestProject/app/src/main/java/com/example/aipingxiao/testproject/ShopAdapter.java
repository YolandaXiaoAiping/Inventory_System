package com.example.aipingxiao.testproject;

public class ShopAdapter {
    private String Shop_available;
    private String Shop_item;
    private String Shop_id;
    private int Size;

    public ShopAdapter(String shop_available,String shop_item,String shop_id,int shop_size){
        this.Shop_available = shop_available;
        this.Shop_item = shop_item;
        this.Shop_id = shop_id;
        this.Size = shop_size;
    }
    public  ShopAdapter(){}

    public String getShop_available() {
        return Shop_available;
    }

    public String getShop_id() {
        return Shop_id;
    }

    public String getShop_item() {
        return Shop_item;
    }

    public int getSize() {
        return Size;
    }

    public void setShop_available(String shop_available) {
        Shop_available = shop_available;
    }

    public void setShop_id(String shop_id) {
        Shop_id = shop_id;
    }

    public void setShop_item(String shop_item) {
        Shop_item = shop_item;
    }

    public void setSize(int size) {
        Size = size;
    }
}
