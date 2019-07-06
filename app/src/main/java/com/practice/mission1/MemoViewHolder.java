package com.practice.mission1;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MemoViewHolder extends RecyclerView.ViewHolder {


    public MemoViewHolder(View itemView) {
        super(itemView);
    }
    private CheckBox itemCheckbox;
    private TextView itemtxtDateView;
    private TextView itemtxtMemoView;
    private ImageView itemImage;

    public CheckBox getItemCheckbox(){
        return itemCheckbox;
    }
    public void setItemCheckbox(CheckBox itemCheckbox){
        this.itemCheckbox = itemCheckbox;
    }

    public TextView getItemtxtDateView(){
        return itemtxtDateView;
    }
    public void setitemtxtDateView(TextView itemtxtDateView){
        this.itemtxtDateView = itemtxtDateView;
    }

    public TextView getitemtxtMemoView(){
        return itemtxtMemoView;
    }
    public void setitemtxtMemoView(TextView itemtxtMemoView){
        this.itemtxtMemoView = itemtxtMemoView;
    }

    public ImageView getitemImage(){
        return itemImage;
    }
    public void setitemImage(ImageView itemImage){
        this.itemImage = itemImage;
    }




}
