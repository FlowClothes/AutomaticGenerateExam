package com.example.Utils;

import com.example.bean.Content;
import com.example.bean.Relation;

import java.util.List;
import java.util.Random;

public class RandomUtils {

    public static int[] randomNumber(int num, List<Relation> list){
        //1. 声明整型数组
        int[] numbers=new int[num];
        //2. 思路： 通过一个while循环来不断生成随机数，通过for循环来剔除重复的随机数
        int index = 0;//初始化需要的随机数 个数
        while (true) {
            int random = (int)(Math.random() * list.size() + 1); //生成随机数1~32
            int j = 0;//后面需要对 j 进行判断，需要提升变量作用域
            for ( ; j < numbers.length ; j++) { // 遍历numbers数组，如果出现重复就跳出for循环
                if(random == numbers[j]) break;
            }
            if(j == numbers.length ) numbers[index++] = random; // 说明没有重复，就可以存放随机数字到指定的位置了
            if (index == num) break;//达到numbers数组的容量就结束
        }
        return numbers;
    }

    public static int chooseNumber(int num){ //生成[0,num)的随机数
        Random r = new Random();
        int i = r.nextInt(num);
        return i;
    }

    public static int[] randomContentNumber(int num, List<Content> list){
        //1. 声明整型数组
        int[] numbers=new int[num];
        //2. 思路： 通过一个while循环来不断生成随机数，通过for循环来剔除重复的随机数
        int index = 0;//初始化需要的随机数 个数
        while (true) {
            int random = (int)(Math.random() * list.size() + 1); //生成随机数1~32
            int j = 0;//后面需要对 j 进行判断，需要提升变量作用域
            for ( ; j < numbers.length ; j++) { // 遍历numbers数组，如果出现重复就跳出for循环
                if(random == numbers[j]) break;
            }
            if(j == numbers.length ) numbers[index++] = random; // 说明没有重复，就可以存放随机数字到指定的位置了
            if (index == num) break;//达到numbers数组的容量就结束
        }
        return numbers;
    }

}
