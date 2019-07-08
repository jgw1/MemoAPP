package com.practice.mission1;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class DownLoadFileTask extends AsyncTask<String,Void,Void> {
    private ProgressDialog dialog;

    public DownLoadFileTask(MainActivity activity){
        dialog = new ProgressDialog(activity);
    }
    @Override
    protected  void onPreExecute(){
        dialog.setMessage("저장하는중입니다!");
        dialog.show();
    }

    @Override
    protected Void doInBackground(String... strings) {
        String a = strings[0];
        String b= strings[1];
        File saveFile = new File("/data/data/com.practice.mission1/"+a+".txt");
        try {

            FileOutputStream fos = new FileOutputStream(saveFile, true);
            fos.write(b.getBytes());

            fos.close();
            Thread.sleep(1000);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void Result){
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }


}