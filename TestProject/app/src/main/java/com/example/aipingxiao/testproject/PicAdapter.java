package com.example.aipingxiao.testproject;

public class PicAdapter {
    private String Pic_id;
    private String Pic_item;
    private String Path;

    public  PicAdapter(String pic_id,String pic_item,String path){
        this.Pic_id = pic_id;
        this.Pic_item = pic_item;
        this.Path = path;
    }

    public String getPath() {
        return Path;
    }

    public String getPic_id() {
        return Pic_id;
    }

    public String getPic_item() {
        return Pic_item;
    }

    public void setPath(String path) {
        Path = path;
    }

    public void setPic_id(String pic_id) {
        Pic_id = pic_id;
    }

    public void setPic_item(String pic_item) {
        Pic_item = pic_item;
    }
}
