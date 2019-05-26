package com.tpbank.writeToPdf;

import java.io.File;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class testPrintUTF8 {

    public static void main(String[] args) {

        try {
            File fileDir = new File("/home/thanhdinh/IdeaProjects/JavaSwing/BOT-AMLgr/SaveEstablishText/test.txt");

            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileDir), "UTF8"));

            out.append("Website UTF-16").append("\r\n");
            out.append("Lo%E1%BA%A1i UTF-16").append("\r\n");
            out.append("Lo%E1%BA%A1i UTF-16").append("\r\n");

            out.flush();
            out.close();

        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
