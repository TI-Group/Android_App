package io.appetizer.hci_fridge.Model;

import android.os.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

import io.appetizer.hci_fridge.util.okhttpManager;

/**
 * Created by user on 2018/8/21.
 */

public class Barcodeinfo {
    private String barcode;
    private Date putInTime;
    private int amount;
    private String name ;
    private String imageUrl;
    private okhttpManager manager = new okhttpManager();

    public Barcodeinfo(final String barcode, Date putInTime, int amount, String name, String imageUrl){
        this.barcode = barcode;
        this.putInTime = putInTime;
        this.amount = amount;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    //TODO java 里面的全局变量 githubstar里面的检查调用方法

    public String getBarcode(){
        return barcode;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public String getName(){
        return name;
    }

    public Date getPutInTime(){
        return putInTime;
    }

    public int getAmount(){
        return amount;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setBarcode(String barcode){
        this.barcode = barcode;
    }

    public void setPutInTime(Date putInTime){
        this.putInTime = putInTime;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }
}
