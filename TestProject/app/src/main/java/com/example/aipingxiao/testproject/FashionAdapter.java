package com.example.aipingxiao.testproject;

import java.util.List;

public class FashionAdapter {
    private ItemAdapter itemAdapter;
    private List<PicAdapter> pa_list;
    private List<ShopAdapter> sa_list;

    public FashionAdapter(){}
    public FashionAdapter(ItemAdapter itemAdapter,List<PicAdapter> pa_list,List<ShopAdapter> sa_list){
        this.itemAdapter = itemAdapter;
        this.pa_list = pa_list;
        this.sa_list = sa_list;
    }

    public void setItemAdapter(ItemAdapter itemAdapter) {
        this.itemAdapter = itemAdapter;
    }

    public void setPa_list(List<PicAdapter> pa_list) {
        this.pa_list = pa_list;
    }

    public void setSa_list(List<ShopAdapter> sa_list) {
        this.sa_list = sa_list;
    }

    public ItemAdapter getItemAdapter() {
        return itemAdapter;
    }

    public List<PicAdapter> getPa_list() {
        return pa_list;
    }

    public List<ShopAdapter> getSa_list() {
        return sa_list;
    }
}
