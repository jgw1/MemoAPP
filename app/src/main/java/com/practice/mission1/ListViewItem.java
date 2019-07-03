package com.practice.mission1;

import android.graphics.drawable.Drawable;

public class ListViewItem {
    private Drawable icon;
    private String text;
    private String text1;

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setText1(String text1){
        this.text1 = text1;
    }

    public Drawable getIcon(){
        return this.icon;
    }
    public String getText(){
        return this.text;
    }
    public String getText1(){
        return this.text1;
    }
}
