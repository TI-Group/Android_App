package io.appetizer.hci_fridge.Model;

/**
 * Created by user on 2018/8/20.
 */

public class Dailyinfo {
    private String name;
    private int num;
    private String time;
    private int itemID;
    private int fridgeID;
    private int userID;

    public Dailyinfo(String name, int num, int itemID, String time, int fridgeID, int userID){
        this.name = name;
        this.num = num;
        this.itemID = itemID;
        this.time = time;
        this.fridgeID = fridgeID;
        this.userID = userID;
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

    public int getFridgeID(){
        return fridgeID;
    }

    public String getTime(){
        return time;
    }

    public int getUserID(){
        return userID;
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

    public void setUserID(int userID){
        this.userID = userID;
    }

    public void setFridgeID(int fridgeID){
        this.fridgeID = fridgeID;
    }
}
