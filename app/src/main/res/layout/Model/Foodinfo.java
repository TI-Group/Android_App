package sjtu_hci.io.hci_irefrigerator.Model;

/**
 * Created by user on 2018/5/31.
 */

public class Foodinfo {
    private String name;
    private double num;
    private int imageId;

    public Foodinfo(String name, double num, int imageId){
        this.name = name;
        this.num = num;
        this.imageId = imageId;
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

    public void setName(String name){
        this.name = name;
    }

    public void setNum(double num){
        this.num = num;
    }

    public void setImageId(int image){
        this.imageId = imageId;
    }
}
