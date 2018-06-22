package io.appetizer.hci_fridge.Model;

/**
 * Created by user on 2018/5/31.
 */

public class Foodinfo {
    private String name;
    private int num;
    private String time;
    private int itemID;

    public Foodinfo(String name, int num, int itemID, String time){
        this.name = name;
        this.num = num;
        this.itemID = itemID;
        this.time = time;
    }

    //TODO java 里面的全局变量 githubstar里面的检查调用方法

    public String getName(){
        return name;
    }

    public double getNum(){
        return num;
    }

    public int getItemID(){
        return itemID;
    }

    public String getTime(){
        return this.time;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setNum(int num){
        this.num = num;
    }

    public void setItemID(int image){
        this.itemID = itemID;
    }

    public void setTime(String time){
        this.time = time;
    }
}
