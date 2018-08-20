package io.appetizer.hci_fridge.Model;

/**
 * Created by user on 2018/6/24.
 */

public class Fridgeinfo {
    private String fridegId;
    private String nickName;

    public Fridgeinfo(String fridegId,String nickName){
        this.fridegId = fridegId;
        this.nickName = nickName;
    }

    public String getFridegId(){
        return fridegId;
    }
    public String getNickName(){
        if(nickName == null || nickName.length() == 0){
            return fridegId+"";
        }
        return nickName;
    }
    public void setFridegId(String fridegId){
        this.fridegId = fridegId;
    }
    public void setNickName(String nickName){
        this.nickName = nickName;
    }
}
