package com.practice.mission1;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MEMO implements Serializable {

    private Date date;
    private int secret = 0;
    protected String text;
    private boolean fullDisplayed;
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' hh:mm aaa");
    private boolean checked = false;
    private static DateFormat date_Format = new SimpleDateFormat("yyyy-mm-dd");

    public MEMO(long time, String text,int secret) {
        this.date = new Date(time);
        this.text = text;
        this.secret = secret;
    }

    public MEMO() {
          this.date = new Date();
    }

    public boolean isChecked(){
        return checked;
    }

    public void setChecked(boolean checked){
        this.checked=checked;
    }
    public String getDate() {
        return dateFormat.format(date);
    }
    public String getDate1(){
        return date_Format.format(date);
    }
    public long getTime() {
        return date.getTime();
    }

    public void setTime(long time) {
        this.date = new Date(time);
    }

    public int getSecret(){
        return secret;
    }

    public void setSecret(int secret){
        this.secret =secret;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getText() {
        return this.text;
    }

    public String getShortText() {
        String temp = text.replaceAll("\n", " ");
        if (temp.length() > 25) {
            return temp.substring(0, 25) + "...";
        } else {
            return temp;
        }
    }

    public void setFullDisplayed(boolean fullDisplayed) {
        this.fullDisplayed = fullDisplayed;
    }

    public boolean isFullDisplayed() {
        return this.fullDisplayed;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
