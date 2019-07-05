package com.practice.mission1.InputOutputStream;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class OutputSTreamEX {
    public static void main(String[] args) {
        try {
            OutputStream output = new FileOutputStream("D:/Eclipse/Java/Output.txt");
            String str ="오늘 날씨는 아주 좋습니다.";
            byte[] by=str.getBytes();
            output.write(by);

        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
