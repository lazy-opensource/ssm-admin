package com.lzy.innovate.utils;

import com.baomidou.mybatisplus.toolkit.StringUtils;

import java.io.File;

/**
 * Created by lzy on 2017/2/25.
 */
public class AutoUpdateClassName {


    private static final String PATH_TEST = "D:/innovate/com/lzy/innovate/service/";
    private static final String PATH_TEST_IMPL = "D:/innovate/com/lzy/innovate/service/impl/";
    private static final String PATH2 = "D:/OpenSource/IDEAWorkSpace/innovate.service/src/main/java/com/lzy/innovate/dubbo/";

    public static void main(String[] args){
        handleInterface(PATH_TEST);
        handleImpl(PATH_TEST_IMPL);
    }

    public static void handleImpl(String path){
        if (StringUtils.isEmpty(path)){
            return;
        }

        File file = new File(path);
        File[] files = file.listFiles();

        for (File f : files){
            if (f.isDirectory()){
                continue;
            }
            String fileN = f.getName();

            if (fileN.contains("Soa")){
                continue;
            }

            int ix = fileN.indexOf(".java");
            String newFileN = "";
            String newClassName = fileN.substring(0 , fileN.indexOf("Impl")) + "SoaImpl";
            if (ix != -1){
                newFileN = newClassName + ".java";
                f.renameTo(new File(path + newFileN));
            }

            HandleTextFile handleTextFile = new HandleTextFile(path + newFileN);
            for (int i = 0; i < handleTextFile.size(); i++){
                String s  = handleTextFile.get(i);
                int ix2 = s.indexOf("extends");


                if (ix2 != -1){
                    int ix3 = s.indexOf("<");

                    String newImpl = "I"+ newClassName.substring(0 , newClassName.indexOf("SoaImpl"));
                    String model = s.substring(s.indexOf(",") + 2 , s.indexOf(">"));
                    StringBuilder sb = new StringBuilder("public class ")
                            .append(newClassName).append(" extends BaseServiceImpl")
                            .append("<").append(newImpl)
                            .append(s.substring(s.indexOf(",") ,s.indexOf("implements")))
                            .append(" implements ").append(newImpl).append("Soa{");

                    handleTextFile.set(i , sb.toString());
                }
            }
            handleTextFile.write(path + newFileN);
        }

    }

    public static void handleInterface(String path){

        if (StringUtils.isEmpty(path)){
            return;
        }

        File file = new File(path);

        System.out.print("begin update file name path --------------"+path);
        System.out.print("\n");
        File[] files = file.listFiles();

        for (File f : files){
            if (f.isDirectory()){
                System.out.print(f.getName()+ " ---------> is dir continue");
                System.out.print("\n");
                continue;
            }

            String fileN = f.getName();
            if (fileN.contains("Soa")){

                System.out.println(f.getName()+ " --------------> do not update continue");
                continue;
            }

            System.out.println("update file before name is " +fileN );
            String className = fileN.substring(0 , fileN.indexOf(".java"));
            String newFileN = className + "Soa" + ".java";
            System.out.println("update file after name is " +newFileN );

            System.out.print("begin rename");
            System.out.print("\n");
            f.renameTo(new File(path + newFileN));
            System.out.print("rename success！");
            System.out.print("\n");


            //------------------- update content ----------------------
            System.out.println("update class name ++++++++++++++++ ");
            HandleTextFile handleTextFile = new HandleTextFile(path + newFileN);
            for (int i = 0; i < handleTextFile.size(); i++){
                String s  = handleTextFile.get(i);
                int ix = s.indexOf("extends");
                if (ix != -1){
                    int ix2 = s.indexOf("<");
                    StringBuilder sb = new StringBuilder("public interface ")
                            .append(className).append("Soa").append(" extends BaseService")
                            .append(s.substring(ix2,s.length()));

                    handleTextFile.set(i , sb.toString());
                }
            }
            handleTextFile.write(path + newFileN);
        }

        System.out.print("\r\n\r\n");
        System.out.println("--------------------------");
        System.out.println("finish ...");
        System.out.println("--------------------------");
    }
}
