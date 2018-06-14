package io.appetizer.hci_fridge.Model;

/**
 * Created by user on 2018/5/31.
 */

public class Foodinfo {
    private String name;
    private double num;
    private String time;
    private int imageId;

    public Foodinfo(String name, double num, int imageId, String time){
        this.name = name;
        this.num = num;
        this.imageId = imageId;
        this.time = time;
    }

    //TODO java 里面的全局变量 githubstar里面的检查调用方法

    public String getName(){
        return name;
    }

    public double getNum(){
        return num;
    }

    public int getImageId(){
        return imageId;
    }

    public String getTime(){
        return this.time;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setNum(double num){
        this.num = num;
    }

    public void setImageId(int image){
        this.imageId = imageId;
    }

    public void setTime(String time){
        this.time = time;
    }
}
