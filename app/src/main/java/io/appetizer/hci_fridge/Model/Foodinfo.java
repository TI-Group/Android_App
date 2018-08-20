package io.appetizer.hci_fridge.Model;

/**
 * Created by user on 2018/5/31.
 */
import java.util.Date;
public class Foodinfo {
    private String name;
    private int num;
    private String time;
    private int itemID;
    private Date putInTime;
    private int shelflife;

    public Foodinfo(String name, int num, int itemID, String time, Date putInTime,int shelflife){
        this.name = name;
        this.num = num;
        this.itemID = itemID;
        this.time = time;
        this.putInTime = putInTime;
        this.shelflife = shelflife;
    }

    //TODO java 里面的全局变量 githubstar里面的检查调用方法

    public String getName(){
        return name;
    }

    public int getNum(){
        return num;
    }

    public int getItemID(){
        return itemID;
    }

    public String getTime(){
        return this.time;
    }

    public Date getPutInTime(){
        return putInTime;
    }

    public int getShelflife(){
        return shelflife;
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

    public void setPutInTime(Date putInTime){
        this.putInTime = putInTime;
    }

    public void setShelflife(int shelflife){
        this.shelflife = shelflife;
    }
}
