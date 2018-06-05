package sjtu_hci.io.hci_irefrigerator.Model;

/**
 * Created by Administrator on 2018/5/15.
 */

public class Userinfo {
    private String username;
    private String password;

    Userinfo(){

    }
    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
}
