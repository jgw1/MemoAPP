package com.practice.mission1.InputOutputStream;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class OutputBufferedStreamEX {
    public static void main(String[] args) throws IOException {
        BufferedOutputStream bs = null;
        try {
            bs = new BufferedOutputStream(new FileOutputStream("D:/Eclipse/Java/Output.txt"));
            String str ="오늘 날씨는 아주 좋습니다.";
            bs.write(str.getBytes()); //Byte형으로만 넣을 수 있음

        } catch (Exception e) {
            e.getStackTrace();
            // TODO: handle exception
        }finally {
            bs.close(); //반드시 닫는다.
        }
    }
}
