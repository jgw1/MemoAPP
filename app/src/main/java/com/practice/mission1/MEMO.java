package com.practice.mission1;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MEMO implements Serializable {

    private Date date;
    private String secret;
    protected String text;
    private boolean fullDisplayed;
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' hh:mm aaa");
    private boolean checked = false;


    public MEMO(long time, String text) {
        this.date = new Date(time);
        this.text = text;
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

    public long getTime() {
        return date.getTime();
    }

    public void setTime(long time) {
        this.date = new Date(time);
    }

    public String getSecret(){
        return this.secret = secret;
    }

    public void setSecret(String secret){
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
