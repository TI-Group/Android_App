package io.appetizer.hci_fridge.Model;

/**
 * Created by Administrator on 2018/5/15.
 */

public class Userinfo {
    private int role;
    private String username;
    private String password;
    private String tel;

    public Userinfo(){

    }
    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
    public String getTel() {
        return this.tel;
    }
    public int getRole() {
        return this.role;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setTel(String tel){
        this.tel = tel;
    }
    public void setRole(int role){
        this.role = role;
    }
}
