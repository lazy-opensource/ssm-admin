package com.lzy.innovate.utils;

import com.alibaba.druid.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by lzy on 2017/2/25.
 */
public class HandleTextFile extends ArrayList<String>{

    public static String read(String fileName){

        if (StringUtils.isEmpty(fileName)){
            return "";
        }

        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(fileName).getAbsoluteFile()));
            String s ;
            try {
                while ((s = br.readLine()) != null ){
                    sb.append(s).append("\n");
                }

                return sb.toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }finally{
                if (br != null){
                    try {
                        System.out.print("close buffered reader");
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void write(String fileName, String text){

        if (StringUtils.isEmpty(fileName) || StringUtils.isEmpty(text)){
            return;
        }

        try {
            PrintWriter pw = new PrintWriter(new File(fileName).getAbsoluteFile());
            try{
                pw.println(text);
            }finally {
                if (pw != null){
                    pw.close();
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(String fileName){
        if (StringUtils.isEmpty(fileName)){
            return;
        }

        try {
            PrintWriter pw = new PrintWriter(new File(fileName).getAbsoluteFile());
            try{
                for (String item : this){
                    pw.println(item);
                }
                pw.flush();
            }finally {
                System.out.print("close write");
                pw.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public HandleTextFile(String fileName,String split){
        super(Arrays.asList(read(fileName).split(split)));
        if (get(0).equals(""))
            remove(0);
    }

    public HandleTextFile(String fileNamee){
        this(fileNamee,"\n");
    }

    public static void main(String[] args){
        String path = "D:\\innovate\\com\\lzy\\innovate\\service\\";
        HandleTextFile handleTextFile = new HandleTextFile(path + "IDemoServiceSoa.java");
        for (int i = 0; i < handleTextFile.size(); i++){
            String s  = handleTextFile.get(i);
            int ix = s.indexOf("extends");
            if (ix != -1){
                String ss = "public interface " + "IDemoServiceSoa " + s.substring(ix , s.length());
                handleTextFile.set(i , ss);
            }
        }
        handleTextFile.write(path + "IDemoServiceSoa.java");
    }
}
