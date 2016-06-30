package com.example.aipingxiao.testproject;

public class ItemAdapter {
    private String Item_id;
    private int price;
    private int sold;

    public ItemAdapter(String id,int price,int sold){
        this.Item_id = id;
        this.price = price;
        this.sold = sold;
    }

    public String getItem_id() {
        return Item_id;
    }

    public int getPrice() {
        return price;
    }

    public int getSold() {
        return sold;
    }

    public void setItem_id(String item_id) {
        Item_id = item_id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }
}
