package com.example.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtils {

    public static ArrayList<String> FileRead(String dest){

        File file = new File(dest);
        byte[] bytes = new byte[1024*1024];
        StringBuffer sb = new StringBuffer();
        try {
            FileInputStream in = new FileInputStream(file);
            int len;
            while ((len = in.read(bytes)) != -1){
                sb.append(new String(bytes,0,len));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuffer sb1 = new StringBuffer();
        ArrayList<String> str = new ArrayList<>();
        int n = 0; //记字数，指示删除的最后位置
        for (int i = 0;i < sb.length();i++){
            n++;
            if ("。".equals(sb.charAt(i)+"")){ //遍历每一个字符，判断是否为句号

                sb1.append(sb.charAt(i));
                if (i != sb.length()-1){
                    if ("”".equals(sb.charAt(i+1)+"")){
                        sb1.append(sb.charAt(i+1));
                        str.add(sb1.toString());
                        sb1.delete(0,n+1);
                        n = 0;
                        continue;
                    }else {
                        str.add(sb1.toString());
                        sb1.delete(0,n);
                        n = 0;
                        continue;
                    }
                }
                str.add(sb1.toString());

                break;
            }
            if ("”".equals(sb.charAt(i)+"") && "。".equals(sb.charAt(i-1)+"")){
                if (i == sb.length()-1){
                    break;
                }
                continue;
            }
            sb1.append(sb.charAt(i));

        }

        return str;

    }


}